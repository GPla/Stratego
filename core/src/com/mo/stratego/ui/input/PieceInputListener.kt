package com.mo.stratego.ui.input

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.map.Grid

/**
 * Input listener that handles the user input for the [Piece] class.
 */
class PieceInputListener(private val piece: Piece,
                         private val engine: Engine) : InputListener() {

    private val posMapper =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val pieceMapper = ComponentMapper.getFor(PieceComponent::class.java)

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                           button: Int): Boolean {

        HighlightType.deleteHighlight(engine)
        createHighlight()

        return true
    }

    /**
     * Creates the highlight for the possible moves of the entity.
     */
    private fun createHighlight() {
        val standpoint = posMapper.get(piece)?.position ?: return
        val allowedMoves = Grid.getAllowedMoves(piece)


        // selected highlight square
        val square =
                engine.createEntity().add(PositionComponent(standpoint, -1))
        engine.addEntity(square)
        HighlightType.createHightlight(square, piece, HighlightType.SQUARE,
                                       null)

        // highlight circle
        for (move in allowedMoves) {
            val point = GridPoint2(standpoint).add(move)
            val circle = engine.createEntity().add(PositionComponent(point, 1))
            engine.addEntity(circle)
            HighlightType.createHightlight(circle, piece, HighlightType.CIRCLE,
                                           move)
        }
    }


}