package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.GridPoint2

/**
 * A component that specifies the map position at which an entity
 * is rendered. The z component is used to determine the render order.
 */
class PositionComponent(var position: GridPoint2, var z: Int = 0) : Component