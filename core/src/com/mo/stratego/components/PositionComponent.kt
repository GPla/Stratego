package com.mo.stratego.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.mo.stratego.systems.RenderSystem

/**
 * A component that specifies the map position at which an entity
 * is rendered.
 * @property position Vector2 map position
 * @property z Int specifies the render order for the [RenderSystem].
 */
class PositionComponent(var position: Vector2, var z: Int) : Component