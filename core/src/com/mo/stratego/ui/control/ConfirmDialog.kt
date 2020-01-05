package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Skin

/**
 * Simple dialog to display a message with 2 options.
 * One option being 'cancel', which closes the dialog and the
 * other being 'confirm' which calls the callback function.
 *
 * @property callback Callback on confirm
 * @param msg Message to display
 * @param skin Skin
 */
class ConfirmDialog(msg: String, val callback: () -> Unit, skin: Skin) :
    Dialog("Confirmation", skin) {
    init {
        isModal = true
        isMovable = false
        isResizable = false

        with(contentTable) {
            add(msg)
            row()
            button("Confirm", 1)
            button("Cancel", 0)

            pad(20f, 30f, 20f, 30f)
        }
    }

    override fun result(`object`: Any?) {
        when (`object` as Int) {
            1 -> callback()
        }
    }
}