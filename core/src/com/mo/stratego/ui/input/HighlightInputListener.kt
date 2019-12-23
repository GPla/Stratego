package com.mo.stratego.ui.input

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mo.stratego.model.*
import com.mo.stratego.model.component.HighlightComponent
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.system.MoveSystem

/**
 * Input listener that handles the user input for [HighlightComponent]s.
 */
class HighlightInputListener(private val entity: Entity,
                             private val engine: Engine) : InputListener() {

    // Component mapper
    private val highMapper =
            ComponentMapper.getFor(HighlightComponent::class.java)
    private val posMapper =
            ComponentMapper.getFor(PositionComponent::class.java)

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                           button: Int): Boolean {

        // react depending on game state
        when (GameController.state) {
            GameState.PREPARATION_PLAYER_1,
            GameState.PREPARATION_PLAYER_2 -> placeEntity()
            GameState.TURN_PLAYER_1,
            GameState.TURN_PLAYER_2        -> moveEntity()
        }

        // delete highlights
        HighlightType.deleteHighlight(engine,
                                      HighlightType.SQUARE,
                                      HighlightType.CIRCLE)

        return true
    }


    /**
     * Moves the [Piece] to the position of the [HighlightComponent].
     * The [Move] is put into the owner of the [Piece] and processed
     * by the [MoveSystem].
     */
    private fun moveEntity() {
        val highlight = highMapper.get(entity) ?: return
        // position of the piece
        val position = posMapper.get(highlight.piece)?.position

        if (highlight.move == null)
            return

        // set move for player
        position?.run {
            highlight.piece.owner.move = Move(this, highlight.move)
        }

    }

    /**
     * Places the [Piece] on Grid. The position of the [Piece]
     * is updated to the position of the [HighlightComponent].
     */
    private fun placeEntity() {
        val highlight = highMapper.get(entity) ?: return
        // position of the piece
        val position = posMapper.get(entity)?.position ?: return

        // place piece on grid
        highlight.piece.add(MoveComponent(position, MoveType.ABSOLUTE))
    }
}