package com.mo.stratego.model

/**
 * Enum of game states
 */
enum class GameState {
    INIT,               // Initialization
    PREPARATION,        // Preparation
    TURN_PLAYER_1,      // Turn player 1
    TURN_PLAYER_2,      // Turn player 2
    GAME_OVER;          // Game ended
}