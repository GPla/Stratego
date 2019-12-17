package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

/**
 * Label that shows a text for the set time.
 * @param time Time to show the text.
 */
class TimedLabel(text: String, val time: Float, skin: Skin) :
    Label(text, skin) {

    private var elapsedTime: Float = 0f

    override fun setText(newText: CharSequence?) {
        super.setText(newText)
        elapsedTime = 0f
    }

    override fun act(delta: Float) {
        super.act(delta)

        elapsedTime += delta
        if (elapsedTime >= time)
            setText("")
    }
}