package com.example.pokemon.network

import com.google.gson.Gson
import retrofit2.Response
import java.net.SocketTimeoutException

internal object ApiCallHelper {
    /**
     * Wraps Retrofit calls and is handling exception in general fashion.
     * @param[call] - retrofit call returning Response
     */
    suspend fun <T: Any> safeApiResult(call: suspend () -> Response<T>) : ApiResult<T> =
            safeApiResult("Error", Nothing::class.java, call)

    /**
     * Wraps Retrofit calls and is handling exception in general fashion.
     * @param[errorMessage] - message that will be placed in the ApiResult.Failure
     * @param[call] - retrofit call returning Response
     */
    suspend fun <T: Any> safeApiResult(errorMessage: String, call: suspend ()-> Response<T>) : ApiResult<T> =
            safeApiResult(errorMessage, Nothing::class.java, call)

    /**
     * Wraps Retrofit calls and is handling exception in general fashion.
     * @param[errorMessage] - message that will be placed in the ApiResult.Failure
     * @param[errorClass] - class that will be used by Gson to try to parse error body from non 200 server responses
     * @param[call] - retrofit call returning Response
     */
    suspend fun <T: Any, E: Any> safeApiResult(errorMessage: String, errorClass: Class<E>?, call: suspend ()-> Response<T>) : ApiResult<T>{
        return try {
            val response = call.invoke()
            if(response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                var errorBody: String? = null
                var errorObject: E? = null

                if (errorClass != null && errorClass !is Nothing) {
                    try {
                        errorObject = Gson().fromJson(response.errorBody()?.string(), errorClass)
                    } catch (e: Throwable) {
                        errorBody = response.errorBody()?.string()
                    }
                } else {
                    errorBody = response.errorBody()?.string()
                }
                if (errorBody != null) {
                    ApiResult.Failure<E>("$errorMessage: $errorBody", null, errorObject)
                } else {
                    ApiResult.Failure<E>("$errorMessage", null, errorObject)
                }
            }

        } catch (se3: SocketTimeoutException) {
            ApiResult.Failure<Nothing>("Error: timeout", se3)
        } catch (e: Throwable) {
            ApiResult.Failure<Nothing>("Error. Do you have internet connection? ", e)
        }
    }
}