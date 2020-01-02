package com.mo.stratego.model.sound

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.mo.stratego.StrategoGame
import com.mo.stratego.util.AssetsManager
import com.mo.stratego.util.Constants

/**
 * Provider class for music and sound effects.
 */
object SoundProvider {
    // preference key
    private const val MUSIC_KEY = "Music"
    private const val DEFAULT_MUSIC = true

    /**
     * Currently playing music.
     */
    private val playingMusic = mutableListOf<Music>()

    /**
     * Whether or not sounds are played. Value is saved and retrieved from
     * the persistent game preferences.
     */
    var isTurnedOn: Boolean
        set(value) {
            with(StrategoGame.preferences) {
                putBoolean(MUSIC_KEY, value)
                flush() // save value
            }
        }
        get() {
            with(StrategoGame.preferences) {
                return getBoolean(MUSIC_KEY, DEFAULT_MUSIC)
            }
        }

    /**
     * Plays a sound.
     * @param soundType Sound to play
     * @return Sound handle.
     */
    fun playSound(soundType: SoundType): Long? {
        if (!isTurnedOn)
            return null
        Gdx.app.log(Constants.TAG_SOUND, "sound played: $soundType")
        return AssetsManager.soundMap[soundType]?.play()
    }


    /**
     * Plays music in a loop.
     * @param musicType Music to play
     * @return Music handle.
     */
    fun playMusic(musicType: MusicType): Int? {
        if (!isTurnedOn)
            return null

        Gdx.app.log(Constants.TAG_SOUND, "music played: $musicType")
        val music = Gdx.audio.newMusic(Gdx.files.internal(musicType.path))
        music.isLooping = true
        music.play()
        playingMusic.add(music)
        return playingMusic.indexOf(music)
    }

    /**
     * Stops music.
     * @param musicHandle Music handle from PlayMusic
     */
    fun stopMusic(musicHandle: Int = 0) {
        if (musicHandle in playingMusic.indices)
            playingMusic[musicHandle].stop()
    }
}