package com.mo.stratego.ui

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mo.stratego.model.Rank
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.ui.control.CounterLabel
import com.mo.stratego.ui.control.ReadyButton
import com.mo.stratego.util.StateEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Controller for ui elements.
 */
object HudController {

    lateinit var stage: Stage
    private lateinit var engine: PooledEngine
    private val skin = Skin(Gdx.files.internal("ui/default/skin/uiskin.json"))

    // actors
    private val lblState = Label("Init", skin)

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
    }

    /**
     * Init actors.
     */
    private fun initActors() {
        // init counters for pieces off grid
        for (rank in Rank.values()) {
            for (player in PlayerId.values()) {
                stage.addActor(CounterLabel(rank, player, skin))
            }
        }

        // ready btn
        val btn = ReadyButton(skin)
        stage.addActor(btn)

        // lblstate
        // FIXME position
        with(lblState) {
            setPosition(4.7f, 17f)
            setFontScale(1.3f)
        }
        stage.addActor(lblState)
    }

    /**
     * GreenRobot Eventbus subscription.
     * @param msg StateEvent
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onStateEvent(msg: StateEvent) {
        Gdx.app.log("state", "state: ${msg.state}")
        // TODO label with state
        lblState.setText(msg.state)
    }

}