package media.uqab.goaltracker.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import media.uqab.goaltracker.presentation.component.ItemTask
import media.uqab.goaltracker.presentation.navigatior.LocalNavigator
import media.uqab.goaltracker.presentation.navigatior.Screen
import media.uqab.goaltracker.presentation.viewmodel.HomeViewModel

class HomeScreen(private val onExit: () -> Unit) : Screen {
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
            topBar = {
                TopAppBar(
                    title = {
                        Text("Goal Tracker")
                    },
                    actions = {
                        IconButton(onClick = { navigator?.navigate(SettingsScreen()) }) {
                            Icon(Icons.TwoTone.Settings, null)
                        }
                        IconButton(onClick = onExit) {
                            Icon(Icons.Default.Close, null)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator?.navigate(TaskEditScreen())
                    },
                ) {
                    Icon(Icons.Default.Add, null)
                }
            },
        ) { pad ->

            Box(modifier = Modifier.padding(pad), contentAlignment = Alignment.Center) {
                LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    itemsIndexed(items = viewModel.timerTasks) { i, it ->
                        ItemTask(it, i == 0, i == viewModel.timerTasks.lastIndex) { t ->
                            navigator?.navigate(TimerScreen(t.id))
                        }
                    }
                }

                if (viewModel.timerTasks.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No goal set")
                        Text("😥", fontSize = TextUnit(8f, TextUnitType.Em))
                    }
                }
            }
        }
    }
}