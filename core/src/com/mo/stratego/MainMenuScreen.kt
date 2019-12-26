package com.mo.stratego

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.ui.controller.MenuController
import com.mo.stratego.util.Constants

/**
 * Screen for the main menu.
 */
class MainMenuScreen : Screen {

    init {
        val stage = Stage(StretchViewport(
                Constants.getUnitToPixel(GameMap.width.toFloat()),
                Constants.getUnitToPixel(GameMap.height.toFloat())))
        MenuController.init(stage)

        Gdx.input.inputProcessor = stage
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

}