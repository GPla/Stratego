package com.mo.stratego.ui.input

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.InputAdapter
import com.mo.stratego.model.HighlightType
import com.mo.stratego.ui.FieldController

/**
 * Listener that is activated if a click is not handled by
 * the [FieldController]'s stage.
 * @property engine Engine
 * @constructor
 */
class MapListener(val engine: Engine) : InputAdapter() {

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int,
                           button: Int): Boolean {

        // remove highlights
        HighlightType.deleteHighlight(engine)
        return true
    }
}