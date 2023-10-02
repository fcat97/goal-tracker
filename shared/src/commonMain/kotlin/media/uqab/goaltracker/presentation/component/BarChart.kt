package media.uqab.goaltracker.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

data class Entity(val x: Float, val y: Float)

@Composable
fun BarChart(modifier: Modifier, entity: List<Entity>) {
    var size by remember { mutableStateOf(IntSize(100, 100)) }

    val entityWidth by remember(entity) {
        derivedStateOf {
            try {
                val s = size.width.toFloat() / entity.size
                if (s > 30f) 25f
                else s
            } catch (_: Exception) {
                20f
            }
        }
    }

    Canvas(
        modifier.graphicsLayer {
            rotationX = 180f
        }.onGloballyPositioned {
            size = it.size
        }
    ) {
        entity.forEachIndexed { i, entity ->
            val height = entity.y * size.height / 100

            try {
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(i * entityWidth, height),
                    size = Size(entityWidth, height)
                )
            } catch (e: Exception) {
            }
        }
    }
}