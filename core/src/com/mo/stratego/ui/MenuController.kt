package com.mo.stratego.ui

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.mo.stratego.MainMenuScreen
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.Atlas

/**
 * Controller for the stage of the [MainMenuScreen].
 */
object MenuController {

    lateinit var stage: Stage

    /**
     * Init the object with this method. If not called before usage
     * an error will be thrown.
     * @param stage Stage
     * @return This for chaining.
     */
    fun init(stage: Stage): MenuController {
        this.stage = stage
        initActors()
        return this
    }

    /**
     * Initialize actors.
     */
    private fun initActors() {
        val table = Table()
        with(table) {
            setFillParent(true)
            align(Align.center)
            setDebug(true)

            val btnStartGame = TextButton("Bluetooth", Atlas.uiSkin)
            btnStartGame.apply {
                setScale(2f)
                align(Align.center)
                setOrigin(Align.center)
                isTransform = true
                addListener(object : ClickListener() {
                    override fun touchDown(event: InputEvent?, x: Float,
                                           y: Float, pointer: Int,
                                           button: Int): Boolean {
                        if (!StrategoGame.comHandler.isScanning)
                            StrategoGame.comHandler.startScan()
                        else
                            StrategoGame.comHandler.stopScan()
                        return true
                    }
                })
            }


            val list = List<String>(Atlas.uiSkin)

            add(btnStartGame).center()
            row()
            add(list).center()
        }


        // add to stage
        with(stage) {
            addActor(table)
        }

    }
}