package com.mo.stratego.model.game

import com.mo.stratego.model.player.PlayerId

/**
 * Enum of game states
 */
enum class GameState(val activePlayerId: PlayerId?, val title: String?) {
    INIT_PLAYER_1(PlayerId.PLAYER1, "Preparation"),
    INIT_PLAYER_2(PlayerId.PLAYER2, null),
    INIT_PREP_PLAYER_1(PlayerId.PLAYER1, null),
    PREPARATION_PLAYER_1(PlayerId.PLAYER1, "Preparation"),
    INIT_PREP_PLAYER_2(PlayerId.PLAYER2, null),
    PREPARATION_PLAYER_2(PlayerId.PLAYER2, "Opponent's Preparation"),
    GAME_START(null, null),
    TURN_PLAYER_1(PlayerId.PLAYER1, "Your Turn"),
    TURN_PLAYER_2(PlayerId.PLAYER2, "Opponent's Turn"),
    GAME_OVER(null, "Game Over!");

    /**
     * Overrides the ++ operator.
     * A call of this operator sets the next game state.
     * @return Next GameState
     */
    operator fun inc(): GameState {
        return when (this) {
            INIT_PLAYER_1        -> INIT_PLAYER_2
            INIT_PLAYER_2        -> INIT_PREP_PLAYER_1
            INIT_PREP_PLAYER_1   -> PREPARATION_PLAYER_1
            PREPARATION_PLAYER_1 -> INIT_PREP_PLAYER_2
            INIT_PREP_PLAYER_2   -> PREPARATION_PLAYER_2
            PREPARATION_PLAYER_2 -> GAME_START
            GAME_START           -> TURN_PLAYER_1
            TURN_PLAYER_1        -> TURN_PLAYER_2
            TURN_PLAYER_2        -> TURN_PLAYER_1
            GAME_OVER            -> GAME_OVER
        }
    }
}