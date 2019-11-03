package com.mo.stratego.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mo.stratego.GameScreen
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent

/**
 * Actor that draws the linked [Piece] on the screen and handles the user input
 */
class PieceActor(val piece: Piece) : Actor() {


    override fun draw(batch: Batch?, parentAlpha: Float) {
        //check if drawing is possible
        if (batch == null)
            return

        // get necessary components for drawing
        val position = piece.getComponent(PositionComponent::class.java)
        val texture = piece.getComponent(TextureComponent::class.java)

        //check if components are valid
        if (position == null || texture == null)
            return

        // draw piece
        batch.draw(texture.region,
                position.position.x, position.position.y,
                texture.origin.x, texture.origin.y,
                texture.region.regionHeight.toFloat(),
                texture.region.regionHeight.toFloat(),
                GameScreen.getPixelToUnit(texture.scale.x),
                GameScreen.getPixelToUnit(texture.scale.y),
                texture.rotation
        )
    }
}