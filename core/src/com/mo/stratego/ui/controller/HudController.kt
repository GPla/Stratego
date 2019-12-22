package com.mo.stratego.ui.controller

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
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
import com.mo.stratego.util.Constants
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Controller for ui elements.
 */
object HudController {

    lateinit var stage: Stage
    private lateinit var engine: PooledEngine

    // actors
    private val lblState = Label("Init", Atlas.uiSkin)
    private var lblTurn = Label("Turn: 0", Atlas.uiSkin)
    private val topBar = Button(Atlas.uiSkin)
    private val lblTime = TimerLabel(Atlas.uiSkin)

    init {
        with(topBar) {
            //setDebug(true)
            touchable = Touchable.disabled
            align(Align.left)
            setY(Constants.getUnitToPixel(17.7f), Align.center)
            add(lblTurn).colspan(1).width(150f).left()
            add(lblState).colspan(2).center().expand()
            add(lblTime).colspan(1).width(150f).right()
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
        lblTurn = Label("Turn: 0", Atlas.uiSkin)
        with(lblTurn) {
            setAlignment(Align.left)
            isVisible = false
        }

        //lblTime
        with(lblTime) {
            setAlignment(Align.right)
            reset()
        }

        topBar.width = stage.width

        // add to stage
        with(stage) {
            addActor(btn)
            addActor(topBar)

        }
    }

    /**
     * GreenRobot Eventbus subscription for [GameController] state change.
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