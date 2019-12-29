package com.mo.stratego.model.sound

import com.mo.stratego.model.sound.SoundProvider.SOUND_PATH

/**
 * Enum of available sounds.
 * @property path Path
 */
enum class SoundType(val path: String) {
    BATTLE_LOST("$SOUND_PATH/dying.wav"),
    BATTLE_WON("$SOUND_PATH/win.wav"),
    MOVE("$SOUND_PATH/move.wav"),
    SPY_CAPTURE("$SOUND_PATH/spy.wav"),
    EXPLOSION("$SOUND_PATH/explosion.mp3"),
    DEFUSE_BOMB("$SOUND_PATH/defuse.wav");


}
