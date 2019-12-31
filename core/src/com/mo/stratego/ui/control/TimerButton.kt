package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mo.stratego.util.formatNoDecimal

/**
 * Button that disables after being pressed for the defined timeout. The action
 * is run when the button is pressed.
 * @param text Text
 * @param action Action
 * @param timeout Timeout in seconds
 * @param skin Skin
 */
class TimerButton(text: String, action: () -> Unit, timeout: Float,
                  skin: Skin) :
    TextButton(text, skin) {

    private val text_ = text

    init {
        addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                   pointer: Int, button: Int): Boolean {
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float,
                                 pointer: Int, button: Int) {
                // run user action
                action()
                // start timer for timeout
                timer = timeout
            }
        })
    }

    // timeout timer
    private var timer: Float = 0f


    override fun act(delta: Float) {
        super.act(delta)
        setText(text_)

        if (timer > 0f) {
            // update timer
            timer -= delta

            // update label with time
            setText("$text_ (${timer.formatNoDecimal()}s)")
        }


        // disable button while timer is running
        touchable = if (timer > 0f) Touchable.disabled else Touchable.enabled


    }

    /**
     * Resets the button.
     */
    override fun reset() {
        timer = 0f
    }
}