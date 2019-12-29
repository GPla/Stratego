package com.mo.stratego

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mo.stratego.model.game.GameResult
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.sound.MusicType
import com.mo.stratego.model.sound.SoundProvider
import com.mo.stratego.ui.Atlas
import com.mo.stratego.ui.Screens
import com.mo.stratego.ui.control.BlinkLabel
import com.mo.stratego.ui.controller.HudController
import com.mo.stratego.util.Constants

/**
 * Displays the [GameResult] as well as other statistics to the user.
 *
 * @param result Game result
 */
// ADD result draw
class EndScreen(private val result: GameResult) : Screen {
    private val stage = Stage(StretchViewport(
            Constants.getUnitToPixel(GameMap.width.toFloat()),
            Constants.getUnitToPixel(GameMap.height.toFloat())))

    private val background: Sprite = when (result) {
        GameResult.WON -> Sprite(Atlas.endWon)
        GameResult.LOST -> Sprite(Atlas.endLost)
        GameResult.DRAW -> Sprite(Atlas.endDraw)
    }

    /**
     * Music handle of the music played through the [SoundProvider].
     */
    private var musicHandle: Int? = null

    init {
        val turns: String = HudController.lblTurn.text.toString()
        val time: String = HudController.lblTime.text.toString()

        val tblContent = Table(Atlas.uiSkinBig)
        with(tblContent) {
            setFillParent(true)


            val bLabel = BlinkLabel(1f, "Tap to continue!", Atlas.uiSkinBig)

            row().expandX()
            add(turns).pad(600f, 50f, 20f, 0f).align(Align.left)
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
        // start the music
        musicHandle = when (result) {
            GameResult.WON -> SoundProvider.playMusic(MusicType.VICTORY)
            else -> SoundProvider.playMusic(MusicType.DEFEAT)
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.1875f, 0.1875f, 0.1875f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)


        with(stage) {
            //setDebugInvisible(false)
            //isDebugAll = true

            // draw background
            batch.begin()
            background.draw(batch)
            batch.end()

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
        // stop the music
        musicHandle?.apply {
            SoundProvider.stopMusic(this)
        }
    }
}