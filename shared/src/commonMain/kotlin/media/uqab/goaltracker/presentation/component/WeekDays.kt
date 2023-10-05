package media.uqab.goaltracker.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import media.uqab.goaltracker.domain.model.DayOfWeek
import media.uqab.goaltracker.domain.model.WeekDay
import media.uqab.goaltracker.domain.model.weekDays
import media.uqab.goaltracker.utils.hexToColor


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeekDaysSelector(selected: DayOfWeek, onSelect: (DayOfWeek) -> Unit) {
    val darkSurface by remember { mutableStateOf("#2a2a2a") }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (MaterialTheme.colors.isLight) {
                    MaterialTheme.colors.surface
                } else {
                    hexToColor(darkSurface)
                }
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text("Starting Date")

        Spacer(Modifier.height(12.dp))

        LazyRow {
            items(items = weekDays()) {
                Surface(
                    shape = CircleShape,
                    color = if (selected == it) {
                        MaterialTheme.colors.primary
                    } else {
                        MaterialTheme.colors.surface
                    },
                    modifier = Modifier.size(48.dp),
                    onClick = { onSelect(it) },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = it.name.first().toString(),
                            color = if (selected == it) {
                                MaterialTheme.colors.onPrimary
                            } else {
                                MaterialTheme.colors.onSurface
                            },
                        )
                    }
                }
            }
        }
    }
}
