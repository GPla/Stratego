package com.mo.stratego.ui

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.mo.stratego.util.StateEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


//TODO: implement piece placer
object HudController {

    lateinit var stage: Stage
    private lateinit var engine: PooledEngine

    private val actorMap: MutableMap<String, Actor> = mutableMapOf()

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

        initStage()
    }


    private fun initStage() {
        val skin = Skin(Gdx.files.internal("ui/default/skin/uiskin.json"))
        val lab = Label("Text", skin)

        lab.setPosition((Gdx.graphics.width / 2f), Gdx.graphics.height / 2f)
        lab.setFontScale(2f)

        stage.addActor(lab)
        actorMap.put("label", lab)

        val button = TextButton("Ready!", skin)
        button.setPosition(Gdx.graphics.width - 100 - button.width,
                           Gdx.graphics.height / 2f)
        button.setScale(2f)
        button.isTransform = true
        button.addListener {
        }
        //TODO impl

        actorMap.put("btnReady", button)
        stage.addActor(button)

        // TODO add button
        // TODO add animation and make text fancier
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onStateEvent(msg: StateEvent) {
        Gdx.app.log("state", "state: ${msg.state}")
        val lab = actorMap.getValue("label") as Label
        lab.setText(msg.state)
        lab.setPosition((Gdx.graphics.width - lab.width) / 2f,
                        Gdx.graphics.height / 2f)

    }


}