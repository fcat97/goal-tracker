package media.uqab.goaltracker.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import media.uqab.goaltracker.presentation.navigatior.LocalNavigator
import media.uqab.goaltracker.presentation.navigatior.Screen
import media.uqab.goaltracker.presentation.viewmodel.TaskEditViewModel

class TaskEditScreen(private val taskId: Long = -1L) : Screen {
    private val viewModel = TaskEditViewModel()

    override val name: String
        get() = javaClass.simpleName

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) { viewModel.getTask(taskId) }

        LaunchedEffect(viewModel.uiEvent) {
            viewModel.uiEvent = viewModel.uiEvent?.let {
                snackBarHostState.showSnackbar(it.msg)
                null
            }
        }

        LaunchedEffect(viewModel.hour, viewModel.minute) {
            viewModel.targetInMillis = (viewModel.hour * 60 + viewModel.minute) * 60_000L // ms
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Goal") },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.onBackPress() }) {
                            Icon(Icons.TwoTone.KeyboardArrowLeft, null)
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveTask()
                            navigator?.onBackPress()
                        }
                    },
                ) {
                    Icon(Icons.TwoTone.Check, null)
                }
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { pad ->
            Column(
                modifier = Modifier.padding(pad).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.taskName,
                    onValueChange = { viewModel.taskName = it },
                    label = {
                        Text("Enter a title")
                    },
                )

                Row {
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.45f),
                        value = viewModel.hour.toString(),
                        onValueChange = { viewModel.hour = it.toIntOrNull() ?: 0 },
                        label = {
                            Text("Target Hour")
                        },
                    )

                    Spacer(Modifier.fillMaxWidth(0.1f))

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.minute.toString(),
                        onValueChange = { viewModel.minute = it.toIntOrNull() ?: 0 },
                        label = {
                            Text("Target Minute")
                        },
                    )
                }

            }
        }
    }
}