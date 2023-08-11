package media.uqab.goaltracker

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import media.uqab.goaltracker.presentation.navigatior.ScreenNavigator
import media.uqab.goaltracker.presentation.screen.HomeScreen
import media.uqab.goaltracker.presentation.theme.GoalTrackerTheme


fun main() = application {
    val windowState = rememberWindowState(width = 300.dp, height = 500.dp)

    GoalTrackerTheme {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Goal Tracker",
            icon = painterResource("/image/ic_goal.png"),
            transparent = true,
            undecorated = true
        ) {
            Card(shape = RoundedCornerShape(16.dp)) {
                WindowDraggableArea {
                    remember { ScreenNavigator() }.StartFrom {
                        HomeScreen(::exitApplication)
                    }
                }
            }
        }
    }
}