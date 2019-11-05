package com.mo.stratego.ui.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mo.stratego.ui.PieceActor

/**
 * Input listener that handles the user input for the [PieceActor] class.
 */
class PieceActorInputListener(val actor : PieceActor) : InputListener() {
    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        Gdx.app.log("dtag", "touched!")

        return  true
    }
}