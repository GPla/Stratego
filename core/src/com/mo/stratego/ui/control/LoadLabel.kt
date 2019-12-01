package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

/**
 *  Label that displays a load animation.
 *
 * @property deltaTime Time between frames
 * @property symbol Symbol to display
 * @property maxCount Maximum number of symbol
 * @property showLabel Function if true show, otherwise hide
 * @param skin Skin
 */
class LoadLabel(val deltaTime: Float, val symbol: Char,
                val showLabel: () -> Boolean, skin: Skin,
                var maxCount: Int = 3) :
    Label("$symbol ", skin) {

    /**
     * Time since last frame.
     */
    private var elapsedTime: Float = 0f

    override fun act(delta: Float) {
        isVisible = showLabel()
        if (!isVisible) {
            setText("")
            return
        }

        elapsedTime += delta

        if (elapsedTime >= deltaTime) {
            elapsedTime = 0f
            if (text.count() / 2f >= maxCount)
                setText("$symbol ")
            else
                setText("$text$symbol ")
        }
    }


}