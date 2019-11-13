package com.mo.stratego.ui

import com.badlogic.ashley.core.*
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.HighlightComponent
import com.mo.stratego.ui.input.HighlightInputListener
import com.mo.stratego.ui.input.PieceInputListener

/**
 * Singleton class that controls the [Actor]'s displayed on the [Stage].
 * Implements [EntityListener] to add a [FieldActor]'s to the [Stage] if
 * a [Piece] or [HighlightComponent] is added to the [Engine].
 */
object FieldController : EntityListener {

    lateinit var stage: Stage
    private lateinit var engine: PooledEngine
    private val highMapper =
            ComponentMapper.getFor(HighlightComponent::class.java)
    private val entityActors: HashMap<Entity, Actor> = HashMap()

    /**
     * Init the object with this method. If not called before usage
     * an error will be thrown.
     * @param objectStage Stage
     * @param engine PooledEngines
     * @return This for chaining.
     */
    fun init(objectStage: Stage, engine: PooledEngine): FieldController {
        this.stage = objectStage
        this.engine = engine
        return this
    }

    /**
     * Added an [FieldActor] for [Piece] instances and entities with a
     * [HighlightComponent].
     * @param entity
     */
    override fun entityAdded(entity: Entity?) {
        if (entity == null)
            return
        val highlight = highMapper.get(entity)

        if (entity is Piece) {
            FieldActor(entity).also {
                entityActors[entity] = it
                stage.addActor(it)
                it.addListener(PieceInputListener(entity, engine))
            }
        } else if (highlight != null && highlight.type == HighlightType.CIRCLE) {
            FieldActor(entity).also {
                entityActors[entity] = it
                stage.addActor(it)
                it.addListener(HighlightInputListener(entity, engine))
            }
        }
    }

    /**
     * Removes the actor that corresponds to the entity
     * @param entity
     */
    override fun entityRemoved(entity: Entity?) {
        if (entityActors.containsKey(entity ?: return)) {
            val actor = entityActors.remove(entity)
            actor?.remove()
        }
    }


}