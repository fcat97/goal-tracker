package media.uqab.goaltracker

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import media.uqab.goaltracker.presentation.navigatior.ScreenNavigator
import media.uqab.goaltracker.presentation.screen.HomeScreen


fun main() = application {
    val windowState = rememberWindowState(width = 300.dp, height = 500.dp)

    Window(onCloseRequest = ::exitApplication, state = windowState, title = "Goal Tracker") {
        remember { ScreenNavigator() }.StartFrom { HomeScreen() }
    }
}