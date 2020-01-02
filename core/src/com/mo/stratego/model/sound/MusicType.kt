package com.mo.stratego.model.sound

import com.mo.stratego.util.Constants

/**
 * Enum of music. Music has a file size > 1MB and is streamed from
 * disk, not stored in memory.
 *
 * @property path Path
 */
enum class MusicType(val path: String) {
    VICTORY("${Constants.SOUND_FOLDER}/victory.wav"),
    DEFEAT("${Constants.SOUND_FOLDER}/defeat.wav");
}