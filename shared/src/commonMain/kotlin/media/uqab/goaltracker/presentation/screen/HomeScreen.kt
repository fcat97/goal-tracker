package media.uqab.goaltracker.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import media.uqab.goaltracker.presentation.component.ItemTask
import media.uqab.goaltracker.presentation.navigatior.LocalNavigator
import media.uqab.goaltracker.presentation.navigatior.Screen
import media.uqab.goaltracker.presentation.viewmodel.HomeViewModel

class HomeScreen : Screen {
    private val viewModel = HomeViewModel()

    override val name: String
        get() = javaClass.simpleName

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        LaunchedEffect(Unit) {
            viewModel.getTimerTasks()
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator?.navigate(TaskEditScreen())
                    },
                ) {
                    Icon(Icons.Default.Add, null)
                }
            }
        ) { pad ->
            Box(modifier = Modifier.padding(pad), contentAlignment = Alignment.Center) {
                LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    items(items = viewModel.timerTasks) {
                        ItemTask(it) { t ->
                            navigator?.navigate(TimerScreen(t.id))
                        }
                    }
                }
            }
        }
    }
}