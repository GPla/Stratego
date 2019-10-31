package com.mo.stratego.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

// inspired by https://www.gamedevelopment.blog/full-libgdx-game-tutorial-entities-ashley/
class TextureComponent(var region: TextureRegion, var isHidden: Boolean) : Component
