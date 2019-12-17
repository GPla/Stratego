package com.mo.stratego

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.model.communication.ICommunication
import com.mo.stratego.ui.Screens
import org.greenrobot.eventbus.EventBus

//todo desc
// TODO cancel back button
// TODO put tags in constants
object StrategoGame : Game() {

    /**
     * Initialize.
     * @param iCom Communication handler
     */
    fun init(iCom: ICommunication) {
        CommunicationHandler.init(iCom)
    }

    /**
     * List that contains registered [EventBus] listener, that do
     * not manage their lifespan.
     */
    val eventBusListener: MutableList<Any> = mutableListOf()

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
        setScreen(MainMenuScreen())
    }

    override fun dispose() {
        super.dispose()
        eventBusListener.forEach { EventBus.getDefault().unregister(it) }
        CommunicationHandler.iCom.disconnect()
    }

    /**
     * Switches the screen.
     * @param type Screen
     */
    fun switchScreen(type: Screens) {
        Gdx.app.log("game", "switch screen: $type")

        // has to be run on the main thread, otherwise its undefined behavior
        // or simply does not work
        Gdx.app.postRunnable {
            // switch to screen
            val oldScreen = this.screen
            this.screen = type.createInstance()
            oldScreen.dispose()
        }
    }
}