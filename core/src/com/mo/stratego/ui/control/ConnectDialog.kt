package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mo.stratego.model.Atlas
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.util.Constants

class ConnectDialog(skin: Skin, val titleText: String = "Select a Device") :
    Dialog("Select a Device", skin) {

    private val loadLabel = LoadLabel(0.33f, '.',
                                      CommunicationHandler.iCom::isScanning,
                                      skin)

    private val deviceList = DeviceList(Atlas.uiSkin)

    init {
        with(contentTable) {
            isMovable = false

            // content
            val pane = ScrollPane(deviceList, skin)

            add(pane).width(Constants.getUnitToPixel(7f))
                    .height(Constants.getUnitToPixel(3f))

            button("Connect", 1)
            button("Refresh", 0)
            button("Cancel", -1)
        }

    }

    override fun act(delta: Float) {
        loadLabel.act(delta)
        titleLabel.setText("$titleText ${loadLabel.text}")
        super.act(delta)
    }

    override fun show(stage: Stage?): Dialog {
        if (!CommunicationHandler.iCom.isScanning)
            CommunicationHandler.iCom.startScan()

        return super.show(stage)
    }

    /**
     * Hides the dialog and disables bluetooth.
     */
    override fun hide() {
        super.hide()
    }

    /**
     * Processes the button result codes.
     * @param object Result code
     */
    override fun result(`object`: Any?) {
        var result = `object` as Int
        when (result) {
            0 -> {
                with(CommunicationHandler.iCom) {
                    if (isScanning)
                        stopScan()
                    startScan()
                }
                cancel()
            }
            1 -> {
                CommunicationHandler.iCom.connect(deviceList.selected)
            }
        }

    }
}