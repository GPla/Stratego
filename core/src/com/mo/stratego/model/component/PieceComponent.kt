package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.mo.stratego.model.Piece

/**
 * Wrapper component for the [Piece] class.
 */
class PieceComponent(val piece: Piece) : Component