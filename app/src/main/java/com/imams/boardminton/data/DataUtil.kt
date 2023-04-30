package com.imams.boardminton.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun String.toList(): List<String> {
    val listType = object : TypeToken<List<String?>>() {}.type
    return Gson().fromJson(this, listType)
}

fun List<String>.toJson(): String {
    return Gson().toJson(this)
}