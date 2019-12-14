package com.mo.stratego.ui

import com.badlogic.gdx.Screen
import com.mo.stratego.GameScreen
import com.mo.stratego.MainMenuScreen
import com.mo.stratego.model.player.PlayerType

/**
 * Enum of screens.
 */
enum class Screens {
    MAINMENU,
    GAME_LOCAL,
    GAME_MULTI;

    /**
     * @return Instance of the corresponding [Screen].
     */
    fun createInstance(): Screen {
        return when (this) {
            MAINMENU   -> MainMenuScreen()
            GAME_LOCAL -> GameScreen(PlayerType.LOCAL)
            GAME_MULTI -> GameScreen(PlayerType.PROXY)
        }
    }
}