package com.mo.stratego.ui.control

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.mo.stratego.StrategoGame
import com.mo.stratego.ui.Screens

/**
 * Game menu dialog. Allows the player to change settings and
 * to surrender the current game.
 * @param skin Skin
 */
class GameMenuDialog(skin: Skin) : Dialog("Menu", skin) {
    init {
        isModal = true
        isMovable = false
        isResizable = false

        with(titleTable) {
            val btnClose = ImageButton(TextureRegionDrawable(
                    Texture(Gdx.files.internal("ui/x.png"))))

            btnClose.addListener(object : ClickListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                       pointer: Int, button: Int): Boolean {

                    hide()
                    return true
                }
            })
            add(btnClose).size(50f).padRight(-8f)
            sizeBy(1.2f)
        }

        with(contentTable) {
            val lblMusic = Label("Music", skin)
            val btnMusic = Button(skin)

            add(lblMusic).padRight(5f)
            add(btnMusic)
            row()
            button("Return to Main Menu", 0)

            align(Align.left)
            pad(20f)
        }
    }


    override fun result(`object`: Any?) {
        when (`object` as Int) {
            0 -> StrategoGame.switchScreen(Screens.MAINMENU)
        }
    }
}