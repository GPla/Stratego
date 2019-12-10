package com.mo.stratego.ui.controller

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
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
    val bluetoothDialog = ConnectDialog(Atlas.uiSkin)
    private lateinit var menuScreen: MainMenuScreen

    /**
     * Init the object with this method. If not called before usage
     * an error will be thrown.
     * @param stage Stage
     * @return This for chaining.
     */
    fun init(stage: Stage, menuScreen: MainMenuScreen): MenuController {
        MenuController.stage = stage
        this.menuScreen = menuScreen
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


            // bluetooth mode
            val btnStartGame = TextButton("Bluetooth", Atlas.uiSkin)
            btnStartGame.apply {
                // button layout
                setScale(2f)
                align(Align.center)
                setOrigin(Align.center)
                isTransform = true

                // button action
                addListener(object : ClickListener() {
                    override fun touchDown(event: InputEvent?, x: Float,
                                           y: Float, pointer: Int,
                                           button: Int): Boolean {

                        bluetoothDialog.show(stage)
                        return true
                    }
                })
            }

            add(btnStartGame).center()
            row()
        }


        // add to stage
        with(stage) {
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
        StrategoGame.switchScreen(Screens.GAME)
    }
}