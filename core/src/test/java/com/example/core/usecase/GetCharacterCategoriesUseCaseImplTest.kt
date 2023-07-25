package com.example.core.usecase

import com.example.core.data.repository.CharactersRepository
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.GetCharacterCategoriesUseCase.GetCategoriesParams
import com.example.core.usecase.base.ResultStatus
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.model.ComicFactory
import com.example.testing.model.EventFactory
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@Suppress("TooGenericExceptionThrown")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCharacterCategoriesUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: CharactersRepository

    private val character = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)
    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))

    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    @Before
    fun setUp() {
        getCharacterCategoriesUseCase = GetCharacterCategoriesUseCaseImpl(
            repository,
            mainCoroutineRule.testDispatcherProvider,
        )
    }

    @Test
    fun `should return Success from ResultStatus when get both requests return success`() =
        //
        // deve retornar Success de ResultStatus quando ambos requests retornam com sucesso
        //
        runTest {
            // Arrange
            whenever(repository.getComics(character.id)).thenReturn(comics)
            whenever(repository.getEvents(character.id)).thenReturn(events)

            // Act
            val resultFlow: Flow<ResultStatus<Pair<List<Comic>, List<Event>>>> =
                getCharacterCategoriesUseCase.invoke(
                    GetCategoriesParams(character.id),
                )

            // Assert
            val resultList = resultFlow.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Success)
        }

    @Test
    fun `should return Error from ResultStatus when get events request returns error`() =
        //
        // deve retornar um erro de ResultStatus quando a request de events retornar erro
        //
        runTest {
            // Arrange
            whenever(repository.getComics(character.id)).thenAnswer { throw Throwable() }

            // Act
            val resultFlow: Flow<ResultStatus<Pair<List<Comic>, List<Event>>>> =
                getCharacterCategoriesUseCase.invoke(
                    GetCategoriesParams(character.id),
                )

            // Assert
            val resultList = resultFlow.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Error)
        }

    @Test
    fun `should return Error from ResultStatus when get comics request returns error`() =
        //
        // deve retornar o erro de ResultStatus quando a request de comics retornar o erro
        //
        runTest {
            // Arrange
            whenever(repository.getComics(character.id)).thenReturn(comics)
            whenever(repository.getEvents(character.id)).thenAnswer { throw Throwable() }

            // Act
            val resultFlow: Flow<ResultStatus<Pair<List<Comic>, List<Event>>>> =
                getCharacterCategoriesUseCase.invoke(
                    GetCategoriesParams(character.id),
                )

            // Assert
            val resultList = resultFlow.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Error)
        }
}
