package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mo.stratego.model.component.SoundComponent
import com.mo.stratego.model.sound.SoundProvider

/**
 * TODO
 *
 */
class SoundSystem :
    IteratingSystem(Family.all(SoundComponent::class.java).get()) {

    private val soundMapper = ComponentMapper.getFor(SoundComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null)
            return

        val sound = soundMapper.get(entity)
        SoundProvider.playSound(sound.soundType)

        entity.remove(SoundComponent::class.java)
    }
}