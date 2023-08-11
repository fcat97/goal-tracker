package media.uqab.goaltracker.utils.sound

import java.io.BufferedInputStream
import java.io.InputStream
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener


class DesktopMediaPlayer : MediaPlayer, LineListener {
    var isPlaying: Boolean = false
        private set

    override fun playSound(res: String) {
        try {
            javaClass.getResourceAsStream("/audio/$res")?.use {  ins ->
                val bufferedIn: InputStream = BufferedInputStream(ins)
                val audioInputStream: AudioInputStream = AudioSystem.getAudioInputStream(bufferedIn)
                val clip = AudioSystem.getClip()
                clip.open(audioInputStream)
                clip.addLineListener(this)
                clip.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun update(event: LineEvent?) {
        when (event) {
            LineEvent.Type.START -> isPlaying = true
            LineEvent.Type.STOP -> isPlaying = false
        }
    }
}

actual fun getMediaPlayer(): MediaPlayer {
    return DesktopMediaPlayer()
}