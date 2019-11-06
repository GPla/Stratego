package com.mo.stratego

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mo.stratego.model.GameController
import com.mo.stratego.model.Piece
import com.mo.stratego.model.Rank
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.player.PlayerLocal
import com.mo.stratego.model.player.PlayerProxy
import com.mo.stratego.model.system.RenderSystem
import com.mo.stratego.ui.FieldController
import com.mo.stratego.util.Constants

/**
 * Game screen
 *
 * game logic implemented using ECS (Entity - Component - System) design pattern
 */

//TODO: fix texture loading, use atlas for all pics
class GameScreen : Screen {
    private var camera: OrthographicCamera = OrthographicCamera()
    private val engine: Engine
    private val batch: SpriteBatch
    private val stage: Stage

    //TODO: fix comments
    init {
        //set camera to map dimensions, to show full map
        camera.setToOrtho(false, GameMap.width.toFloat(), GameMap.height.toFloat())
        camera.position.set(Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f), 0f)

        batch = SpriteBatch()

        stage = Stage(StretchViewport(camera.viewportWidth, camera.viewportHeight))

        engine = Engine()

        // add listeners to engine
        //trigger listener if piece component is added / removed
        var family = Family.all(PieceComponent::class.java).get()
        engine.addEntityListener(family, FieldController.init(stage, engine))
        family = Family.all(PieceComponent::class.java).one(PositionComponent::class.java,
                            MoveComponent::class.java).get()
        engine.addEntityListener(family, Grid)

        GameMap.engine = engine
        GameController.init(engine, PlayerLocal(), PlayerProxy())

        // add systems to engine
        engine.addSystem(RenderSystem(batch, camera))

        engine.addEntity(Piece(Rank.MARSHAL, GameController.players[0])
                .add(TextureComponent(TextureRegion(Texture("pics/10_marshal_1.png"), 64, 64)))
                .add(PositionComponent(Vector2(4f, 7f))))
        engine.addEntity(Piece(Rank.SCOUT, GameController.players[0])
                .add(TextureComponent(TextureRegion(Texture("pics/9_general_1.png"), 64, 64)))
                .add(PositionComponent(Vector2(8f, 12f))))

        // handle user input with stage
        Gdx.input.inputProcessor = stage
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

        camera.update()

        // render map
        GameMap.render(camera)

        // update ashley engine
        engine.update(Gdx.graphics.deltaTime)

        // render stage
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
