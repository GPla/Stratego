package com.mo.stratego.model

/**
 * Enum of game states
 */
enum class GameState(val title: String?) {
    INIT(null),
    INIT_PREP_PLAYER_1(null),
    PREPARATION_PLAYER_1("Preparation Player 1"),
    INIT_PREP_PLAYER_2(null),
    PREPARATION_PLAYER_2("Preparation Player 2"),
    TURN_PLAYER_1("Turn Player 1"),
    TURN_PLAYER_2("Turn Player 2"),
    GAME_OVER("Game Over!");

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