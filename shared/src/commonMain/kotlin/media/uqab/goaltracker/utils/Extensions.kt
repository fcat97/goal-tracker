package media.uqab.goaltracker.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime
import java.time.ZoneId

inline val LocalDateTime.timeInMillis: Long
    get() = this.atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

@Composable
fun hexToColor(hex: String, alpha: Float = 1f): Color {
    val cleanHex = hex.replace("#", "") // Remove '#' if present
    val colorValue = cleanHex.toLongOrNull(16) ?: 0
    val hasAlpha = cleanHex.length == 8 // Check if the hex string includes alpha channel

    val red = ((colorValue shr if (hasAlpha) 16 else 8) and 0xFF).toFloat()
    val green = ((colorValue shr if (hasAlpha) 8 else 0) and 0xFF).toFloat()
    val blue = (colorValue and 0xFF).toFloat()
    val parsedAlpha = (alpha * 255).toInt()

    return Color(red = red / 255f, green = green / 255f, blue = blue / 255f, alpha = parsedAlpha / 255f)
}


fun <T> Collection<T>.sumOf(map: (T) -> Float): Float {
    var sum = 0f
    forEach { sum += map(it) }
    return sum
}
