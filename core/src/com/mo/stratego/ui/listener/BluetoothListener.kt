package com.mo.stratego.ui.listener

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener

class BluetoothListener : InputListener() {
    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                           button: Int): Boolean {

        Gdx.app.log("bth", "clicked")
        return true
    }
}