package com.mo.stratego.model.player

import com.mo.stratego.model.Piece
import com.mo.stratego.model.map.StartingGrid

//TODO desc
class PlayerProxy(id: Int) : Player(id) {
    //TODO exchange othersmove

    override fun processOthersGrid(othersPieces: List<Piece>,
                                   otherGrid: StartingGrid) {
        // TODO exchange via bluetooth
    }
}