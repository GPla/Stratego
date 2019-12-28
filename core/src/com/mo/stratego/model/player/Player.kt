package com.mo.stratego.model.player

import com.mo.stratego.model.Move
import com.mo.stratego.model.MoveType
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.game.GameController
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.map.StartingGrid


/**
 * Inspired by https://pdfs.semanticscholar.org/f35c/df2b5cb1a36d703ab6c4a4d80cbaaf3cc603.pdf
 * The [GameController] corresponds to the referee in the paper.
 * This class is a blueprint for a player.
 */
abstract class Player(val id: PlayerId) {
    /**
     * Whether or not the player is allowed to make his move.
     */
    open var allow: Boolean = false

    /**
     * The move made by the player.
     */
    var move: Move? = null

    /**
     * The move made by the other player.
     */
    open var othersMove: Move? = null

    /**
     * Presents the other players move.
     */
    fun presentOthersMove() {
        // get piece from grid and add move
        othersMove?.run {
            val piece = Grid[this.position] ?: return
            piece.add(MoveComponent(this.move, MoveType.RELATIVE))
        }
    }

    /**
     * Grid with the starting position of the players [Piece]s.
     */
    var startingGrid: StartingGrid? = null

    /**
     * Present own starting grid.
     * @param pieces [Piece]s of the player
     */
    abstract fun presentGrid(pieces: List<Piece>)

    /**
     * Process the other players grid.
     * @param otherGrid The [StartingGrid] from the other [Player]
     */
    abstract fun processOthersGrid(otherGrid: StartingGrid)

    /**
     * Number that determines the starting player.
     */
    var startNumber: StartNumber? = null

    /**
     * Process the other players starting number
     * @param number Start number
     */
    abstract fun processOthersStartingNumber(number: StartNumber)

    /**
     * Resets the player to the initial state, so that a
     * new game can be started.
     */
    fun reset() {
        move = null
        othersMove = null
        allow = false
        startingGrid = null
    }

}