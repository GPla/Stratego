package com.mo.stratego.ui.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.mo.stratego.StrategoGame
import com.mo.stratego.ui.BackButtonHandler

/**
 *  Listener for androids back button.
 */
class BackButtonListener : InputProcessor {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int,
                         button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int,
                              pointer: Int): Boolean {
        return false
    }

    /**
     * Notifies screen about android's back button, if [BackButtonHandler]
     * is implemented.
     *
     * @param keycode Keycode
     * @return Whether or not the back button was handled.
     */
    override fun keyDown(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.BACK -> {
                StrategoGame.screen.let {
                    if (it is BackButtonHandler) {
                        it.handleBackButton()
                        return@let true
                    }
                    false
                }
            }
            else -> false
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int,
                           button: Int): Boolean {
        return false
    }
}