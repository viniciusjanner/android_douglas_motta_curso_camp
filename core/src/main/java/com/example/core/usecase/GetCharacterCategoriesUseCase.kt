package com.example.core.usecase

import com.example.core.data.repository.CharactersRepository
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.GetCharacterCategoriesUseCase.GetCategoriesParams
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharacterCategoriesUseCase {

    //
    // operator : nos permite suprimir o .invoke na chamada do método.
    //
    operator fun invoke(params: GetCategoriesParams): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetCategoriesParams(val characterId: Int)
}

class GetCharacterCategoriesUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository,
    private val coroutinesDispatchers: CoroutinesDispatchers,
) : UseCase<GetCategoriesParams, Pair<List<Comic>, List<Event>>>(),
    GetCharacterCategoriesUseCase {

    override suspend fun doWork(params: GetCategoriesParams): ResultStatus<Pair<List<Comic>, List<Event>>> {
        return withContext(coroutinesDispatchers.io()) {
            val id = params.characterId
            val comicsDeferred: Deferred<List<Comic>> = async { repository.getComics(id) }
            val eventsDeferred: Deferred<List<Event>> = async { repository.getEvents(id) }

            val comics: List<Comic> = comicsDeferred.await()
            val events: List<Event> = eventsDeferred.await()

            ResultStatus.Success(comics to events)
        }
    }
}
