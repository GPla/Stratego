package com.mo.stratego.ui.input

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
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
        Gdx.app.log("dtag", "touched")

        createHighlight()

        return true
    }

    private fun createHighlight() {
        val standpoint = posMapper.get(actor.piece)?.position ?: return
        val allowedMoves = Grid.getAllowedMoves(actor.piece)

        //FIXME: texture management
        val circleTexture = TextureComponent(
                TextureRegion(Texture("pics/highlight_circle.png")))
        val squareTexture = TextureComponent(
                TextureRegion(Texture("pics/highlight_square.png")))

        // selected highlight square
        engine.addEntity(engine.createEntity()
                                 .add(squareTexture)
                                 .add(PositionComponent(standpoint, -1)))

        Gdx.app.log("dtag", "size: ${allowedMoves.size}")
        // move highlight circle
        for (move in allowedMoves) {
            val point = GridPoint2(standpoint).add(move)
            engine.addEntity(engine.createEntity()
                                     .add(circleTexture)
                                     .add(PositionComponent(point, 1)))
        }
    }

}