package com.aibles.common.utils

//TODO: Convert to sealed class so that consumers have to handle all error cases
class NoNetworkException(message: String?): Exception(message)
class NetworkAuthenticationException(message: String?) : Exception(message)
class NetworkServerException(message: String?) : Exception(message)
class NetworkResourceNotFoundException(message: String?) : Exception(message)
class RequestTimeoutException(message: String?) : Exception(message)
class BadRequestException(message: String?) : Exception(message)
class UnknownException(message: String?) : Exception(message)
class NetworkException(message: String? = null) : Exception(message)
