package com.mo.stratego.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mo.stratego.GameScreen
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.ui.input.PieceActorInputListener
import com.mo.stratego.util.Scale

/**
 * Actor that draws the linked [Piece] on the screen and handles the user input
 */
class PieceActor(val piece: Piece, val engine : Engine) : Actor() {

    init {
        addListener(PieceActorInputListener(this, engine))
    }

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

        //update actor
        update(position, texture)

        // draw piece
        batch.draw(texture.region,
                    x, y,
                    originX, originY,
                    width, height,
                    scaleX, scaleY,
                    rotation)

    }

    /**
     * Copies the data from the [PositionComponent] and [TextureComponent] into this class.
     */
    //FIXME: use eventbus instead of polling?
    fun update(pos : PositionComponent, tex : TextureComponent){
        setPosition(pos.position.x, pos.position.y)
        setOrigin(tex.origin.x, tex.origin.y)
        setSize(tex.region.regionWidth.toFloat(), tex.region.regionHeight.toFloat())
        setScale(Scale.getPixelToUnit(tex.scale.x), Scale.getPixelToUnit(tex.scale.y))
        rotation = tex.rotation
        setBounds(pos.position.x, pos.position.y,
                tex.region.regionWidth.toFloat(), tex.region.regionHeight.toFloat())
    }



}