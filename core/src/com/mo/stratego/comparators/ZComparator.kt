package com.mo.stratego.comparators

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.mo.stratego.components.PositionComponent


/**
 * Compares z axis of two entities
 */
class ZComparator : Comparator<Entity> {
    private val pm: ComponentMapper<PositionComponent> =
            ComponentMapper.getFor(PositionComponent::class.java)

    /**
     * Compares z axis of two entities with a [PositionComponent]
     * @param p0 Entity
     * @param p1 Entity
     * @return Int 1 if p0.z > p1.z, -1 if p0.z < p1.z, else 0
     */
    override fun compare(p0: Entity?, p1: Entity?): Int {
        return pm.get(p0).z.compareTo(pm.get(p1).z)
    }
}