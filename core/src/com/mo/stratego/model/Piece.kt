package com.mo.stratego.model

import com.badlogic.ashley.core.Entity
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.player.Player
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.ui.Atlas

/**
 * Class that represents a playing piece. It has a rank and an owner.
 */
class Piece(val rank: Rank, val owner: Player) :
    Entity() {
    val range: Range = Range.getRange(rank)
    private var frontSide: Boolean =
            owner.id == PlayerId.PLAYER1 // true if front is shown
    private val defaultSide: Side =
            if (owner.id == PlayerId.PLAYER1) Side.FRONT else Side.BACK

    init {
        add(PieceComponent(this))
        add(PositionComponent(rank.getDefaultPosition(owner.id)))

        // add texture
        when (owner.id) {
            PlayerId.PLAYER1 -> add(
                    Atlas.getPieceTexture(rank, owner.id))
            PlayerId.PLAYER2 -> add(
                    Atlas.getPieceBacksideTexture(owner.id))
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
     * Returns the piece to the start position of the piece's [Rank] and owner.
     */
    fun returnToDefaultPosition() {
        // only move if position is not matching the default position
        if (getComponent(PositionComponent::class.java).position !=
                rank.getDefaultPosition(owner.id))
            add(MoveComponent(rank.getDefaultPosition(owner.id),
                              MoveType.ABSOLUTE))
    }


    /**
     * @return Whether or not the piece should receive touch events
     */
    fun isTouchable(): Boolean {
        return owner.allow
    }
}