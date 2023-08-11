package media.uqab.goaltracker.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import media.uqab.goaltracker.utils.preference.AppPreference

class SettingsViewModel: ViewModel {
    private val settings = AppPreference.getInstance()

    var playClockSound by mutableStateOf(settings.getBoolean(AppPreference.KEY_CLOCK_TICK_SOUND))
    val onClockSettingChange: (Boolean) -> Unit = {
        settings.setBoolean(AppPreference.KEY_CLOCK_TICK_SOUND, it)
        playClockSound = it
    }

    var playNotificationSound by mutableStateOf(settings.getBoolean(AppPreference.KEY_CLOCK_TICK_SOUND))
    val onNotificationSettingChange: (Boolean) -> Unit = {
        settings.setBoolean(AppPreference.KEY_NOTIFICATION_SOUND, it)
        playNotificationSound = it
    }
}