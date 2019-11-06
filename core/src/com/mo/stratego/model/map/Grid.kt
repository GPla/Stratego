package com.mo.stratego.model.map

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent

/**
 * This class represents the game grid.
 */
object Grid : EntityListener {

    val matrix : Array<IntArray> = Array(10) { IntArray(10)}

    private val pm : ComponentMapper<PositionComponent>
            = ComponentMapper.getFor(PositionComponent::class.java)
    private val mm : ComponentMapper<MoveComponent>
            = ComponentMapper.getFor(MoveComponent::class.java)

    override fun entityRemoved(entity: Entity?) {

    }

    /**
     * Update grid if a [Piece] with [PositionComponent] or
     * [MoveComponent] is added.
     */
    override fun entityAdded(entity: Entity?) {
        // check if playing piece
        if(entity !is Piece)
            return

        // check for movement
        val move = mm.get(entity)
        if (move != null)
            update()
        else{
            val pos = pm.get(entity)
            if (pos != null)
                update()
        }
    }

    /**
     * Reloads the complete grid.
     */
    fun reload(){

    }

    /**
     * Updates the changes.
     */
    private fun update(){
        //TODO: implement
    }
}