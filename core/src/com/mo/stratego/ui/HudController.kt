package com.mo.stratego.ui

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.mo.stratego.model.Atlas
import com.mo.stratego.model.Rank
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.ui.control.CounterLabel
import com.mo.stratego.ui.control.ReadyButton
import com.mo.stratego.util.Constants
import com.mo.stratego.util.StateEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Controller for ui elements.
 */
object HudController {

    lateinit var stage: Stage
    private lateinit var engine: PooledEngine

    // actors
    private val lblState = TextButton("Init", Atlas.uiSkin)

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
                stage.addActor(CounterLabel(rank, player, Atlas.defaultSkin))
            }
        }

        // ready btn
        val btn = ReadyButton(Atlas.uiSkin)
        stage.addActor(btn)


        // lblstate
        with(lblState) {
            updateState("Init")
            this.label.setAlignment(Align.center)
            touchable = Touchable.disabled
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
        updateState(msg.state)
    }

    /**
     * Updates the state label's text and position.
     * @param text Text
     */
    fun updateState(text: String) {
        with(lblState) {
            setText(text)
            pack()
            setPosition(Constants.getUnitToPixel(
                    GameMap.width / 2f) - this.width / 2f,
                        Constants.getUnitToPixel(17.3f))
        }

    }
}