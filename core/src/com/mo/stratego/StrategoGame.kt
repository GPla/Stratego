package com.mo.stratego

import com.badlogic.gdx.Game
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.model.communication.ICommunication

//todo desc
class StrategoGame(iCom: ICommunication) : Game() {

    init {
        CommunicationHandler.init(iCom)
    }

    override fun create() {
        setScreen(MainMenuScreen(this))
    }

    override fun render() {
        super.render()
    }

    override fun pause() {
        super.pause()
    }

    override fun resume() {
        super.resume()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
    }
}