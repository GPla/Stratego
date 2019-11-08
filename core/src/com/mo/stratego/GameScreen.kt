package com.mo.stratego

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
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
        camera.setToOrtho(false, GameMap.width.toFloat(),
                          GameMap.height.toFloat())
        camera.position.set(
                Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f),
                0f)

        batch = SpriteBatch()

        stage = Stage(StretchViewport(camera.viewportWidth,
                                      camera.viewportHeight))

        engine = PooledEngine()

        // add listeners to engine
        //listener is triggered if entity component is added / removed from the family
        // FieldController
        var family = Family.all(PieceComponent::class.java).get()
        engine.addEntityListener(family, FieldController.init(stage, engine))

        // Grid
        family = Family.all(PieceComponent::class.java,
                            PositionComponent::class.java).get()
        engine.addEntityListener(family, Grid)
        family = Family.all(PieceComponent::class.java,
                            PositionComponent::class.java,
                            MoveComponent::class.java).get()
        engine.addEntityListener(family, Grid)

        GameMap.engine = engine
        GameController.init(engine, PlayerLocal(1), PlayerProxy(2))

        // add systems to engine
        engine.addSystem(RenderSystem(batch, camera))


        val piece = Piece(Rank.MARSHAL, GameController.players[0]).add(
                TextureComponent(
                        TextureRegion(Texture("pics/10_marshal_1.png"), 64,
                                      64)))
                .add(PositionComponent(GridPoint2(3, 7)))

        engine.addEntity(piece)

        engine.addEntity(
                Piece(Rank.SCOUT, GameController.players[1])
                        .add(TextureComponent(TextureRegion(
                                Texture("pics/2_scout_2.png"), 64, 64)))
                        .add(PositionComponent(GridPoint2(7, 12))))

        engine.addEntity(
                Piece(Rank.SCOUT, GameController.players[1])
                        .add(TextureComponent(TextureRegion(
                                Texture("pics/2_scout_2.png"), 64, 64)))
                        .add(PositionComponent(GridPoint2(8, 12))))

        // handle user input with stage
        Gdx.input.inputProcessor = stage

        Gdx.app.log("dtag", Grid.toString())

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
