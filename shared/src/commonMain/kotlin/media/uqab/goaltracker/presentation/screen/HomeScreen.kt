package media.uqab.goaltracker.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import media.uqab.goaltracker.domain.model.test
import media.uqab.goaltracker.getPlatform
import media.uqab.goaltracker.presentation.component.BarChart
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
        LaunchedEffect(Unit) {
            test()
            viewModel.getTimerTasks()
        }

        Scaffold(
            topBar = { AppBar() },
            floatingActionButton = { FloatingActionButton() },
        ) { pad ->

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BarChart(
                    modifier = Modifier.fillMaxWidth(0.6f)
                        .height(35.dp),
                    viewModel.chartEntity
                )

                Box(modifier = Modifier.padding(pad)) {
                    TaskList(modifier = Modifier.align(Alignment.TopCenter))

                    NoItem(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

    @Composable
    private fun AppBar() {
        val navigator = LocalNavigator.current

        TopAppBar(
            title = {
                Text("Goal Tracker")
            },
            actions = {
                IconButton(onClick = { navigator?.navigate(SettingsScreen()) }) {
                    Icon(Icons.TwoTone.Settings, null)
                }

                if (getPlatform().name == "JvmPlatform") {
                    IconButton(onClick = onExit) {
                        Icon(Icons.Default.Close, null)
                    }
                }

            }
        )
    }

    @Composable
    private fun FloatingActionButton() {
        val navigator = LocalNavigator.current

        FloatingActionButton(
            onClick = {
                navigator?.navigate(TaskEditScreen())
            },
        ) {
            Icon(Icons.Default.Add, null)
        }
    }

    @Composable
    private fun NoItem(modifier: Modifier) {
        if (viewModel.timerTasks.isEmpty()) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No goal set")
                Text("😥", fontSize = TextUnit(8f, TextUnitType.Em))
            }
        }
    }

    @Composable
    private fun TaskList(modifier: Modifier) {
        val navigator = LocalNavigator.current

        LaunchedEffect(viewModel.selectedDate) {
            viewModel.updatePercentage()
        }

        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            TaskListHeader()

            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                itemsIndexed(items = viewModel.showingTask) { i, it ->
                    ItemTask(it, i == 0, i == viewModel.timerTasks.lastIndex) { t ->
                        navigator?.navigate(TimerScreen(t.id))
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun TaskListHeader() {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = viewModel::selectPreviousDate) {
                Icon(Icons.Default.KeyboardArrowLeft, null)
            }

            Surface(
                modifier = Modifier.weight(1f).padding(8.dp),
                onClick = viewModel::selectToday,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(viewModel.currentDate, style = MaterialTheme.typography.body1)

                    Text(viewModel.currentDateProgress, style = MaterialTheme.typography.subtitle2)
                }
            }

            IconButton(onClick = viewModel::selectNextDate) {
                Icon(Icons.Default.KeyboardArrowRight, null)
            }
        }
    }
}