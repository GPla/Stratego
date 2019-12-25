package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

/**
 *  Label with blinking text.
 *
 * @param interval Blink interval in seconds
 * @param text Text
 * @param skin Skin
 */
class BlinkLabel(private val interval: Float, text: String, skin: Skin) :
    Label(text, skin) {

    private var elapsed: Float = 0f

    override fun act(delta: Float) {
        super.act(delta)

        elapsed += delta

        if (elapsed >= interval) {
            elapsed = 0f
            isVisible = !isVisible
        }

    }
}