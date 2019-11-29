package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mo.stratego.StrategoGame

class BluetoothList(skin: Skin) : List<String>(skin) {

    override fun act(delta: Float) {

        items.clear()
        StrategoGame.comHandler.getAvailableDevices().forEach { items.add(it) }
    }
}