package com.example.pokemon.utils

import java.io.IOException
import java.io.InputStreamReader

object ResponseReader {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)!!
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}