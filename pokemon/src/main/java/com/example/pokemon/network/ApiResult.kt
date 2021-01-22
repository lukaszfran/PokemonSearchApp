package com.example.pokemon.network

/**
 * Represents outcomes of any API call
 */
sealed class ApiResult<out T> {
    /**
     * Successful response
     */
    data class Success<out R>(val value: R): ApiResult<R>()

    /**
     * Failed response
     */
    data class Failure<out E>(
            /**
             * Error message
             */
        val message: String?,
            /**
             * Associated exception if failure was caused by one
             */
        val throwable: Throwable?,
            /**
             * Parsed error response from a server if request body was present and parsed correctly
             */
        val errorResponse: E? = null
    ): ApiResult<Nothing>()
}
