package media.uqab.goaltracker.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.onClick
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import media.uqab.goaltracker.domain.model.TimeTask

@Composable
fun ItemTask(timeTask: TimeTask, onClick: (TimeTask) -> Unit) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(vertical = 2.dp)
            .clickable { onClick(timeTask) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(timeTask.title)

            Text("${String.format("%.2f", timeTask.progressPercentage)}%")
        }
    }
}