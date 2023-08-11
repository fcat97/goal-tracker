package media.uqab.goaltracker.utils.preference

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class AppPreference private constructor(private val settings: Settings) {
    fun getString(key: String, def: String = ""): String = settings.getString(key, def)
    fun setSetting(key: String, value: String) { settings[key] = value }

    fun getBoolean(key: String, def: Boolean = false): Boolean = settings.getBoolean(key, def)

    fun setBoolean(key: String, value: Boolean) { settings[key] = value }

    companion object {
        private var instance: AppPreference? = null

        @Synchronized
        fun getInstance(): AppPreference {
            if (instance == null) {
                instance = AppPreference(Settings())
            }
            return instance!!
        }

        const val KEY_CLOCK_TICK_SOUND = "play_clock_sound"
        const val KEY_NOTIFICATION_SOUND = "play_notification_sound"
    }
}