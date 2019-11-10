package com.mo.stratego.ui.input

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.component.HighlightComponent
import com.mo.stratego.model.component.MoveComponent

/**
 * Input listener that handles the user input for highlights.
 */
class HighlightInputListener(private val entity: Entity,
                             private val engine: Engine) : InputListener() {

    val highMapper = ComponentMapper.getFor(HighlightComponent::class.java)

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                           button: Int): Boolean {
        moveEntity()
        return true
    }

    //TODO: remove, see architecture
    //TODO: put move into owner of piece
    private fun moveEntity() {
        val highlight = highMapper.get(entity) ?: return
        if (highlight.move == null)
            return

        highlight.piece.add(MoveComponent(highlight.move))
        HighlightType.deleteHighlight(engine)
    }
}