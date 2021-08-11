package com.aibles.common.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <T, A> performGetOperation(databaseQuery: () -> Flow<T>,
                               networkCall: suspend () -> Resource<A>,
                               saveCallResult: suspend (A) -> Unit): Flow<Resource<T>> =
    flow {
        emit(Resource.loading())
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitAll(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Resource.Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)

        } else if (responseStatus.status == Resource.Status.ERROR) {
            emit(Resource.error(responseStatus.error!!, null))
            emitAll(source)
        }
    }
