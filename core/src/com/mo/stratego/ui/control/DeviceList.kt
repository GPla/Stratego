package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Array
import com.mo.stratego.StrategoGame

class DeviceList(skin: Skin) : List<String>(skin) {

    override fun act(delta: Float) {
        // show available devices
        setItems(Array<String>(
                StrategoGame.comHandler.getAvailableDevices().toTypedArray()))
    }
}