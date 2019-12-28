package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mo.stratego.model.component.WaitComponent

/**
 * This system process Entities with a [WaitComponent]. After the waitTime,
 * that's specified in the [WaitComponent], is elapsed, the [WaitComponent]
 * is removed. The [WaitComponent] is used to delay the processing of other
 * components through other systems.
 */
class WaitSystem :
    IteratingSystem(Family.all(WaitComponent::class.java).get()) {

    val waitMapper = ComponentMapper.getFor(WaitComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null)
            return

        val wait = waitMapper.get(entity) ?: return

        wait.time += deltaTime

        if (wait.time >= wait.waitTime)
            entity.remove(WaitComponent::class.java)
    }
}