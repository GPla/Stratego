package com.mo.stratego

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mo.stratego.model.GameController
import com.mo.stratego.model.Piece
import com.mo.stratego.model.Rank
import com.mo.stratego.model.component.HighlightComponent
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.player.PlayerType
import com.mo.stratego.model.system.MoveSystem
import com.mo.stratego.model.system.RenderSystem
import com.mo.stratego.ui.FieldController
import com.mo.stratego.ui.input.MapListener

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

    //TODO: fix comments and cleanup
    init {
        //set camera to map dimensions, to show full map
        camera.setToOrtho(false, GameMap.width.toFloat(),
                          GameMap.height.toFloat())
        camera.position.set(
                Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f),
                0f)

        batch = SpriteBatch()

        val objectStage = Stage(StretchViewport(camera.viewportWidth,
                                                camera.viewportHeight))
        engine = PooledEngine()

        // add listeners to engine
        //listener is triggered if entity component is added / removed from the family
        // FieldController
        FieldController.init(objectStage, engine)
        var family = Family.one(PieceComponent::class.java,
                                HighlightComponent::class.java).get()
        engine.addEntityListener(family, FieldController)

        // Grid
        family = Family.all(PieceComponent::class.java,
                            PositionComponent::class.java).get()
        engine.addEntityListener(family, Grid)
        family = Family.all(PieceComponent::class.java,
                            PositionComponent::class.java,
                            MoveComponent::class.java).get()
        engine.addEntityListener(family, Grid)

        GameMap.engine = engine
        GameController.init(engine, PlayerType.LOCAL, PlayerType.PROXY)

        // add systems to engine
        engine.addSystem(RenderSystem(batch, camera))
        engine.addSystem(MoveSystem())


        val piece = Piece(Rank.MARSHAL, GameController.players[0])
                .add(PositionComponent(GridPoint2(3, 7)))

        engine.addEntity(piece)

        engine.addEntity(
                Piece(Rank.GENERAL, GameController.players[1])
                        .add(PositionComponent(GridPoint2(7, 12))))

        engine.addEntity(
                Piece(Rank.SCOUT, GameController.players[1])
                        .add(PositionComponent(GridPoint2(8, 12))))


        // handle user input with stage
        // objectStage receive events, if not handled they get passed to
        // the map stage
        Gdx.input.inputProcessor =
                InputMultiplexer(objectStage, MapListener(engine))
    }


    override fun hide() {
    }

    override fun show() {
        camera.update()
    }

    override fun render(delta: Float) {
        Gdx.app.log("dtag", Grid.toString())

        // clear screen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()

        // render map
        GameMap.render(camera)

        // update ashley engine
        engine.update(Gdx.graphics.deltaTime)

        // render stages
        FieldController.stage.run {
            batch.projectionMatrix = camera.combined
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
