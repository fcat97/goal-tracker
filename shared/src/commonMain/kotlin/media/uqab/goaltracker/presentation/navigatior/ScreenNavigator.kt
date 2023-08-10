package media.uqab.goaltracker.presentation.navigatior

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Stack

/**
 * A simple screen navigator.
 * It only provides basic functions of changing screens.
 */
class ScreenNavigator {
    private var currentScreen by mutableStateOf<Screen?>(null)
    private val screenStack = Stack<Screen>()

    @Composable
    fun StartFrom(onInit: OnNavigatorInit) {
        navigate(onInit(this))

        CompositionLocalProvider(LocalNavigator provides this) {
            currentScreen?.Content()
        }
    }

    fun navigate(screen: Screen) {
        screenStack.push(screen)
        currentScreen = screen
    }

    fun onBackPress() {
        screenStack.pop()
        currentScreen = screenStack.peek()
    }

    companion object {
        private const val TAG = "ScreenNavigator"
    }
}