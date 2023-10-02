package media.uqab.goaltracker.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import media.uqab.goaltracker.domain.model.TimeTask
import media.uqab.goaltracker.presentation.viewmodel.HomeViewModel

@Composable
fun ItemTask(timeTask: HomeViewModel.ItemTask, isFirst: Boolean, isLast: Boolean, onClick: (TimeTask) -> Unit) {
    val topRound = if (isFirst) 12.dp else 0.dp
    val bottomRound = if (isLast) 12.dp else 0.dp
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(vertical = 2.dp)
            .clickable { onClick(timeTask.t) },
        shape = RoundedCornerShape(
            topStart = topRound,
            topEnd = topRound,
            bottomStart = bottomRound,
            bottomEnd = bottomRound
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(timeTask.title)

            Text("${String.format("%.2f", timeTask.percentage)}%")
        }
    }
}