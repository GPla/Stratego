package com.mo.stratego.model.game

/**
 * Class that keeps track of the current turn.
 */
class TurnCounter {
    /**
     * Current turn.
     */
    var counter: Int = 0
        private set

    /**
     * Player who starts the game.
     */
    var firstTurn: GameState = GameState.TURN_PLAYER_1

    /**
     * Updates the turn counter in regard to the current state
     * @param state Current state
     */
    fun changedState(state: GameState) {
        if (state == firstTurn)
            ++counter
    }
}