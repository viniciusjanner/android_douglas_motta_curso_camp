package com.example.marvelapp.framework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.marvelapp.framework.db.AppDatabase
import com.example.marvelapp.framework.db.entity.CharacterEntity
import com.example.marvelapp.framework.db.entity.RemoteKeyEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

//
// A função principal de RemoteMediator é carregar mais dados da rede quando os Pager dados esgotarem
// ou os dados existentes forem invalidados. Ele inclui um load() método que você deve substituir
// para definir o comportamento de carregamento.
//
// Link: https://developer.android.com/topic/libraries/architecture/paging/v3-network-db#implement-remotemediator
//
@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator @Inject constructor(
    private val query: String,
    private val database: AppDatabase,
    private val remoteDataSource: CharactersRemoteDataSource,
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = database.characterDao()
    private val remoteKeyDao = database.remoteKeyDao()

    @Suppress("ReturnCount")
    override suspend fun load(loadType: LoadType, state: PagingState<Int, CharacterEntity>): MediatorResult {
        return try {
            val offset = when (loadType) {
                // Atualiza a lista
                LoadType.REFRESH -> {
                    0 // Zero representa a página 1
                }

                // Faz o cálculo para adicionar novos itens no topo da lista (exemplo: Instagram)
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                // Adiciona itens ao final da lista.
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.remoteKey()
                    }

                    if (remoteKey.nextOffset == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextOffset
                }
            }

            val queries = hashMapOf("offset" to offset.toString())

            if (query.isNotEmpty()) {
                queries["nameStartsWith"] = query
            }

            // Busca os dados
            val characterPaging = remoteDataSource.fetchCharacters(queries)
            val responseOffset = characterPaging.offset
            val totalCharacters = characterPaging.total

            // Armazena os dados
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearAll()
                    characterDao.clearAll()
                }

                remoteKeyDao.insertOrReplace(
                    RemoteKeyEntity(nextOffset = responseOffset + state.config.pageSize),
                )

                val charactersEntities = characterPaging.characters
                    .map {
                        CharacterEntity(
                            id = it.id,
                            name = it.name,
                            imageUrl = it.imageUrl,
                        )
                    }
                characterDao.insertAll(charactersEntities)
            }

            // Verifica se a paginacao chegou ao fim
            val endOf: Boolean = responseOffset >= totalCharacters

            MediatorResult.Success(endOfPaginationReached = endOf)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
