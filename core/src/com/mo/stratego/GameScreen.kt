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
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mo.stratego.model.GameController
import com.mo.stratego.model.GameState
import com.mo.stratego.model.component.HighlightComponent
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.player.PlayerType
import com.mo.stratego.model.system.AttackSystem
import com.mo.stratego.model.system.MoveSystem
import com.mo.stratego.model.system.RenderSystem
import com.mo.stratego.model.system.WaitSystem
import com.mo.stratego.ui.FieldController
import com.mo.stratego.ui.HudController
import com.mo.stratego.ui.input.MapListener
import com.mo.stratego.util.Constants
import org.greenrobot.eventbus.EventBus

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
        val hudStage = Stage(StretchViewport(
                Constants.getUnitToPixel(GameMap.width.toFloat()),
                Constants.getUnitToPixel(GameMap.height.toFloat())))
        engine = PooledEngine()

        // add listeners to engine
        //listener is triggered if entity component is added / removed from the family
        // FieldController
        FieldController.init(objectStage, engine)
        var family = Family.one(PieceComponent::class.java,
                                HighlightComponent::class.java).get()
        engine.addEntityListener(family, FieldController)

        // HudController
        HudController.init(hudStage, engine)

        // Grid
        // Grid is updated if piece witWh position component is added / removed
        // or a move component is added/ removed
        family = Family.all(PieceComponent::class.java,
                            PositionComponent::class.java)
                .exclude(MoveComponent::class.java).get()
        engine.addEntityListener(family, Grid)

        GameMap.engine = engine
        GameController.init(engine, PlayerType.LOCAL)

        // add systems to engine
        engine.addSystem(RenderSystem(batch, camera))
        engine.addSystem(MoveSystem())
        engine.addSystem(WaitSystem())
        engine.addSystem(AttackSystem())

        testPieces()

        // handle user input with stage
        // objectStage receive events, if not handled they get passed to
        // the map stage
        Gdx.input.inputProcessor =
                InputMultiplexer(objectStage, hudStage, MapListener(engine))
    }


    override fun hide() {
    }

    override fun show() {
        camera.update()
        EventBus.getDefault().register(HudController)
    }

    override fun render(delta: Float) {
        Gdx.app.log("field", Grid.toString())
        //Gdx.app.log("state", GameController.state.toString())
        Grid.spawnMap.forEach { it ->
            Gdx.app.log("spawn", "${it.key}: ${it.value[0]}, ${it.value[1]}")
        }

        GameController.run()

        // clear screen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()

        // render map
        GameMap.render(camera)

        // update ashley engine if game is running
        if (GameController.state != GameState.GAME_OVER)
            engine.update(Gdx.graphics.deltaTime)

        // render stage
        FieldController.stage.apply {
            act(delta)
            draw()
        }

        HudController.stage.apply {
            setDebugInvisible(false)
            isDebugAll = true
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
        EventBus.getDefault().unregister(HudController)
    }

    fun testPieces() {
        /*var y = 6
        Rank.values().forEachIndexed { index, rank ->
            engine.addEntity(
                    Piece(rank, GameController.players[0],
                          GridPoint2(index % 10, y)))

            engine.addEntity(
                    Piece(rank, GameController.players[1],
                          GridPoint2(index % 10, y + 6)))

            if (index % 10 >= 9)
                ++y
        }*/


    }
}
