package com.mo.stratego

import com.badlogic.gdx.Game
import com.mo.stratego.screens.GameScreen

class StrategoGame : Game(){
    override fun create() {
        setScreen(GameScreen())
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