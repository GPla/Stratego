package com.mo.stratego.ui.provider

import com.badlogic.gdx.scenes.scene2d.Stage
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.Atlas
import com.mo.stratego.ui.Screens
import com.mo.stratego.ui.control.OneButtonDialog

/**
 * Provider class for defined dialogs.
 */
object DialogProvider {

    /**
     * TODO
     */
    fun showConnectionLost(stage: Stage) {
        OneButtonDialog("Connection lost",
                        "You have lost the Connection.",
                        "Return to Main Menu",
                        { StrategoGame.switchScreen(Screens.MAINMENU) },
                        Atlas.uiSkinMed).apply {
            show(stage)
        }
    }
}