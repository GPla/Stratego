package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

/**
 *  Label that displays a load animation. The animation consists of the
 *  repetition of the defined symbol. Each frame adds another instance of the
 *  symbol and lasts for the defined deltaTime.
 *  One cycle is deltaTime * maxCount long.
 *
 * @property deltaTime Time between frames
 * @property symbol Symbol to display
 * @property maxCount Maximum number of repetitions
 * @property showProgress Function if true show, otherwise hide animation only
 * @param skin Skin
 */
class LoadLabel(val defaultText: String, val deltaTime: Float, val symbol: Char,
                val showProgress: () -> Boolean, skin: Skin,
                var maxCount: Int = 3) :
    Label(defaultText, skin) {

    private var tick: Int = 0

    /**
     * Time since last frame.
     */
    private var elapsedTime: Float = 0f

    override fun act(delta: Float) {
        if (!showProgress()) {
            setText(defaultText)
            return
        }

        elapsedTime += delta

        if (elapsedTime >= deltaTime) {
            elapsedTime = 0f
            if (++tick > maxCount)
                tick = 0

            setText("$defaultText" + " $symbol".repeat(tick))
        }
    }


}