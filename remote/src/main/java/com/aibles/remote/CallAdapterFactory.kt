package com.aibles.remote

import com.aibles.common.utils.*
import okhttp3.Request
import okhttp3.ResponseBody
import okio.IOException
import okio.Timeout
import retrofit2.*
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.net.ssl.SSLHandshakeException

@Suppress("ThrowableNotThrown")
class CallAdapterFactory private constructor() : CallAdapter.Factory() {
    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = CallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        check (returnType is ParameterizedType) {
            throw IllegalStateException(
                "Response return type must be parameterized as Call<Resource<Foo>> or Call<Resource<out Foo>>")
        }

        val responseType = getParameterUpperBound(0, returnType)

        if (getRawType(responseType) != Resource::class.java) {
            return null
        }

        check (responseType is ParameterizedType) {
            "Response must be parameterized as Resource<Foo> or Resource<out Foo>"
        }

        val successBodyType = getParameterUpperBound(0, responseType)

        val converter = retrofit.nextResponseBodyConverter<Resource<Any>>(null, successBodyType, annotations)

        return BodyCallAdapter(successBodyType, converter)
    }

    private class BodyCallAdapter<T : Any>(
        private val responseType: Type,
        private val converter: Converter<ResponseBody, Resource<T>>
    ) : CallAdapter<T, Call<Resource<T>>> {

        override fun responseType() : Type = responseType

        override fun adapt(call: Call<T>): Call<Resource<T>> {
            return ResourceCall(call, converter)
        }
    }

    internal class ResourceCall<S: Any>(private val delegate: Call<S>, private val converter: Converter<ResponseBody, Resource<S>>) : Call<Resource<S>> {
        override fun enqueue(callback: Callback<Resource<S>>) {
            Timber.d("EO-75 ResourceCall enqueue()")

            delegate.enqueue(object : Callback<S> {
                override fun onFailure(call: Call<S>, t: Throwable) {
                    Timber.d("EO-75 onFailure()")
                    val apiResponse = when (t) {
                        is IOException, is SSLHandshakeException -> Resource.error(
                            NoNetworkException("No network connection"), null)
                        //SSLHandshakeException is thrown when user's internet connection is disconnected
                        //before the server can return response
                        else -> {
                            Resource.error(UnknownException("Unknown exception ${t.message}"), null)
                        }
                    }
                    callback.onResponse(this@ResourceCall, Response.success(apiResponse))
                }

                override fun onResponse(call: Call<S>, response: Response<S>) {
                    val body = response.body()
                    val error = response.errorBody()
                    val code = response.code()

                    if (response.isSuccessful && response.body() != null) {
                        callback.onResponse(this@ResourceCall, Response.success(Resource.success(body!!)))
                    } else {
                        val exception = when (code) {
                            400 -> BadRequestException(null)
                            401,403 -> NetworkAuthenticationException(null)
                            404 -> NetworkResourceNotFoundException(null)
                            408 -> RequestTimeoutException(null)
                            500 -> NetworkServerException(null)
                            else -> NetworkException()
                        }

                        callback.onResponse(this@ResourceCall, Response.success(Resource.error(exception, null)))
                    }
                }
            })
        }

        override fun isExecuted(): Boolean = delegate.isExecuted

        override fun timeout(): Timeout {
            return delegate.timeout()
        }

        override fun clone(): Call<Resource<S>> = ResourceCall(delegate.clone(), converter)

        override fun isCanceled(): Boolean = delegate.isCanceled

        override fun cancel() = delegate.cancel()

        override fun execute(): Response<Resource<S>> {
            throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
        }

        override fun request(): Request = delegate.request()
    }
}
