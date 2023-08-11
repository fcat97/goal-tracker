package media.uqab.goaltracker.utils.sound

interface MediaPlayer {
    fun playSound(res: String)
}

expect fun getMediaPlayer(): MediaPlayer

object MediaPlayerProvider {
    fun playSound(res: String) = getMediaPlayer().playSound(res)
}

object AudioRes {
    val task_done get() = "task_done.wav"
    val clock_tick get() = "clock_tick.wav"
}