package com.mo.stratego.model.player

import com.mo.stratego.model.Piece
import com.mo.stratego.model.map.StartingGrid

//TODO desc
class PlayerLocal(id: Int) : Player(id) {
    override fun processOthersGrid(othersPieces: List<Piece>,
                                   otherGrid: StartingGrid) {
        // do nothing
        // for local players the pieces are already placed and shown
    }
}