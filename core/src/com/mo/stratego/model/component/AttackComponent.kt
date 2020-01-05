package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.mo.stratego.model.Piece

/**
 * Component for attacking other [Piece]s.
 * @property piece attacked piece
 */
class AttackComponent(val piece: Piece) : Component