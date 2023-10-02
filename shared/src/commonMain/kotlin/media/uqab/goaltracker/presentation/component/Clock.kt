import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
    modifier: Modifier,
    size: Float,
    seconds: Int,
    minutes: Int,
    hours: Int,
    targetTime: Int
) {
    val density = LocalDensity.current.density
    val center = Offset(density * size / 2f, density * size / 2f) // Center of the clock
    val textColor = MaterialTheme.colors.onBackground

    // Calculate positions for second, minute, and hour arms
    val secondAngle = seconds * 6f - 90f
    val secondArmLength = 100f * density
    val secondArmEnd = calculatePointOnCircle(center, secondAngle, secondArmLength)

    val minuteAngle = minutes * 6f - 90f
    val minuteArmLength = 90f * density
    val minuteArmEnd = calculatePointOnCircle(center, minuteAngle, minuteArmLength)

    val hourAngle = (hours % 12) * 30f - 90f
    val hourArmLength = 60f * density
    val hourArmEnd = calculatePointOnCircle(center, hourAngle, hourArmLength)

    Canvas(modifier = modifier) {
        // Draw clock face with 60-second marks
        for (i in 0 until 60) {
            val angle = i * 6f
            val start = calculatePointOnCircle(center, angle, ((size / 2) - 10) * density)
            val end = calculatePointOnCircle(center, angle, ((size / 2) - 20) * density)

            drawLine(
                textColor,
                start,
                end,
                strokeWidth = if (i % 5 == 0) 3f * density else 1f * density,
                cap = if (i % 5 == 0) StrokeCap.Round else StrokeCap.Butt
            )
        }

        val elapsedTime = (hours * 3600) + (minutes * 60) + seconds
        val remainingTime = maxOf(targetTime - elapsedTime, 0)
        val remainingSeconds = remainingTime % 60
        val remainingMinutes = (remainingTime / 60) % 60
        val remainingHours = remainingTime / 3600
        val remainingHourAngle = (remainingHours % 12) * 30f - 90f
        val remainingMinuteAngle = remainingMinutes * 6f - 90f
        val remainingSecondAngle = remainingSeconds * 6f - 90f

        // Draw remaining time arc lines (hours, minutes, seconds)
        drawArcLine(center, hourAngle, remainingHourAngle, size / 3, Color.Blue, 6f * density)
        drawArcLine(
            center,
            minuteAngle,
            remainingMinuteAngle,
            ((size / 3) - 10),
            Color.Green,
            4f * density
        )
        drawArcLine(
            center,
            secondAngle,
            remainingSecondAngle,
            ((size / 3) - 20),
            Color.Red,
            3f * density
        )

        // Draw second arm
        drawLine(Color.Red, center, secondArmEnd, 2f * density, cap = StrokeCap.Round)

        // Draw minute arm
        drawLine(Color.Green, center, minuteArmEnd, 4f * density, cap = StrokeCap.Round)

        // Draw hour arm
        drawLine(Color.Blue, center, hourArmEnd, 5f * density, cap = StrokeCap.Round)
    }
}

fun DrawScope.drawArcLine(
    center: Offset,
    startAngle: Float,
    endAngle: Float,
    size: Float,
    color: Color,
    strokeWidth: Float
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = endAngle + 90f/* - startAngle*/,
        topLeft = center - Offset(size / 2, size / 2),
        size = Size(size, size),
        useCenter = false,
        style = Stroke(strokeWidth, cap = StrokeCap.Round),
        alpha = 0.1f
    )
}

fun calculatePointOnCircle(center: Offset, angle: Float, radius: Float): Offset {
    val x = center.x + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
    val y = center.y + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
    return Offset(x, y)
}
