package media.uqab.goaltracker.domain.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

inline fun <reified T> encode(data: T): String {
    return Json.encodeToString(serializer(), data)
}

inline fun <reified T> decode(data: String): T {
    return Json.decodeFromString(serializer(), data)
}