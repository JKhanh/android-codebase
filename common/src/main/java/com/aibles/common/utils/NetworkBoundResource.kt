package com.aibles.common.utils

import kotlinx.coroutines.flow.*

inline fun <DB, REMOTE> networkBoundResource(
    crossinline fetchFromLocal: () -> Flow<DB>,
    crossinline shouldFetchFromRemote: (DB?) -> Boolean = { true },
    crossinline fetchFromRemote: suspend () -> Resource<REMOTE>,
    crossinline processRemoteResponse: (response: REMOTE) -> Unit = { },
    crossinline saveRemoteData: suspend (REMOTE) -> Unit = { },
    crossinline onFetchFailed: (errorBody: String?) -> Unit = { }
) = flow {
    emit(Resource.loading())

    val localData = fetchFromLocal().first()

    if(shouldFetchFromRemote(localData)) {
        emit(Resource.loading(localData))

        val response = fetchFromRemote()
        when (response.status){
            Resource.Status.SUCCESS -> {
                processRemoteResponse(response.data!!)
                saveRemoteData(response.data)
                emitAll(fetchFromLocal().map { dbData ->
                    Resource.success(dbData)
                })
            }

            Resource.Status.ERROR -> {
                onFetchFailed(response.error?.message)
                emitAll(fetchFromLocal().map {
                    Resource.error(
                        response.error!!,
                        it
                    )
                })
            }
        }
    } else {
        emitAll(fetchFromLocal().map {
            Resource.success(it)
        })
    }
}