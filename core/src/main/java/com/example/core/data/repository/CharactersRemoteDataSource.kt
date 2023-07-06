package com.example.core.data.repository

interface CharactersRemoteDataSource<T> {

    suspend fun fechtCharacters(queries: Map<String, String>): T
}
