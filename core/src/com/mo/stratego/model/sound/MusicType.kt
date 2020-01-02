package com.mo.stratego.model.sound

import com.mo.stratego.util.Constants

enum class MusicType(val path: String) {
    VICTORY("${Constants.SOUND_FOLDER}/victory.wav"),
    DEFEAT("${Constants.SOUND_FOLDER}/defeat.wav");
}