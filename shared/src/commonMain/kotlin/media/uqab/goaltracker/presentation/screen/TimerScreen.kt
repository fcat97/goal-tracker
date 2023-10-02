package media.uqab.goaltracker.presentation.screen

import Clock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import media.uqab.goaltracker.presentation.component.BackButton
import media.uqab.goaltracker.presentation.navigatior.LocalNavigator
import media.uqab.goaltracker.presentation.navigatior.Screen
import media.uqab.goaltracker.presentation.viewmodel.TimerViewModel
import media.uqab.goaltracker.presentation.viewmodel.toReadable
import kotlin.time.Duration.Companion.seconds

class TimerScreen(
    private val taskID: Long,
) : Screen {
    private val viewModel = TimerViewModel()

    override val name: String
        get() = javaClass.simpleName

    @Composable
    override fun Content() {
        var hour by remember { mutableStateOf(0) }
        var minute by remember { mutableStateOf(0) }
        var second by remember { mutableStateOf(0) }

        LaunchedEffect(viewModel.currentProgress) {
            viewModel.currentProgress.seconds.toComponents { h, m, s, _ ->
                hour = h.toInt()
                minute = m
                second = s
            }
        }

        LaunchedEffect(Unit) { viewModel.getTask(taskID) }

        var clockSize by remember { mutableStateOf(250f) }
        var fabTop by remember { mutableStateOf(300f) }
        val isOverlapped by remember {
            derivedStateOf {
                fabTop <= clockSize + /*padding of clock */100f
            }
        }

        Scaffold(
            topBar = { AppBar() },
        ) { pad ->
            Box(
                modifier = Modifier.padding(pad).fillMaxSize()
                    .onGloballyPositioned {
                        clockSize =
                            it.size.width.coerceAtMost(it.size.height) - /*padding of clock */100f
                    },
            ) {
                Clock(
                    modifier = Modifier.size(clockSize.dp).align(Alignment.TopCenter),
                    clockSize,
                    second,
                    minute,
                    hour,
                    viewModel.target
                )

                FloatingButton(
                    modifier = Modifier.padding(24.dp)
                        .align(
                            if (isOverlapped) {
                                Alignment.BottomEnd
                            } else {
                                Alignment.BottomCenter
                            }
                        ),
                    onPositionYChange = { fabTop = it}
                )
            }
        }
    }

    @Composable
    private fun AppBar() {
        val coroutine = rememberCoroutineScope()
        val navigator = LocalNavigator.current
        var showMenuItems by remember { mutableStateOf(false) }

        TopAppBar(
            title = { Text(viewModel.taskTitle) },
            navigationIcon = {
                BackButton {
                    coroutine.launch {
                        viewModel.stopTimer()
                        navigator?.onBackPress()
                    }
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
    }

    @Composable
    private fun FloatingButton(modifier: Modifier, onPositionYChange: (Float) -> Unit) {
        val coroutine = rememberCoroutineScope()

        Column(
            modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingActionButton(
                onClick = {
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
                },
                modifier = Modifier.onGloballyPositioned {
                    onPositionYChange(it.positionInRoot().y)
                },
            ) {
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


            Text(
                "${viewModel.currentProgress.toReadable()} / ${viewModel.target.toReadable()}",
            )
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