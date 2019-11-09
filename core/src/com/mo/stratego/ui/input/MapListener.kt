package com.mo.stratego.ui.input

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.InputAdapter
import com.mo.stratego.model.HighlightType

class MapListener(val engine: Engine) : InputAdapter() {

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int,
                           button: Int): Boolean {

        HighlightType.deleteHighlight(engine)
        return true
    }
}