package com.example.pokemon.network

/**
 * ApiResultException allowing to throw ApiResults in Coroutine flow chains
 */
class ApiResultException(message: String, var result: ApiResult.Failure<*>): Throwable() {

}