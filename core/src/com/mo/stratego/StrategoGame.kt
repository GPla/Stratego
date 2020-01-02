package com.mo.stratego

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.model.communication.ICommunication
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.ui.BackButtonHandler
import com.mo.stratego.ui.Screens
import com.mo.stratego.util.AssetsManager
import com.mo.stratego.util.Constants
import org.greenrobot.eventbus.EventBus

// TODO cancel back button
// TODO put tags in constants
/**
 * LibGDX entry point. Class that handles the currently displayed screen.
 */
object StrategoGame : Game(), BackButtonHandler {

    /**
     * Persistent user settings.
     */
    val preferences: Preferences by lazy {
        Gdx.app.getPreferences("Settings")
    }

    /**
     * Initialize the game.
     * @param iCom Communication handler
     */
    fun init(iCom: ICommunication) {
        CommunicationHandler.init(iCom)
    }

    /**
     * List that contains registered [EventBus] listener, that do
     * not manage their own lifespan. On app dispose() all instances
     * will be unregistered.
     */
    private val eventBusListener: MutableList<Any> = mutableListOf()

    /**
     * Registers for the [EventBus].
     * @param any Any
     */
    fun register(any: Any) {
        if (EventBus.getDefault().isRegistered(any))
            return
        EventBus.getDefault().register(any)
        eventBusListener.add(any)
    }

    /**
     * Unregister from the [EventBus].
     * @param any Any
     */
    fun unregister(any: Any) {
        if (!EventBus.getDefault().isRegistered(any))
            return

        EventBus.getDefault().unregister(any)
        eventBusListener.remove(any)
    }

    override fun create() {
        GameMap.init()
        AssetsManager.init()
        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        setScreen(MainMenuScreen())
    }

    override fun dispose() {
        super.dispose()
        // dispose resources
        GameMap.dispose()
        AssetsManager.dispose()
        // save preferences to disk
        preferences.flush()
        // unregister all instances from the event bus
        eventBusListener.forEach { EventBus.getDefault().unregister(it) }
        CommunicationHandler.iCom.stopService()
    }

    /**
     * Switches the screen.
     * @param type Screen
     */
    fun switchScreen(type: Screens) {
        Gdx.app.log(Constants.TAG_GAME, "switch screen: $type")

        // has to be run on the main thread, otherwise its undefined behavior
        // or simply does not work
        Gdx.app.postRunnable {
            // switch to screen
            val oldScreen = this.screen
            this.screen = type.createInstance()
            this.screen.show()
            oldScreen.dispose()
        }
    }

    override fun handleBackButton() {

    }
}
