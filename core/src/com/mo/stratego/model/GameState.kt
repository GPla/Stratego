package com.mo.stratego.model

/**
 * Enum of game states
 */
enum class GameState {
    INIT,                   // Initialization
    INIT_PREP_PLAYER_1,     // Init preparation for player 1
    PREPARATION_PLAYER_1,   // Preparation player 1
    INIT_PREP_PLAYER_2,      // Init preparation for player 2
    PREPARATION_PLAYER_2,   // Preparation player 2
    TURN_PLAYER_1,          // Turn player 1
    TURN_PLAYER_2,          // Turn player 2
    GAME_OVER;              // Game ended

    /**
     * Overrides the ++ operator.
     * A call of this operator sets the next game state.
     * @return Next GameState
     */
    operator fun inc(): GameState {
        return when (this) {
            INIT                 -> INIT_PREP_PLAYER_1
            INIT_PREP_PLAYER_1   -> PREPARATION_PLAYER_1
            PREPARATION_PLAYER_1 -> INIT_PREP_PLAYER_2
            INIT_PREP_PLAYER_2   -> PREPARATION_PLAYER_2
            PREPARATION_PLAYER_2 -> TURN_PLAYER_1
            TURN_PLAYER_1        -> TURN_PLAYER_2
            TURN_PLAYER_2        -> TURN_PLAYER_1
            GAME_OVER            -> GAME_OVER
        }
    }
}