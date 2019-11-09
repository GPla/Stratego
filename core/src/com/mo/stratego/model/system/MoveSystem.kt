package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PositionComponent

class MoveSystem : IteratingSystem(Family.all(PositionComponent::class.java,
                                              MoveComponent::class.java).get()) {

    // component mapper
    private val posMapper =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val movMapper = ComponentMapper.getFor(MoveComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val position = posMapper.get(entity)
        val move = movMapper.get(entity)

        if (entity == null ||
            position == null ||
            move == null)
            return

        // check if move is valid 
        if ()
        // move entity to target position
            position.position.add(move.move)

        entity.remove(MoveComponent::class.java)

    }
}