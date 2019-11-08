package com.mo.stratego.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.mo.stratego.model.Piece

/**
 * Singleton class that controls the [Actor]s displayed on the [Stage].
 * Implements [EntityListener] to add a [PieceActor] to the [Stage] if
 * a [Piece] is added to the [Engine].
 */
object FieldController : EntityListener {

    private lateinit var stage: Stage
    private lateinit var engine: PooledEngine

    /**
     * Init the object with this method. If not called before usage
     * an error will be thrown.
     */
    fun init(stage: Stage, engine: PooledEngine): FieldController {
        this.stage = stage
        this.engine = engine
        return this
    }

    override fun entityAdded(entity: Entity?) {
        // add if class = piece
        if (entity is Piece) {
            stage.addActor(PieceActor(entity, engine))
        }
    }

    override fun entityRemoved(entity: Entity?) {
        // TODO: remove actor?
    }


}