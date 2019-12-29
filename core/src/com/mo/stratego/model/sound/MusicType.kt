package com.mo.stratego.model.sound

import com.mo.stratego.model.sound.SoundProvider.SOUND_PATH

enum class MusicType(val path: String) {
    VICTORY("$SOUND_PATH/victory.wav"),
    DEFEAT("$SOUND_PATH/defeat.wav");
}