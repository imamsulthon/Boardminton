package com.imams.boardminton.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat

fun String.toList(): List<String> {
    val listType = object : TypeToken<List<String?>>() {}.type
    return Gson().fromJson(this, listType)
}

fun List<String>.toJson(): String {
    return Gson().toJson(this)
}
fun <T, M> StateFlow<T>.map(
    coroutineScope: CoroutineScope,
    mapper: (value: T) -> M
): StateFlow<M> = map { mapper(it) }.stateIn(
    coroutineScope,
    SharingStarted.Eagerly,
    mapper(value)
)
fun String.asDateTime(): String? {
    return try {
        val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
        val netDate = this.toLong()
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}