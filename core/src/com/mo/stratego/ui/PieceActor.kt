package com.mo.stratego.ui

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.model.system.RenderSystem
import com.mo.stratego.ui.input.PieceActorInputListener
import com.mo.stratego.util.Constants

/**
 * Scene2d actor for the [Piece] class. This class links an [Piece] instance
 * with an [Actor] instance to handle the user input. It does not draw on the
 * screen.
 */
class PieceActor(val piece: Piece, val engine: PooledEngine) : Actor() {

    init {
        // add listener to handle user input
        addListener(PieceActorInputListener(this, engine))
    }

    /**
     * This method updates the bounds to the position and dimensions of the [Piece],
     * so that the[PieceActorInputListener] receives the relevant user input, i.e touch events.
     * It does not draw the texture of the [Piece], this is done by the [RenderSystem].
     */
    override fun draw(batch: Batch?, parentAlpha: Float) {
        // get necessary components for drawing
        val position = piece.getComponent(PositionComponent::class.java)
        val texture = piece.getComponent(TextureComponent::class.java)

        //check if components are valid
        if (position == null || texture == null)
            return

        // update bounds, listener will only receive events in this area
        setBounds(position.position.x.toFloat(), position.position.y.toFloat(),
                  Constants.getPixelToUnit(
                          texture.region.regionWidth.toFloat()),
                  Constants.getPixelToUnit(
                          texture.region.regionHeight.toFloat()))
    }
}