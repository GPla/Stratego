package com.mo.stratego.model.sound

import com.mo.stratego.StrategoGame

/**
 * Provider class for music and sound effects.
 */
object SoundProvider {
    // preference key
    private const val MUSIC_KEY = "Music"
    private const val DEFAULT_MUSIC = true

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


}