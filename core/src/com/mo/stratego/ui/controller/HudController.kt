package com.mo.stratego.ui.controller

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.Atlas
import com.mo.stratego.model.GameController
import com.mo.stratego.model.GameState
import com.mo.stratego.model.Rank
import com.mo.stratego.model.communication.StateEvent
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.ui.control.CounterLabel
import com.mo.stratego.ui.control.ReadyButton
import com.mo.stratego.ui.control.TimerLabel
import com.mo.stratego.ui.provider.DialogProvider
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Controller for ui elements while the game is running.
 */
object HudController {

    lateinit var stage: Stage
    private lateinit var engine: PooledEngine

    // actors
    private val topBar = Button(Atlas.uiSkin)
    private val lblState = Label("Init", Atlas.uiSkin)
    val lblTurn = Label("Turn: 0", Atlas.uiSkin)
    val lblTime = TimerLabel(Atlas.uiSkin)

    init {
        with(topBar) {
            //setDebug(true)
            add(lblTurn).colspan(1).width(150f).left()
            add(lblState).colspan(2).center().expand()
            add(lblTime).colspan(1).width(150f).padRight(5f).right()

            // shows the game menu
            addListener(object : ClickListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                       pointer: Int, button: Int): Boolean {
                    DialogProvider.showGameMenu(HudController.stage)
                    return true
                }
            })

        }
    }

    /**
     * Init the object with this method. If not called before usage
     * an error will be thrown.
     * @param stage Stage
     * @param engine PooledEngines
     * @return This for chaining.
     */
    fun init(stage: Stage, engine: PooledEngine) {
        this.stage = stage
        this.engine = engine

        initActors()
        StrategoGame.register(this)
    }

    /**
     * Init actors.
     */
    private fun initActors() {
        // init counters for pieces off grid
        for (rank in Rank.values()) {
            for (player in PlayerId.values()) {
                stage.addActor(CounterLabel(rank, player, Atlas.defaultSkin))
            }
        }

        // ready btn
        val btn = ReadyButton(Atlas.uiSkin)


        // lblstate
        with(lblState) {
            setAlignment(Align.center)
        }

        // lblTurn
        with(lblTurn) {
            setText("Turn: 0")
            setAlignment(Align.left)
            isVisible = false
        }

        //lblTime
        with(lblTime) {
            setAlignment(Align.right)
            reset()
        }


        with(topBar) {
            width = HudController.stage.width
            height = 50f
            y = HudController.stage.height - height
        }


        // add to stage
        with(stage) {
            addActor(btn)
            addActor(topBar)
        }
    }

    /**
     * Eventbus subscription for [GameController] state change.
     * @param msg StateEvent
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onStateEvent(msg: StateEvent) {
        Gdx.app.log("game_state", "state: ${msg.state}")
        lblState.setText(msg.state)

        lblTurn.isVisible = when (GameController.state) {
            GameState.TURN_PLAYER_1, GameState.TURN_PLAYER_2 -> true
            else                                             -> false
        }
        msg.turn?.also {
            lblTurn.setText("Turn: $it")
        }
    }


}