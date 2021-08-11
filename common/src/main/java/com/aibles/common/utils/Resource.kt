package com.aibles.common.utils

data class Resource<out T>(val status: Status, val data: T?, val error: Throwable?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(error: Throwable, data: T?= null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                error
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }

    fun isSuccessful() = status == Status.SUCCESS

    fun isLoading() = status == Status.LOADING

    fun isError() = status == Status.ERROR
}

inline fun <X, Y> Resource<X>.map(transform: (X?) -> Y): Resource<Y> = Resource(status, transform(data), error)
