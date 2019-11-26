package com.mo.stratego

import com.badlogic.gdx.Game

class StrategoGame : Game() {
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