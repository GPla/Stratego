package com.mo.stratego

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mo.stratego.model.Atlas
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.ui.Screens
import com.mo.stratego.ui.control.BlinkLabel
import com.mo.stratego.ui.controller.HudController
import com.mo.stratego.util.Constants

// TODO
class EndScreen(private val playerId: PlayerId) : Screen {
    private val stage = Stage(StretchViewport(
            Constants.getUnitToPixel(GameMap.width.toFloat()),
            Constants.getUnitToPixel(GameMap.height.toFloat())))

    init {
        val turns: String = HudController.lblTurn.text.toString()
        val time: String = HudController.lblTime.text.toString()

        val tblContent = Table(Atlas.uiSkinBig)
        with(tblContent) {
            setFillParent(true)

            val style = Label.LabelStyle(Atlas.font100, Color.WHITE)
            val resultLabel = Label(if (playerId == PlayerId.PLAYER1) "You Won!"
                                    else "You Lost!", style)
            
            val bLabel = BlinkLabel(1f, "Tap to continue!", Atlas.uiSkinBig)

            add(resultLabel).expand()
            row()
            add(turns).pad(20f, 50f, 20f, 0f).align(Align.left)
            row()
            add(time).pad(20f, 50f, 20f, 0f).align(Align.left)
            row().padTop(300f)
            add(bLabel).align(Align.center).padBottom(100f)
        }

        stage.addActor(tblContent)
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.1875f, 0.1875f, 0.1875f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)


        with(stage) {
            //setDebugInvisible(false)
            //isDebugAll = true
            act(delta)
            draw()
        }

        // on touch return to main menu
        if (Gdx.input.justTouched())
            StrategoGame.switchScreen(Screens.MAINMENU)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }
}