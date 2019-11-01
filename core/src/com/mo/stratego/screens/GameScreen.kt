package com.mo.stratego.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.mo.stratego.components.PositionComponent
import com.mo.stratego.components.TextureComponent
import com.mo.stratego.systems.RenderSystem

/**
 * Game screen
 *
 * game logic implemented using ECS (Entity - Component - System) design pattern
 */
class GameScreen : Screen {
    private var camera : OrthographicCamera = OrthographicCamera()
    private val map : TiledMap
    private val mapRenderer: OrthogonalTiledMapRenderer
    private val engine : Engine
    private val batch: SpriteBatch

    init {
        // load map
        map = TmxMapLoader().load("maps/map.tmx")
        // render map with unit scale 1/32, as one tile is 32x32
        mapRenderer = OrthogonalTiledMapRenderer(map, 1/32f)

        // set camera to map dimensions 12 x 21
        camera.setToOrtho(true, 12f, 21f)
        camera.position.set(Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f), 0f)

        batch = SpriteBatch()

        // create ashley engine
        engine = Engine()
        engine.addSystem(RenderSystem(batch, camera))

        engine.addEntity(Entity()
                .add(TextureComponent(TextureRegion(Texture("tilesets/female/Female 01-2.png"), 32, 32)))
                .add(PositionComponent(Vector2(0f, 0f))))

        engine.addEntity(Entity()
                .add(TextureComponent(TextureRegion(Texture("tilesets/female/Female 02-2.png"), 32, 32)))
                .add(PositionComponent(Vector2(0f, 0f))))
    }


    override fun hide() {
    }

    override fun show() {
        camera.update()
    }

    override fun render(delta: Float) {
        // clear screen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // update camera
        camera.update()

        //render map for this frame
        mapRenderer.setView(camera)
        mapRenderer.render()

        // update engine systems
        engine.update(Gdx.graphics.deltaTime)
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
