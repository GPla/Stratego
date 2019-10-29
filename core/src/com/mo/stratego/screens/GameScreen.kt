package com.mo.stratego.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

/**
 * Screen that renders the game
 */
class GameScreen : Screen {
    private var camera : OrthographicCamera = OrthographicCamera()
    private lateinit var map : TiledMap
    private lateinit var mapRenderer: TiledMapRenderer

    init {
        // load map
        map = TmxMapLoader().load("maps/map.tmx")
        // render map with unit scale 1/32, as one tile is 32x32
        mapRenderer = OrthogonalTiledMapRenderer(map, 1/32f)

        // set camera to map dimensions
        // TODO: adjust map dimensions
        camera.setToOrtho(false, 12f, 21f)
    }


    override fun hide() {
    }

    override fun show() {
        camera.update()
    }

    override fun render(delta: Float) {
        // clear screen
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // update camera
        camera.update()

        //render map for this frame
        mapRenderer.setView(camera)
        mapRenderer.render()
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
