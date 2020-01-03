package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.Piece

/**
 * Class that marks an Entity as highlight. An highlight is a visual
 * cue that marks a possible move for a Piece.
 * @property type Type of highlight
 * @property piece Piece
 */
class HighlightComponent(val type: HighlightType, val piece: Piece,
                         val move: GridPoint2?) : Component