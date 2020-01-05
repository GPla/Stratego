package com.mo.stratego.model.comparator

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.mo.stratego.model.MoveType
import com.mo.stratego.model.component.MoveComponent

/**
 * Compares two entities with [MoveComponent]s.
 */
class MoveTypeComparator : Comparator<Entity> {

    private val mm = ComponentMapper.getFor(MoveComponent::class.java)

    /**
     * Compares two entities with [MoveComponent]s.
     * @param p0 Entity 1
     * @param p1 Entity 2
     * @return Result of compareTo of [MoveType].
     */
    override fun compare(p0: Entity?, p1: Entity?): Int {
        return mm.get(p0).type.compareTo(mm.get(p1).type)
    }
}