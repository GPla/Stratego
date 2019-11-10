package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.Piece

/**
 * This component defines a move action for a [Piece]. The
 * move property will be added to the [Piece]'s position.
 */
class MoveComponent(val move: GridPoint2) : Component