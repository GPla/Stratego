package com.mo.stratego.model

import com.badlogic.ashley.core.Entity
import com.mo.stratego.model.component.OwnerComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.player.Player

/**
 * Class that represents a playing piece. It has a rank and an owner.
 */
class Piece(val rank: Rank, val owner : Player) : Entity() {
    init {
        add(PieceComponent(this))
        add(OwnerComponent(owner))
    }
}