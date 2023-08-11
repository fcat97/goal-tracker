package media.uqab.goaltracker.utils.sound

class AndroidMediaPlayer(): MediaPlayer {
    override fun playSound(res: String) {
        TODO("Not yet implemented")
    }
}

actual fun getMediaPlayer(): MediaPlayer {
    return AndroidMediaPlayer()
}