package com.mo.stratego.ui

import com.badlogic.gdx.Screen
import com.mo.stratego.GameScreen
import com.mo.stratego.MainMenuScreen

/**
 * Enum of screens.
 */
enum class Screens {
    MAINMENU,
    GAME;

    /**
     * @return Instance of the corresponding [Screen].
     */
    fun createInstance(): Screen {
        return when (this) {
            MAINMENU -> MainMenuScreen()
            GAME     -> GameScreen()
        }
    }
}