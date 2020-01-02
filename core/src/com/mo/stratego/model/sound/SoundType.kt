package com.mo.stratego.model.sound

import com.mo.stratego.util.Constants

/**
 * Enum of available sounds. Sound is <= 1MB and stored in memory.
 * @property path Path
 */
enum class SoundType(val path: String) {
    BATTLE_LOST("${Constants.SOUND_FOLDER}/dying.wav"),
    BATTLE_WON("${Constants.SOUND_FOLDER}/win.wav"),
    MOVE("${Constants.SOUND_FOLDER}/move.wav"),
    SPY_CAPTURE("${Constants.SOUND_FOLDER}/spy.wav"),
    EXPLOSION("${Constants.SOUND_FOLDER}/explosion.mp3"),
    DEFUSE_BOMB("${Constants.SOUND_FOLDER}/defuse.wav");


}
