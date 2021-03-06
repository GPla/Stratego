package com.mo.stratego.ui.control

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.mo.stratego.model.game.GameController
import com.mo.stratego.model.sound.SoundProvider
import com.mo.stratego.ui.controller.HudController
import com.mo.stratego.ui.provider.DialogProvider
import com.mo.stratego.util.AssetsManager

/**
 * Game menu dialog. Allows the player to change settings and
 * to surrender the current game.
 * @param skin Skin
 */
class GameMenuDialog(skin: Skin) : Dialog("Menu", skin) {

    private val btnDraw: TimerButton

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
            val lblMusic = Label("Sound", skin)
            val btnMusic = ToggleButton(SoundProvider::isTurnedOn,
                                        AssetsManager.uiSkinMed)

            // SURRENDER
            val btnSurrender = TextButton("Surrender", skin)
            // surrender on click
            btnSurrender.addListener(object : ClickListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                       pointer: Int, button: Int): Boolean {
                    DialogProvider.showConfirmationDialog(
                            "Are you sure you want to\nsurrender?",
                            { GameController.surrender() },
                            AssetsManager.uiSkinMed,
                            HudController.stage)
                    return true
                }
            })

            // DRAW
            btnDraw = TimerButton("Offer Draw",
                                  { GameController.offerDraw() }, 60f, skin)


            val innerTable = Table()
            with(innerTable) {
                add(lblMusic).padRight(20f)
                add(btnMusic).width(80f).height(50f)
            }

            add(innerTable).align(Align.left)
            row().padTop(30f)
            add(btnDraw).width(300f).colspan(2)
            row().padTop(30f)
            add(btnSurrender).width(300f).colspan(2)

            pad(30f, 80f, 20f, 80f)
        }
    }

    override fun reset() {
        btnDraw.reset()
    }
}