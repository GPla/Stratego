package com.mo.stratego.ui.input

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.ui.PieceActor

/**
 * Input listener that handles the user input for the [PieceActor] class.
 */
class PieceActorInputListener(val actor: PieceActor, val engine: Engine) :
    InputListener() {
    companion object {
        val entity: Entity = Entity()

        init {
            entity.add(PositionComponent(GridPoint2(10, 10), -1))
                    .add(TextureComponent(
                            TextureRegion(Texture("pics/highlight_square.png"),
                                          64, 64)))
        }

        var added: Boolean = false
    }

    init {
        if (!added) {
            added = true
            engine.addEntity(entity)
        }
    }

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                           button: Int): Boolean {
        Gdx.app.log("dtag", "touched")
        val pos = entity.getComponent(PositionComponent::class.java)

        pos.position.x = actor.x.toInt()
        pos.position.y = actor.y.toInt()

        return true
    }
}