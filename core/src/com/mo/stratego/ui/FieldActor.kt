package com.mo.stratego.ui

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.model.system.RenderSystem
import com.mo.stratego.util.Constants

/**
 * Scene2d actor for the [Entity] class. This class links an [Entity] instance
 * with an [Actor] instance to handle the user input. It does not draw on the
 * screen.
 */
class FieldActor(val entity: Entity) : Actor() {

    // component mapper
    private val posMapper =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val textureMapper =
            ComponentMapper.getFor(TextureComponent::class.java)

    /**
     * This method updates the bounds to the position and dimensions of the [Entity],
     * so that the [InputListener] receives the relevant user input, i.e touch events.
     * It does not draw the texture of the [Entity], this is done by the [RenderSystem].
     */
    override fun draw(batch: Batch?, parentAlpha: Float) {
        // get necessary components for drawing
        val position = posMapper.get(entity)?.position ?: return
        val texture = textureMapper.get(entity) ?: return

        // update bounds, listener will only receive events in this area
        setBounds(position.x.toFloat(), position.y.toFloat(),
                  Constants.getPixelToUnit(
                          texture.region.regionWidth.toFloat()),
                  Constants.getPixelToUnit(
                          texture.region.regionHeight.toFloat()))

        if (entity is Piece)
            touchable = if (entity.isTouchable()) Touchable.enabled
            else Touchable.disabled
    }
}