package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

/**
 * Dialog with one Button, a Message and a result action.
 *
 *  @param title Title of the dialog
 * @param text Message of the dialog
 * @param btnText Button text
 * @param skin Skin
 */
class OneButtonDialog(title: String, text: String, btnText: String,
                      val action: () -> Unit, skin: Skin) :
    Dialog(title, skin) {
    init {
        contentTable.pad(20f)
        button(TextButton(btnText, skin))
        text(text)
        isModal = true
        isMovable = false
        isResizable = false
    }

    override fun result(`object`: Any?) {
        action()
    }
}