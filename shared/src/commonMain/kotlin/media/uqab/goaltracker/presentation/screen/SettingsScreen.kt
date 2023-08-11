package media.uqab.goaltracker.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import media.uqab.goaltracker.presentation.component.BackButton
import media.uqab.goaltracker.presentation.component.SettingItem
import media.uqab.goaltracker.presentation.navigatior.LocalNavigator
import media.uqab.goaltracker.presentation.navigatior.Screen
import media.uqab.goaltracker.presentation.viewmodel.SettingsViewModel

class SettingsScreen : Screen {
    private val viewModel = SettingsViewModel()
    override val name: String
        get() = javaClass.simpleName

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Settings")
                    },
                    navigationIcon = {
                        BackButton { navigator?.onBackPress() }
                    },
                )
            }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize().padding(12.dp)
            ) {
                SettingItem(
                    title = "Clock Tick Sound",
                    details = "Play tick-tick sound when timer is running",
                    isChecked = viewModel.playClockSound,
                    onCheckChange = viewModel.onClockSettingChange
                )

                SettingItem(
                    title = "Notification Sound",
                    isChecked = viewModel.playNotificationSound,
                    onCheckChange = viewModel.onNotificationSettingChange
                )
            }
        }
    }
}