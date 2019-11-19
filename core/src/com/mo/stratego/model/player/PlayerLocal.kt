package com.mo.stratego.model.player

import com.mo.stratego.model.Piece
import com.mo.stratego.model.map.StartingGrid

//TODO desc
class PlayerLocal(id: Int) : Player(id) {
    override fun presentGrid(pieces: List<Piece>) {
        // do nothing
        // pieces are already in place for local players
    }

    override fun processOthersGrid(otherGrid: StartingGrid) {
        // do nothing
    }
}