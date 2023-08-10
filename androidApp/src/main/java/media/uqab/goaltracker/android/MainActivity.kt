package media.uqab.goaltracker.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import media.uqab.goaltracker.presentation.navigatior.ScreenNavigator
import media.uqab.goaltracker.presentation.screen.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                remember { ScreenNavigator() }.StartFrom { HomeScreen() }
            }
        }
    }
}
