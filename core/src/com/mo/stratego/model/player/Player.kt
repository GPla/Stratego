package com.mo.stratego.model.player

import com.mo.stratego.model.GameController
import com.mo.stratego.model.Move
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.map.Grid


/**
 * Inspired by https://pdfs.semanticscholar.org/f35c/df2b5cb1a36d703ab6c4a4d80cbaaf3cc603.pdf
 * The [GameController] corresponds to the referee in the paper.
 * This class is a blueprint for a player.
 */
abstract class Player(val id: Int) {

    /**
     * Whether or not the player is allowed to make his move.
     */
    var allow: Boolean = false

    /**
     * The move made by the player.
     */
    var move: Move? = null

    /**
     * The move made by the other player.
     */
    var othersMove: Move? = null

    /**
     * Presents the other players move.
     */
    fun present() {
        // get piece from grid and add move
        othersMove?.run {
            val piece = Grid[this.position] ?: return
            piece.add(MoveComponent(this.move))
        }
    }

    //TODO: outcome??

}