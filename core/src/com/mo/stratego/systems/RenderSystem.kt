package com.mo.stratego.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Array
import com.mo.stratego.comparators.ZComparator
import com.mo.stratego.components.PositionComponent
import com.mo.stratego.components.TextureComponent

/**
 * System that renders all entities with a [PositionComponent] and a
 * [TextureComponent]. The render order is specified through the z value
 * of the [PositionComponent].
 */
class RenderSystem(var batch: SpriteBatch) :
        SortedIteratingSystem(
                Family.all(PositionComponent::class.java,
                        TextureComponent::class.java).get(),
                ZComparator()
        ) {

    // component mapper to retrieve components of an entity
    private val pm: ComponentMapper<PositionComponent> =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val tm: ComponentMapper<TextureComponent> =
            ComponentMapper.getFor(TextureComponent::class.java)

    // entities that need to be rendered
    private val renderQueue: Array<Entity> = Array()

    override fun update(deltaTime: Float) {
        // render entities for frame
        batch.begin()
        // loop through each entity in the queue
        for (entity in renderQueue) {
            val pos = pm.get(entity)
            val tex = tm.get(entity)

            if (tex == null || tex.isHidden)
                continue

            // draw entity
            batch.draw(tex.region, pos.position.x, pos.position.y)
        }
        batch.end()

        // reset queue
        renderQueue.clear()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        // add to queue render them all at once with a SpriteBatch
        renderQueue.add(entity)
    }
}