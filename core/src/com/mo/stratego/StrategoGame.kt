package com.mo.stratego

import com.badlogic.gdx.Game
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.model.communication.ICommunication
import org.greenrobot.eventbus.EventBus

//todo desc
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
    }
}