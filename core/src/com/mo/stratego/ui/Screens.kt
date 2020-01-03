package com.mo.stratego.ui

import com.badlogic.gdx.Screen
import com.mo.stratego.EndScreen
import com.mo.stratego.GameScreen
import com.mo.stratego.MainMenuScreen
import com.mo.stratego.model.game.GameMode
import com.mo.stratego.model.game.GameResult

/**
 * Enum of screens.
 */
enum class Screens {
    MAINMENU,
    GAME_LOCAL,
    GAME_MULTI,
    GAME_WON,
    GAME_DRAW,
    GAME_LOST;

    /**
     * @return Instance of the corresponding Screen.
     */
    fun createInstance(): Screen {
        return when (this) {
            MAINMENU -> MainMenuScreen()
            GAME_LOCAL -> GameScreen(GameMode.LOCAL)
            GAME_MULTI -> GameScreen(GameMode.MULTI)
            GAME_WON -> EndScreen(GameResult.WON)
            GAME_DRAW -> EndScreen(GameResult.DRAW)
            GAME_LOST -> EndScreen(GameResult.LOST)
        }
    }
}