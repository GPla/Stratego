package com.mo.stratego.model

import com.mo.stratego.model.player.PlayerId

/**
 * Enum of game states
 */
enum class GameState(val activePlayerId: PlayerId?, val title: String?) {
    INIT(null, "Preparation"),
    INIT_PLAYER_1(PlayerId.PLAYER1, "Init Player 1"),
    INIT_PLAYER_2(PlayerId.PLAYER2, "Init Player 2"),
    INIT_PREP_PLAYER_1(PlayerId.PLAYER1, null),
    PREPARATION_PLAYER_1(PlayerId.PLAYER1, "Preparation Player 1"),
    INIT_PREP_PLAYER_2(PlayerId.PLAYER2, null),
    PREPARATION_PLAYER_2(PlayerId.PLAYER2, "Preparation Player 2"),
    TURN_PLAYER_1(PlayerId.PLAYER1, "Turn Player 1"),
    TURN_PLAYER_2(PlayerId.PLAYER2, "Turn Player 2"),
    GAME_OVER(null, "Game Over!");

    /**
     * Overrides the ++ operator.
     * A call of this operator sets the next game state.
     * @return Next GameState
     */
    operator fun inc(): GameState {
        return when (this) {
            INIT                 -> INIT_PLAYER_1
            INIT_PLAYER_1        -> INIT_PLAYER_2
            INIT_PLAYER_2        -> INIT_PREP_PLAYER_1
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