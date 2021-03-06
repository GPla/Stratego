package com.mo.stratego.ui.input

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.Move
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.game.GameController
import com.mo.stratego.model.game.GameState
import com.mo.stratego.model.map.Grid

/**
 * Input listener that handles the user input for the [Piece] class.
 */
class PieceInputListener(private val piece: Piece,
                         private val engine: Engine) :
    ActorGestureListener() {

    private val posMapper =
            ComponentMapper.getFor(PositionComponent::class.java)

    /**
     * Handles tap events on the [Piece].
     */
    override fun tap(event: InputEvent?, x: Float, y: Float, count: Int,
                     button: Int) {

        // delete old highlights
        HighlightType.deleteHighlight(engine,
                                      HighlightType.SQUARE,
                                      HighlightType.CIRCLE)

        // react depending on game state
        when (GameController.state) {
            GameState.PREPARATION_PLAYER_1,
            GameState.PREPARATION_PLAYER_2 -> {
                when (count) {
                    1 -> createPlacementHighlight()
                    2 -> returnToDefaultPosition()
                }
            }
            GameState.TURN_PLAYER_1,
            GameState.TURN_PLAYER_2        -> {
                HighlightType.deleteHighlight(engine,
                                              HighlightType.SQUARE,
                                              HighlightType.CIRCLE)
                createMoveHighlight()
            }
        }
    }

    /**
     * Creates the highlights for the possible [Move]s of the [Piece].
     */
    private fun createMoveHighlight() {
        val standpoint = posMapper.get(piece)?.position ?: return
        val allowedMoves = Grid.getAllowedMoves(piece)


        // selected highlight square
        val square =
                engine.createEntity().add(PositionComponent(standpoint, -1))
        engine.addEntity(square)
        HighlightType.createHighlight(square, piece, HighlightType.SQUARE,
                                      null)

        // highlight circles
        for (move in allowedMoves) {
            // position of highlight
            val point = GridPoint2(standpoint).add(move)
            val circle = engine.createEntity().add(PositionComponent(point, 1))
            engine.addEntity(circle)
            HighlightType.createHighlight(circle, piece, HighlightType.CIRCLE,
                                          move)
        }
    }

    /**
     * Creates highlights for possible placements on the [Grid].
     */
    private fun createPlacementHighlight() {
        val cells = Grid.getFreeCellsInPlayerZone(piece.owner.id)
        val position = posMapper.get(piece)

        // create highlight square at position of selected piece
        val square = engine.createEntity()
                .add(PositionComponent(position.position.cpy(), -1))
        engine.addEntity(square)
        HighlightType.createHighlight(square, piece, HighlightType.SQUARE,
                                      null)

        // create highlight circle for empty fields on the grid
        for (cell in cells) {
            val circle = engine.createEntity().add(PositionComponent(cell, 1))
            engine.addEntity(circle)
            HighlightType.createHighlight(circle, piece, HighlightType.CIRCLE,
                                          null)

        }

    }

    /**
     * Returns [Piece] to default position.
     */
    private fun returnToDefaultPosition() {
        piece.returnToDefaultPosition()
    }
}