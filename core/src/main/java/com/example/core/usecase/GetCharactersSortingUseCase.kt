package com.example.core.usecase

import com.example.core.data.mapper.SortingMapper
import com.example.core.data.repository.StorageRepository
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharactersSortingUseCase {
    //
    // operator : nos permite suprimir o .invoke na chamada do método.
    //
    suspend operator fun invoke(params: Unit = Unit): Flow<Pair<String, String>>
}

class GetCharactersSortingUseCaseImpl @Inject constructor(
    private val storageRepository: StorageRepository,
    private val sortingMapper: SortingMapper,
    private val coroutinesDispatchers: CoroutinesDispatchers,
) : FlowUseCase<Unit, Pair<String, String>>(),
    GetCharactersSortingUseCase {

    override suspend fun createFlowObservable(params: Unit): Flow<Pair<String, String>> {
        return withContext(coroutinesDispatchers.io()) {
            storageRepository.sortingFlow.map { sorting ->
                sortingMapper.mapToPair(sorting)
            }
        }
    }
}
