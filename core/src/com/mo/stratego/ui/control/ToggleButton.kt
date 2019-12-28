package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import kotlin.reflect.KMutableProperty0

/**
 * Toggle button with state bound to given property. Property will be toggled
 * if button gets pressed.
 *
 * @property prop Property to bind
 * @param skin Skin
 */
class ToggleButton(val prop: KMutableProperty0<Boolean>, skin: Skin) :
    TextButton("On", skin) {

    init {
        // toggle property value
        addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                   pointer: Int, button: Int): Boolean {
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float,
                                 pointer: Int, button: Int) {
                // toggle
                prop.set(!prop.get())
            }
        })
    }

    override fun act(delta: Float) {
        super.act(delta)
        setText(if (prop.get()) "On" else "Off")
    }


}