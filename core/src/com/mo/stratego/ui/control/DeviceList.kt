package com.mo.stratego.ui.control

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Array
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.util.Constants

/**
 * List that shows all devices found by the [CommunicationHandler].
 * @param skin Skin
 */
class DeviceList(skin: Skin) : List<String>(skin) {
    override fun act(delta: Float) {
        try {
            // show available devices
            setItems(Array<String>(
                    CommunicationHandler.iCom.getAvailableDevices().toTypedArray()))
        } catch (e: Exception) {
            Gdx.app.log(Constants.TAG_ERROR, e.message)
        }
    }
}