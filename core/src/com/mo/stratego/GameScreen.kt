package com.mo.stratego

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mo.stratego.model.Piece
import com.mo.stratego.model.Rank
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.ui.FieldController

/**
 * Game screen
 *
 * game logic implemented using ECS (Entity - Component - System) design pattern
 */
class GameScreen : Screen {
    private var camera: OrthographicCamera = OrthographicCamera()
    private val map: TiledMap
    private val mapRenderer: OrthogonalTiledMapRenderer
    private val engine: Engine
    //private val batch: SpriteBatch
    private val stage: Stage

    init {
        // load map
        map = TmxMapLoader().load("maps/map.tmx")
        // render map with unit scale 1/32, as one tile is 32x32
        mapRenderer = OrthogonalTiledMapRenderer(map, 1 / 32f)

        // set camera to map dimensions 12 x 21
        camera.setToOrtho(false, 12f, 21f)
        camera.position.set(Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f), 0f)


        //batch = SpriteBatch()

        //stage for ui
        stage = Stage(StretchViewport(camera.viewportWidth, camera.viewportHeight))

        // create ashley engine
        engine = Engine()

        //trigger listener if piece component is added / removed
        val family = Family.all(PieceComponent::class.java).get()
        engine.addEntityListener(family, FieldController.init(this, stage))
        //engine.addSystem(RenderSystem(batch, camera))

        engine.addEntity(Piece(Rank.MARSHAL)
                .add(TextureComponent(TextureRegion(Texture("tilesets/female/Female 01-2.png"), 32, 32)))
                .add(PositionComponent(Vector2(4f, 7f))))
        engine.addEntity(Piece(Rank.SCOUT)
                .add(TextureComponent(TextureRegion(Texture("tilesets/female/Female 02-2.png"), 32, 32)))
                .add(PositionComponent(Vector2(8f, 14f))))
        engine.addEntity(Piece(Rank.SCOUT)
                .add(TextureComponent(TextureRegion(Texture("tilesets/female/Female 03-2.png"), 32, 32)))
                .add(PositionComponent(Vector2(7f, 14f))))
        engine.addEntity(Piece(Rank.SCOUT)
                .add(TextureComponent(TextureRegion(Texture("tilesets/female/Female 04-2.png"), 32, 32)))
                .add(PositionComponent(Vector2(9f, 14f))))
        engine.addEntity(Piece(Rank.SCOUT)
                .add(TextureComponent(TextureRegion(Texture("tilesets/female/Female 05-2.png"), 32, 32)))
                .add(PositionComponent(Vector2(8f, 15f))))
        engine.addEntity(Piece(Rank.SCOUT)
                .add(TextureComponent(TextureRegion(Texture("tilesets/female/Female 06-2.png"), 32, 32)))
                .add(PositionComponent(Vector2(8f, 13f))))
    }

    companion object {
        // scale: 32 pixels equals 1 game unit
        val unitscale: Float = 1 / 32f

        fun getPixelToUnit(pixel: Float) = pixel * unitscale
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

        stage.batch.projectionMatrix = camera.combined

        stage.act(delta)
        stage.draw()
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
