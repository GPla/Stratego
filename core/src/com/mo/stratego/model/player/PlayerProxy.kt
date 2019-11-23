package com.mo.stratego.model.player

import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.MoveType
import com.mo.stratego.model.Piece
import com.mo.stratego.model.PieceFactory
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.map.StartingGrid

//TODO desc
class PlayerProxy(id: PlayerId) : Player(id) {
    //TODO exchange othersmove

    override fun presentGrid(pieces: List<Piece>) {
        // group pieces by rank
        val rankPieces = pieces.toMutableList()
                .groupByTo(mutableMapOf()) { it.rank }

        val origin = GridPoint2(0, 6)

        // place pieces on grid
        startingGrid?.forEachIndexedTransposed { _, _, rank ->
            rankPieces[rank]?.let {
                val piece = it.removeAt(0)
                val move = Grid.translateCellToPosition(origin.cpy())
                piece.add(MoveComponent(move, MoveType.ABSOLUTE))
                PieceFactory.incrementPoint(origin)
            }
        }
    }

    //TODO impl
    override fun processOthersGrid(otherGrid: StartingGrid) {
        // TODO exchange via bluetooth
    }
}