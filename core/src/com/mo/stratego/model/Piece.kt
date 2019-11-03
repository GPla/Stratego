package com.mo.stratego.model

import com.badlogic.ashley.core.Entity
import com.mo.stratego.model.component.PieceComponent

/**
 * Class that represents a playing piece
 * @property rank Rank of the playing piece
 */
class Piece(val rank: Rank) : Entity() {
    init {
        add(PieceComponent(this))
    }
}