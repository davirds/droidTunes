package com.davirdgs.tunes.base

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal inline fun <reified T> T.toJson(): String = Json.encodeToString(this)
