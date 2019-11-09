package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.Piece


//TODO: add description
class HighlightComponent(val type: HighlightType, val piece: Piece,
                         val move: GridPoint2?) : Component