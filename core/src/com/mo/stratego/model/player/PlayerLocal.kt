package com.mo.stratego.model.player

import com.mo.stratego.model.Piece
import com.mo.stratego.model.map.StartingGrid
import kotlin.random.Random

/**
 * Player, that operate on the running device.
 * @param id Id of player
 */
class PlayerLocal(id: PlayerId) : Player(id) {
    init {
        startNumber = StartNumber(Random.nextInt(255))
    }

    override fun presentGrid(pieces: List<Piece>) {
        // do nothing
        // pieces are already in place for local players
    }

    override fun processOthersGrid(otherGrid: StartingGrid) {
        // do nothing
    }

    override fun processOthersStartingNumber(number: StartNumber) {
        // do nothing
    }
}