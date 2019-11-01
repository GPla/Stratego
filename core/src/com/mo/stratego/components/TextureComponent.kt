package com.mo.stratego.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

// inspired by https://www.gamedevelopment.blog/full-libgdx-game-tutorial-entities-ashley/
class TextureComponent(var region: TextureRegion, var scale : Vector2 = Vector2(1.0f, 1.0f),
                       var rotation : Float = 0f, var isHidden: Boolean = false) : Component
