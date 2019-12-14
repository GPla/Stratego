package com.mo.stratego.ui.controller

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.mo.stratego.GameScreen
import com.mo.stratego.MainMenuScreen
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.Atlas
import com.mo.stratego.model.communication.OnConnectedEvent
import com.mo.stratego.ui.Screens
import com.mo.stratego.ui.control.ConnectDialog
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Controller for the stage of the [MainMenuScreen].
 */
// TODO show error/ success after connection
object MenuController {

    lateinit var stage: Stage
    val connectDialog = ConnectDialog(Atlas.uiSkin)

    /**
     * Init the object with this method. If not called before usage
     * an error will be thrown.
     * @param stage Stage
     * @return This for chaining.
     */
    fun init(stage: Stage): MenuController {
        MenuController.stage = stage
        initActors()
        StrategoGame.register(this)
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

            // multi player
            val btnMulti = TextButton("Bluetooth", Atlas.uiSkinBig)
            btnMulti.apply {
                // button layout
                align(Align.center)
                setOrigin(Align.center)
                isTransform = true

                // button action
                addListener(object : ClickListener() {
                    override fun touchDown(event: InputEvent?, x: Float,
                                           y: Float, pointer: Int,
                                           button: Int): Boolean {

                        connectDialog.show(stage)
                        return true
                    }
                })
            }


            // local 2 player mode
            val btnLocal = TextButton("Local", Atlas.uiSkinBig)
            btnLocal.apply {
                align(Align.center)
                setOrigin(Align.center)
                isTransform = true


                addListener(object : ClickListener() {
                    override fun touchDown(event: InputEvent?, x: Float,
                                           y: Float, pointer: Int,
                                           button: Int): Boolean {
                        StrategoGame.switchScreen(Screens.GAME_LOCAL)
                        return true
                    }
                })
            }

            val btnSettings = TextButton("Settings", Atlas.uiSkinBig)
            //TODO settings


            add(btnLocal).padBottom(100f).width(300f)
            row()
            add(btnMulti).width(300f).padBottom(100f)
            row()
            add(btnSettings).width(300f)


        }

        // title font
        val style =
                Label.LabelStyle(Atlas.fontTitle, Color.FOREST)
        val title = Label("STRATEGO", style)
        title.setPosition((stage.width - title.width) / 2, stage.height - 250)

        // add to stage
        with(stage) {
            addActor(title)
            addActor(table)
        }

    }

    /**
     * Switches to [GameScreen].
     * Event occurs if connected to other device.
     * @param event OnConnectedEvent
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun OnConnectedEvent(event: OnConnectedEvent) {
        StrategoGame.switchScreen(Screens.GAME_MULTI)
        StrategoGame.unregister(this)
    }
}