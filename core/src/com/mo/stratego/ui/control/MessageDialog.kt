package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

/**
 * Simple dialog with a message and a confirm button.
 * @param title Title of the dialog
 * @param msg Message to display
 * @param skin Skin
 */
class MessageDialog(title: String, val msg: String, skin: Skin) :
    Dialog(title, skin) {
    init {
        with(contentTable) {
            isMovable = false
            isModal = true

            val lblMsg = Label(msg, skin)
            lblMsg.setWrap(true)
            add(lblMsg)

            button("Ok")
        }
    }
}