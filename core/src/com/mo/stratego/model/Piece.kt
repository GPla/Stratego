package com.mo.stratego.model

import com.badlogic.ashley.core.Entity
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.player.Player

/**
 * Class that represents a playing piece. It has a rank and an owner.
 */
class Piece(val rank: Rank, val owner: Player) : Entity() {
    val range: Range = Range.getRange(rank)

    init {
        add(PieceComponent(this))

        //TODO: change?
        when (owner.id) {
            0 -> add(Atlas.getPieceTexture(rank, 0))
            1 -> add(Atlas.getPieceBacksideTexture(1))
        }
    }
}