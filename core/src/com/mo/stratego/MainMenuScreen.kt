package com.mo.stratego

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mo.stratego.ui.BackButtonHandler
import com.mo.stratego.ui.controller.MenuController
import com.mo.stratego.ui.input.BackButtonListener
import com.mo.stratego.util.Constants

/**
 * Screen for the main menu.
 */
class MainMenuScreen : Screen, BackButtonHandler {

    init {
        // pixel dimensions of the screen will always be the same
        val stage = Stage(StretchViewport(Constants.SCREEN_WIDTH,
                                          Constants.SCREEN_HEIGHT))
        MenuController.init(stage)

        Gdx.input.inputProcessor = InputMultiplexer(stage, BackButtonListener())
    }


    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.1875f, 0.1875f, 0.1875f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)


        MenuController.stage.apply {
            batch.begin()
            MenuController.getBackground()?.draw(batch)
            batch.end()

            //setDebugInvisible(false)
            //isDebugAll = true
            act(delta)
            draw()
        }


    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }

    /**
     * Use [MenuController]'s handler.
     */
    override fun handleBackButton() {
        MenuController.handleBackButton()
    }
}