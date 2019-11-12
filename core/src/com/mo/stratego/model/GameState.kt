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

    /**
     * Overrides the ++ operator.
     * A call of this operator sets the next game state.
     * @return Next GameState
     */
    operator fun inc(): GameState {
        return when (this) {
            INIT          -> PREPARATION
            PREPARATION   -> TURN_PLAYER_1
            TURN_PLAYER_1 -> TURN_PLAYER_2
            TURN_PLAYER_2 -> TURN_PLAYER_1
            GAME_OVER     -> GAME_OVER
        }
    }
}