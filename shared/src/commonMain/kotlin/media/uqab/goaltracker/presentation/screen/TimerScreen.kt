package media.uqab.goaltracker.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.twotone.KeyboardArrowLeft
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import media.uqab.goaltracker.presentation.component.ArcProgress
import media.uqab.goaltracker.presentation.navigatior.LocalNavigator
import media.uqab.goaltracker.presentation.navigatior.Screen
import media.uqab.goaltracker.presentation.viewmodel.TimerViewModel
import media.uqab.goaltracker.presentation.viewmodel.toReadable

class TimerScreen(
    private val taskID: Long,
) : Screen {
    private val viewModel = TimerViewModel()

    override val name: String
        get() = javaClass.simpleName

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val coroutine = rememberCoroutineScope()

        val progressPercentage by animateFloatAsState(viewModel.currentPercent)
        var showMenuItems by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.getTask(taskID)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(viewModel.taskTitle) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutine.launch {
                                    viewModel.stopTimer()
                                    navigator?.onBackPress()
                                }
                            },
                        ) {
                            Icon(Icons.TwoTone.KeyboardArrowLeft, null)
                        }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { showMenuItems = true }) {
                                Icon(Icons.TwoTone.MoreVert, null)
                            }

                            MenuItems(showMenuItems, onDismiss = { showMenuItems = false }) {
                                when (it) {
                                    "Edit" -> {
                                        navigator?.navigate(TaskEditScreen(taskID))
                                    }

                                    "Delete" -> {
                                        coroutine.launch {
                                            viewModel.deleteTask()
                                            navigator?.onBackPress()
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            },
        ) { pad ->
            Box(modifier = Modifier.padding(pad), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ArcProgress(
                        canvasSize = 250.dp,
                        indicatorValue = viewModel.currentProgress,
                        maxIndicatorValue = viewModel.target,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Text(String.format("%.2f", progressPercentage) + " %")
                        }
                    }

                    FloatingActionButton(onClick = {
                        when (viewModel.timerState) {
                            TimerViewModel.TimerState.Paused -> {
                                coroutine.launch {
                                    viewModel.startTimer()
                                }
                            }

                            TimerViewModel.TimerState.Stopped -> {
                                coroutine.launch {
                                    viewModel.startTimer()
                                }
                            }

                            TimerViewModel.TimerState.Running -> {
                                coroutine.launch {
                                    viewModel.pauseTimer()
                                }
                            }
                        }
                    }) {
                        when (viewModel.timerState) {
                            TimerViewModel.TimerState.Paused,
                            TimerViewModel.TimerState.Stopped -> {
                                Icon(Icons.Default.PlayArrow, null)
                            }

                            TimerViewModel.TimerState.Running -> {
                                Icon(Icons.Default.Close, null)
                            }
                        }
                    }


                    Text("${viewModel.currentProgress.toReadable()} / ${viewModel.target.toReadable()}")
                }
            }
        }
    }

    @Composable
    fun MenuItems(show: Boolean, onDismiss: () -> Unit, onClick: (String) -> Unit) {
        DropdownMenu(expanded = show, onDismissRequest = onDismiss) {
            listOf(
                "Edit",
                "Delete"
            ).forEach {
                DropdownMenuItem(onClick = { onClick(it) }) {
                    Text(it)
                }
            }
        }
    }
}