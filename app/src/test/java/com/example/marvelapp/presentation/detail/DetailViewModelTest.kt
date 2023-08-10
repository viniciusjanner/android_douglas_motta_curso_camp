package com.example.marvelapp.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.domain.model.Comic
import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.CheckFavoriteUseCase
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.core.usecase.RemoveFavoriteUseCase
import com.example.core.usecase.base.ResultStatus
import com.example.marvelapp.R
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.model.ComicFactory
import com.example.testing.model.EventFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@Suppress("MaxLineLength")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    @Mock
    private lateinit var checkFavoriteUseCase: CheckFavoriteUseCase

    @Mock
    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @Mock
    private lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<CategoriesUiActionStateLiveData.UiState>

    private val character = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)
    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))

    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(
            getCharacterCategoriesUseCase,
            checkFavoriteUseCase,
            addFavoriteUseCase,
            removeFavoriteUseCase,
            mainCoroutineRule.testDispatcherProvider,
        ).apply {
            categories.state.observeForever(uiStateObserver)
        }
    }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns success`() =
        //
        // deve notificar uiState com sucesso de UiState quando obter categorias de character
        // retorna sucesso
        //
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            comics to events,
                        ),
                    ),
                )

            // Act
            detailViewModel.categories.actionLoad(character.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<CategoriesUiActionStateLiveData.UiState.Success>())

            val uiStateSuccess = detailViewModel.categories.state.value as CategoriesUiActionStateLiveData.UiState.Success
            val categoriesParentList = uiStateSuccess.detailParentList

            assertEquals(2, categoriesParentList.size)
            assertEquals(
                R.string.details_comics_category,
                categoriesParentList[0].categoryStringResId,
            )
            assertEquals(
                R.string.details_events_category,
                categoriesParentList[1].categoryStringResId,
            )
        }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns only comics`() =
        //
        // deve notificar uiState com sucesso de UiState quando obter categorias de character
        // retorna apenas comics
        //
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            comics to emptyList(),
                        ),
                    ),
                )

            // Act
            detailViewModel.categories.actionLoad(character.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<CategoriesUiActionStateLiveData.UiState.Success>())

            val uiStateSuccess = detailViewModel.categories.state.value as CategoriesUiActionStateLiveData.UiState.Success
            val categoriesParentList = uiStateSuccess.detailParentList

            assertEquals(1, categoriesParentList.size)
            assertEquals(
                R.string.details_comics_category,
                categoriesParentList[0].categoryStringResId,
            )
        }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns only events`() =
        //
        // deve notificar uiState com sucesso de UiState quando obter categorias de character
        // retorna apenas events
        //
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            emptyList<Comic>() to events,
                        ),
                    ),
                )

            // Act
            detailViewModel.categories.actionLoad(character.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<CategoriesUiActionStateLiveData.UiState.Success>())

            val uiStateSuccess = detailViewModel.categories.state.value as CategoriesUiActionStateLiveData.UiState.Success
            val categoriesParentList = uiStateSuccess.detailParentList

            assertEquals(1, categoriesParentList.size)
            assertEquals(
                R.string.details_events_category,
                categoriesParentList[0].categoryStringResId,
            )
        }

    @Test
    fun `should notify uiState with Empty from UiState when get character categories returns an empty result list`() =
        //
        // deve notificar uiState com Empty from UiState quando obter categorias de character
        // retorna uma lista de resultados vazia
        //
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            emptyList<Comic>() to emptyList(),
                        ),
                    ),
                )

            // Act
            detailViewModel.categories.actionLoad(character.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<CategoriesUiActionStateLiveData.UiState.Empty>())
        }

    @Test
    fun `should notify uiState with Error from UiState when get character categories returns an exception`() =
        //
        // deve notificar uiState com erro de UiState quando obter categorias de character
        // retorna uma exceção
        //
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Error(Throwable()),
                    ),
                )

            // Act
            detailViewModel.categories.actionLoad(character.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<CategoriesUiActionStateLiveData.UiState.Error>())
        }
}
