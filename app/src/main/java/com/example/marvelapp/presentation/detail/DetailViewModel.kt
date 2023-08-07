package com.example.marvelapp.presentation.detail

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.marvelapp.R
import com.example.marvelapp.presentation.extensions.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    private val _favoriteUiState = MutableLiveData<FavoriteUiState>()
    val favoriteUiState: LiveData<FavoriteUiState> get() = _favoriteUiState

    init {
        _favoriteUiState.value = FavoriteUiState.FavoriteIcon(R.drawable.ic_favorite_unchecked)
    }

    fun getCharacterCategories(characterId: Int) = viewModelScope.launch {
        //
        // O GetCharacterCategoriesUseCaseImpl extend GetCharacterCategoriesUseCase.
        //
        // Em GetCharacterCategoriesUseCase temos a funcao -> operator fun invoke...
        //
        // Podemos suprimir aqui o invoke de "getCharacterCategoriesUseCase.invoke" por causa do modificador operator.
        //
        getCharacterCategoriesUseCase
            .invoke(
                GetCharacterCategoriesUseCase.GetCategoriesParams(characterId),
            )
            .watchStatus(
                loading = {
                    _uiState.value = UiState.Loading
                },
                success = { data ->
                    val detailParentList = mutableListOf<DetailParentVE>()

                    val comics: List<Comic> = data.first

                    if (comics.isNotEmpty()) {
                        comics.map { comic ->
                            DetailChildVE(comic.id, comic.imageUrl)
                        }.also {
                            detailParentList.add(
                                DetailParentVE(R.string.details_comics_category, it),
                            )
                        }
                    }

                    val events: List<Event> = data.second

                    if (events.isNotEmpty()) {
                        events.map { event ->
                            DetailChildVE(event.id, event.imageUrl)
                        }.also {
                            detailParentList.add(
                                DetailParentVE(R.string.details_events_category, it),
                            )
                        }
                    }

                    _uiState.value =
                        if (detailParentList.isNotEmpty()) {
                            UiState.Success(detailParentList)
                        } else {
                            UiState.Empty
                        }
                },
                error = {
                    UiState.Error
                },
            )
    }

    fun updateFavorite(detailViewArg: DetailViewArg) = viewModelScope.launch {
        detailViewArg.run {
            addFavoriteUseCase.invoke(
                AddFavoriteUseCase.Params(characterId, name, imageUrl),
            ).watchStatus(
                loading = {
                    _favoriteUiState.value = FavoriteUiState.Loading
                },
                success = {
                    _favoriteUiState.value = FavoriteUiState.FavoriteIcon(R.drawable.ic_favorite_checked)
                },
                error = {
                },
            )
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentVE>) : UiState()
        object Error : UiState()
        object Empty : UiState()
    }

    sealed class FavoriteUiState {
        object Loading : FavoriteUiState()
        data class FavoriteIcon(@DrawableRes val icon: Int) : FavoriteUiState()
    }
}
