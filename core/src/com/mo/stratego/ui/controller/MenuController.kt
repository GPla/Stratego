package com.mo.stratego.ui.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.mo.stratego.GameScreen
import com.mo.stratego.MainMenuScreen
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.Atlas
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.model.communication.OnConnectedEvent
import com.mo.stratego.model.communication.OnConnectingEvent
import com.mo.stratego.model.communication.OnErrorEvent
import com.mo.stratego.ui.Screens
import com.mo.stratego.ui.control.DeviceList
import com.mo.stratego.ui.control.LoadLabel
import com.mo.stratego.ui.control.TimedLabel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Controller for the stage of the [MainMenuScreen].
 */
object MenuController {

    lateinit var stage: Stage
    private var mode = Mode.MAIN

    // backgrounds 
    private val mainBackground = Sprite(Atlas.mainMenu)
    private val plainBackground = Sprite(Atlas.plainMenu)

    // actors
    private val tableMain: Table = Table()
    private val tableMulti: Table = Table()
    private val log = TimedLabel("", 5f, Atlas.uiSkinMed)

    init {
        initMain()
        initMulti()
    }

    /**
     * Init the object with this method. If not called before usage
     * an error will be thrown.
     * @param stage Stage
     * @return This for chaining.
     */
    fun init(stage: Stage): MenuController {
        MenuController.stage = stage

        with(stage) {
            addActor(tableMain)
            addActor(tableMulti)
        }
        show(Mode.MAIN)

        return this
    }

    /**
     * Initializes the actors for MAIN mode.
     */
    private fun initMain() {
        with(tableMain) {
            setFillParent(true)
            align(Align.top)
            //setDebug(true)

            val btnMulti = TextButton("Multiplayer", Atlas.uiSkinBig)
            btnMulti.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    show(Mode.MULTI)
                }
            })

            val btnLocal = TextButton("Local", Atlas.uiSkinBig)
            btnLocal.apply {
                addListener(object : ClickListener() {
                    override fun touchDown(event: InputEvent?, x: Float,
                                           y: Float, pointer: Int,
                                           button: Int): Boolean {
                        return true
                    }

                    override fun touchUp(event: InputEvent?, x: Float, y: Float,
                                         pointer: Int, button: Int) {
                        StrategoGame.switchScreen(Screens.GAME_LOCAL)
                    }
                })
            }

            val btnSettings = TextButton("Settings", Atlas.uiSkinBig)
            btnSettings.addListener(object : ClickListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                       pointer: Int, button: Int): Boolean {
                    return true
                }
            })
            //TODO settings


            add(btnLocal).width(580f).pad(700f, 30f, 10f, 30f)
            row()
            add(btnMulti).width(580f).pad(10f, 30f, 10f, 30f)
            row()
            add(btnSettings).width(580f).pad(10f, 30f, 50f, 30f)
        }
    }


    /**
     * Initializes the actor for MULTI mode.
     */
    private fun initMulti() {
        with(tableMulti) {
            setFillParent(true)
            align(Align.center)
            //setDebug(true)

            val btnBack = ImageButton(TextureRegionDrawable(
                    Texture(Gdx.files.internal("ui/arrow_left.png"))))
            btnBack.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    CommunicationHandler.iCom.disconnect()
                    show(Mode.MAIN)
                }
            })

            val title = LoadLabel("Select a Device", 0.33f, '.',
                                  CommunicationHandler.iCom::isScanning,
                                  Atlas.uiSkinBig)

            val deviceList = DeviceList(Atlas.uiSkinMed)

            val btnSearch = TextButton("Refresh", Atlas.uiSkinMed)
            btnSearch.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    CommunicationHandler.iCom.startScan()
                }
            })

            val btnConnect = TextButton("Connect", Atlas.uiSkinMed)
            btnConnect.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    deviceList.selected?.also {
                        CommunicationHandler.iCom.connect(it)
                    }
                }
            })

            add(btnBack).size(70f).left().pad(20f, 20f, 0f, 0f).colspan(2)
                    .expandX()
            row()
            add(title).colspan(2).left().padLeft(150f)
            row()
            add(deviceList).expandY().fill().pad(20f).colspan(2)
            row()
            add(log).padBottom(20f).colspan(2)
            row().padBottom(80f)
            add(btnConnect).width(300f).height(70f)
            add(btnSearch).width(300f).height(70f)
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

    /**
     * Displays error message to the user.
     * Event occurs if an error occurs in the communication handler.
     * @param event Event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun OnErrorEvent(event: OnErrorEvent) {
        log.setText(event.msg)
    }

    /**
     * Displays connection attempt to user.
     * @param event Event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onConnectingEvent(event: OnConnectingEvent) {
        log.setText("Connecting...")
    }

    /**
     * Enum of display modes for this stage.
     */
    private enum class Mode {
        MAIN,
        MULTI,
        SETTINGS;
    }

    /**
     * Show mode of this stage.
     * @param mode Mode
     */
    private fun show(mode: Mode) {
        this.mode = mode

        tableMain.isVisible = mode == Mode.MAIN
        tableMulti.isVisible = mode == Mode.MULTI

        if (mode == Mode.MULTI) {
            CommunicationHandler.iCom.startScan()
            log.setText("")
            StrategoGame.register(this)
        } else {
            StrategoGame.unregister(this)
        }
    }

    /**
     * @return The background image for the current mode.
     */
    fun getBackground(): Sprite? =
            when (mode) {
                Mode.MAIN -> mainBackground
                else      -> null
            }

}
