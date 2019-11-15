package com.mo.stratego.ui

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton


//TODO: implement piece placer
object HudController {

    lateinit var stage: Stage
    private lateinit var engine: PooledEngine

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


    fun initStage() {
        val atlas =
                TextureAtlas(Gdx.files.internal("ui/default/skin/uiskin.atlas"))
        val skin =
                Skin(Gdx.files.internal("ui/default/skin/uiskin.json"), atlas)

        //val pieces = ImageList(skin)
        //pieces.setPosition(0f, 0f)
        //pieces.height = 10f
        //pieces.width = 10f
        //engine.entities.filter { it is Piece }
        //.forEach { pieces.items.add(it as Piece) }
        //stage.addActor(pieces)

        val button = TextButton("Click", skin, "default")
        button.height = 1f
        button.width = 2f

        stage.addActor(button)

    }
}