package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

/**
 * A component that specifies the map position at which an entity
 * is rendered
 */
class PositionComponent(var position: Vector2, var z: Int = 0) : Component