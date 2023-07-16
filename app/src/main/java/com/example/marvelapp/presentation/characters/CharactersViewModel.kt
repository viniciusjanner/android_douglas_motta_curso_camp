package com.example.marvelapp.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.domain.model.Character
import com.example.core.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
) : ViewModel() {

    fun charactersPagingData(query: String): Flow<PagingData<Character>> {
        //
        // O GetCharactersUseCase extend PagingUseCase.
        //
        // Em PagingUseCase temos a funcao -> operator fun invoke...
        //
        // Podemos suprimir aqui o invoke de "getCharactersUseCase.invoke" por causa do modificador operator.
        //
        return getCharactersUseCase(
            GetCharactersUseCase.GetCharactersParams(query, getPageConfig()),
        ).cachedIn(viewModelScope)
    }

    private fun getPageConfig() = PagingConfig(pageSize = 20)
}
