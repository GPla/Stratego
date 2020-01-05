package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.mo.stratego.model.sound.SoundType

/**
 *  Component that defines which sound should be played.
 * @property soundType Type of sound
 */
class SoundComponent(val soundType: SoundType) : Component