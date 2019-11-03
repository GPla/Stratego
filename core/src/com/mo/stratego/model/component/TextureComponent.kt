package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

/**
 * Components for drawing a texture on the screen
 */
class TextureComponent(var region: TextureRegion,
                       var scale: Vector2 = Vector2(1.0f, 1.0f),
                       var origin: Vector2 = Vector2(0f, 0f),
                       var rotation: Float = 0f,
                       var isHidden: Boolean = false) : Component
