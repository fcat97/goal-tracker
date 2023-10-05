package media.uqab.goaltracker.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import media.uqab.goaltracker.domain.model.Daily
import media.uqab.goaltracker.domain.model.Monthly
import media.uqab.goaltracker.domain.model.TaskRepeatType
import media.uqab.goaltracker.domain.model.Weekly
import media.uqab.goaltracker.domain.model.Yearly
import media.uqab.goaltracker.domain.model.name
import media.uqab.goaltracker.presentation.component.BackButton
import media.uqab.goaltracker.presentation.component.WeekDaysSelector
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

        Scaffold(topBar = {
            TopAppBar(
                title = { Text("Edit Goal") },
                navigationIcon = {
                    BackButton { navigator?.onBackPress() }
                },
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        if (viewModel.saveTask()) {
                            navigator?.onBackPress()
                        }
                    }
                },
            ) {
                Icon(Icons.TwoTone.Check, null)
            }
        }, snackbarHost = { SnackbarHost(snackBarHostState) }) { pad ->
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

                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    listOf(
                        Daily(1f),
                        Weekly(target = 1f),
                        Monthly(target = 1f),
                        Yearly(target = 1f)
                    ),
                    viewModel.repeat, onSelect = { viewModel.repeat = it },
                )

                AnimatedVisibility(viewModel.repeat is Weekly) {
                    WeekDaysSelector(viewModel.selectedWeekDay) {
                        viewModel.selectedWeekDay = it
                    }
                }
            }
        }
    }

    @Composable
    private fun ExposedDropdownMenuBox(
        modifier: Modifier,
        options: List<TaskRepeatType>,
        selected: TaskRepeatType,
        onSelect: (TaskRepeatType) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        val onDismiss = { expanded = false }
        val rotation by animateFloatAsState(if (expanded) 90f else 0f)

        Box(modifier) {
            TextField(readOnly = true,
                value = selected.name(),
                onValueChange = { },
                label = { Text("Repeat") },
                enabled = false,
                trailingIcon = {
                    Icon(
                        Icons.TwoTone.KeyboardArrowRight, null, modifier = Modifier.rotate(rotation)
                    )
                },
                modifier = Modifier.clickable {
                    expanded = true
                })

            DropdownMenu(expanded, onDismiss) {
                for (item in options) {
                    DropdownMenuItem(onClick = {
                        onSelect(item)
                        onDismiss()
                    }) {
                        Text(text = item.name())
                    }
                }
            }
        }
    }
}