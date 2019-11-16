package com.mo.stratego.model

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.player.Player

/**
 * Class that represents a playing piece. It has a rank and an owner.
 */
class Piece(val rank: Rank, val owner: Player, val startPosition: GridPoint2) :
    Entity() {
    val range: Range = Range.getRange(rank)
    private var frontSide: Boolean = owner.id == 0 // true if front is shown
    private val defaultSide: Side = if (owner.id == 0) Side.FRONT else Side.BACK

    init {
        add(PieceComponent(this))
        add(PositionComponent(startPosition))

        // add texture
        when (owner.id) {
            0 -> add(Atlas.getPieceTexture(rank, 0))
            1 -> add(Atlas.getPieceBacksideTexture(1))
        }
    }

    /**
     * Shows front side texture
     */
    fun showFront() {
        if (!frontSide)
            add(Atlas.getPieceTexture(rank, owner.id))
        frontSide = true
    }

    /**
     * Shows back side texture
     */
    fun showBack() {
        if (frontSide)
            add(Atlas.getPieceBacksideTexture(owner.id))
        frontSide = false
    }

    /**
     * Shows default side texture
     */
    fun showDefault() {
        when (defaultSide) {
            Side.FRONT -> showFront()
            Side.BACK  -> showBack()
        }
    }

    /**
     * Returns the piece to the start position.
     */
    fun returnToStartPosition() {
        remove(PositionComponent::class.java)
        add(PositionComponent(startPosition))
    }


    /**
     * @return Whether or not the piece should receive touch events
     */
    fun isTouchable(): Boolean {
        return owner.allow
    }
}