package com.davirdgs.tunes.base

import androidx.lifecycle.SavedStateHandle
import kotlinx.serialization.json.Json

inline fun <reified T> SavedStateHandle.jsonParam(name: String): T =
    checkNotNull<String>(this[name]).let { Json.decodeFromString<T>(it) }

inline fun <reified T> SavedStateHandle.nullableJsonParam(name: String): T? {
    val value: String? = this[name]
    return if (value != null) {
        Json.decodeFromString<T>(value)
    } else {
        null
    }
}
