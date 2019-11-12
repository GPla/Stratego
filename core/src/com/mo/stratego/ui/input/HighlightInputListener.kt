package com.mo.stratego.ui.input

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.Move
import com.mo.stratego.model.component.HighlightComponent
import com.mo.stratego.model.component.PositionComponent

/**
 * Input listener that handles the user input for highlights.
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
        moveEntity()
        return true
    }

    //TODO: remove, see architecture
    //TODO: put move into owner of piece
    private fun moveEntity() {
        val highlight = highMapper.get(entity) ?: return
        val position = posMapper.get(highlight.piece)?.position

        if (highlight.move == null)
            return

        // set move for player
        //highlight.piece.add(MoveComponent(highlight.move))
        position?.run {
            highlight.piece.owner.move = Move(this, highlight.move)
        }

        // delete highligths
        HighlightType.deleteHighlight(engine)
    }
}