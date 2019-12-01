package com.mo.stratego

import com.badlogic.gdx.Game
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.model.communication.ICommunication
import org.greenrobot.eventbus.EventBus

//todo desc
class StrategoGame(iCom: ICommunication) : Game() {

    init {
        CommunicationHandler.init(iCom)
    }

    companion object {
        val eventBusListener: MutableList<Any> = mutableListOf()

        fun register(any: Any) {
            EventBus.getDefault().register(any)
            eventBusListener.add(any)
        }

        fun unregister(any: Any) {
            EventBus.getDefault().unregister(any)
            eventBusListener.remove(any)
        }
    }

    override fun create() {
        setScreen(MainMenuScreen(this))
    }

    override fun dispose() {
        super.dispose()
        eventBusListener.forEach { EventBus.getDefault().unregister(it) }
    }
}