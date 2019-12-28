package com.mo.stratego.model.game

import com.mo.stratego.model.player.PlayerType

/**
 * Enum of game modes.
 */
enum class GameMode {
    LOCAL,
    MULTI,
    AI;

    /**
     * @return The [PlayerType] for the second player.
     */
    fun get2PType(): PlayerType =
            when (this) {
                LOCAL -> PlayerType.LOCAL
                MULTI -> PlayerType.PROXY
                AI    -> PlayerType.LOCAL
            }
}