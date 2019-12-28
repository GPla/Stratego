package com.mo.stratego.ui.provider

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mo.stratego.StrategoGame
import com.mo.stratego.ui.Atlas
import com.mo.stratego.ui.Screens
import com.mo.stratego.ui.control.ConfirmDialog
import com.mo.stratego.ui.control.GameMenuDialog
import com.mo.stratego.ui.control.OneButtonDialog

/**
 * Provider class for dialogs.
 */
object DialogProvider {

    private val gameMenu = GameMenuDialog(
            Atlas.uiSkinMed)

    /**
     * Shows the connection lost dialog. Upon accepting, the main
     * menu is shown.
     * @param stage Stage
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

    /**
     * Shows the game menu.
     * @param stage Stage
     */
    fun showGameMenu(stage: Stage) {
        gameMenu.show(stage)
    }

    /**
     * Show confirmation dialog with two options: confirm and cancel.
     * @param msg Message
     * @param callback Callback, called if confirm is pressed.
     * @param stage Stage
     */
    fun showConfirmationDialog(msg: String, callback: () -> Unit, skin: Skin,
                               stage: Stage) {

        ConfirmDialog(msg, callback, skin).show(stage)
    }

}