package com.mo.stratego.ui.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.mo.stratego.GameScreen
import com.mo.stratego.MainMenuScreen
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.model.communication.OnConnectedEvent
import com.mo.stratego.model.communication.OnConnectingEvent
import com.mo.stratego.model.communication.OnErrorEvent
import com.mo.stratego.model.sound.SoundProvider
import com.mo.stratego.ui.BackButtonHandler
import com.mo.stratego.ui.Font
import com.mo.stratego.ui.Screens
import com.mo.stratego.ui.control.DeviceList
import com.mo.stratego.ui.control.LoadLabel
import com.mo.stratego.ui.control.TimedLabel
import com.mo.stratego.ui.control.ToggleButton
import com.mo.stratego.ui.provider.DialogProvider
import com.mo.stratego.util.AssetsManager
import com.mo.stratego.util.Constants
import com.mo.stratego.util.MarkdownParser
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Controller for the stage of the [MainMenuScreen].
 */
object MenuController : BackButtonHandler {

    lateinit var stage: Stage
    private var mode = Mode.MAIN

    // backgrounds 
    private val mainBackground = Sprite(AssetsManager.mainMenu)

    // actors
    private val tableMain: Table = Table()
    private val tableMulti: Table = Table()
    private val tableRules: Table = Table()
    private val tableSettings: Table = Table()
    private val log = TimedLabel("", 5f, AssetsManager.uiSkinMed)

    init {
        initMain()
        initMulti()
        initRules()
        initSettings()
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
            addActor(tableRules)
            addActor(tableSettings)
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

            val btnMulti = TextButton("Multiplayer", AssetsManager.uiSkinBig)
            btnMulti.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    show(Mode.MULTI)
                }
            })

            /*
            val btnLocal = TextButton("Local", AssetsManager.uiSkinBig)
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
            } */

            // settings
            val btnSettings = TextButton("Settings", AssetsManager.uiSkinBig)
            btnSettings.addListener(object : ClickListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                       pointer: Int, button: Int): Boolean {
                    show(Mode.SETTINGS)
                    return true
                }
            })

            // rules
            val btnRules = TextButton("Rules", AssetsManager.uiSkinBig)
            btnRules.addListener(object : ClickListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                       pointer: Int, button: Int): Boolean {
                    show(Mode.RULES)
                    return true
                }
            })

            /*
            add(btnLocal).width(580f).pad(700f, 30f, 10f, 30f)
            row() */
            add(btnMulti).width(580f).pad(730f, 30f, 10f, 30f)
            row()
            add(btnSettings).width(580f).pad(10f, 30f, 10f, 30f)
            row()
            add(btnRules).width(580f).pad(10f, 30f, 10f, 30f)
        }
    }


    /**
     * Initializes the actor for MULTI mode.
     */
    private fun initMulti() {
        with(tableMulti) {
            setFillParent(true)
            align(Align.top)
            //setDebug(true)

            val btnBack =
                    ImageButton(TextureRegionDrawable(AssetsManager.backArrow))
            btnBack.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    CommunicationHandler.iCom.disconnect()
                    show(Mode.MAIN)
                }
            })

            val title = LoadLabel("Select a Device", 0.33f, '.',
                                  CommunicationHandler.iCom::isScanning,
                                  AssetsManager.uiSkinBig)

            val deviceList = DeviceList(
                    AssetsManager.uiSkinMed)

            val btnSearch = TextButton("Refresh", AssetsManager.uiSkinMed)
            btnSearch.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    CommunicationHandler.iCom.startScan()
                }
            })

            val btnConnect = TextButton("Connect", AssetsManager.uiSkinMed)
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
     * Initializes the settings menu.
     */
    private fun initSettings() {
        with(tableSettings) {
            setFillParent(true)
            align(Align.top)

            // back
            val btnBack =
                    ImageButton(TextureRegionDrawable(AssetsManager.backArrow))
            btnBack.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    show(Mode.MAIN)
                }
            })

            // title
            val lblTitle = Label("Settings", AssetsManager.uiSkinBig)


            // content
            // sound
            val lblSound = Label("Sound", AssetsManager.uiSkinMed)
            val btnSound = ToggleButton(SoundProvider::isTurnedOn,
                                        AssetsManager.uiSkinMed)
            val tblSound = Table()

            with(tblSound) {
                add(lblSound).padRight(20f)
                add(btnSound)
            }

            // game modifier
            val subHeaderStyle =
                    Label.LabelStyle(AssetsManager.fontMap[Font.SIZE32],
                                     Constants.YELLOW)
            val lblModifier = Label("Game Modifier", subHeaderStyle)

            add(btnBack).size(70f).left().pad(20f, 20f, 0f, 0f)
            row()
            add(lblTitle).center().expandX()
            row()
            add(tblSound).left().pad(30f, 100f, 20f, 0f)
            //row()
            //add(lblModifier).left().pad(20f, 50f, 30f, 0f)

        }
    }

    /**
     * Initializes RULES mode.
     */
    fun initRules() {
        with(tableRules) {
            setFillParent(true)
            align(Align.top)

            val btnBack =
                    ImageButton(TextureRegionDrawable(AssetsManager.backArrow))
            btnBack.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    show(Mode.MAIN)
                }
            })

            val lblTitle = Label("Rules", AssetsManager.uiSkinBig)

            // content
            val rules = Gdx.files.internal(Constants.RULES_PATH)
            val rulesTable = MarkdownParser.parseMarkdownToScene2D(rules)
            val scrollPane = ScrollPane(rulesTable)

            row().expandX()
            add(btnBack).size(70f).left().pad(20f, 20f, 0f, 0f)
            row()
            add(lblTitle).align(Align.center)
            row().padTop(10f)
            add(scrollPane).align(Align.topLeft).expand()

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
        SETTINGS,
        RULES;
    }

    /**
     * Show mode of this stage.
     * @param mode Mode
     */
    private fun show(mode: Mode) {
        this.mode = mode

        tableMain.isVisible = mode == Mode.MAIN
        tableMulti.isVisible = mode == Mode.MULTI
        tableRules.isVisible = mode == Mode.RULES
        tableSettings.isVisible = mode == Mode.SETTINGS

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
                else -> null
            }

    /**
     * Quits the game if in main mode, otherwise returns to main.
     */
    override fun handleBackButton() {
        when (mode) {
            Mode.MAIN -> DialogProvider.showConfirmationDialog(
                    "Do you want to quit the game?", Gdx.app::exit,
                    AssetsManager.uiSkinMed, stage)
            else -> show(Mode.MAIN)
        }
    }
}
