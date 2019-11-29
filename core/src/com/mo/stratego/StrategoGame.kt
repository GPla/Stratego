package com.mo.stratego

import com.badlogic.gdx.Game
import com.mo.stratego.model.communication.ICommunication

class StrategoGame(com: ICommunication) : Game() {

    //FIXME: architecture of com handler
    init {
        comHandler = com
    }

    companion object {
        lateinit var comHandler: ICommunication
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