package com.mo.stratego.ui.input

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.map.Grid
import com.mo.stratego.ui.PieceActor

/**
 * Input listener that handles the user input for the [PieceActor] class.
 */
class PieceActorInputListener(val actor: PieceActor, val engine: PooledEngine) :
    InputListener() {

    private val posMapper =
            ComponentMapper.getFor(PositionComponent::class.java)

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
        val standpoint = posMapper.get(actor.piece)?.position ?: return
        val allowedMoves = Grid.getAllowedMoves(actor.piece)


        // selected highlight square
        val square =
                engine.createEntity().add(PositionComponent(standpoint, -1))
        engine.addEntity(square)
        HighlightType.createHightlight(square, HighlightType.SQUARE)

        // highlight circle
        for (move in allowedMoves) {
            val point = GridPoint2(standpoint).add(move)
            val circle = engine.createEntity().add(PositionComponent(point, 1))
            engine.addEntity(circle)
            HighlightType.createHightlight(circle, HighlightType.CIRCLE)
        }
    }


}