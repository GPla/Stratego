package com.mo.stratego.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mo.stratego.components.PositionComponent
import com.mo.stratego.components.TextureComponent

/**
 * System that renders all entities with a [PositionComponent] and a
 * [TextureComponent]. The render order is specified through the z value
 * of the [PositionComponent].
 */
class RenderSystem(private var batch: SpriteBatch) : EntitySystem() {

    // component mapper to retrieve components of an entity
    private val pm: ComponentMapper<PositionComponent> =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val tm: ComponentMapper<TextureComponent> =
            ComponentMapper.getFor(TextureComponent::class.java)

    // family, filter engine for entities with these components
    private val family: Family = Family.all(PositionComponent::class.java,
            TextureComponent::class.java).get()

    // entities that need to be rendered
    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine?) {
        var ent = engine?.getEntitiesFor(family)
        if (ent != null)
            entities = ent
    }

    override fun update(deltaTime: Float) {

        // render entities for frame
        batch.begin()

        // loop through each entity in the queue
        for (entity in entities) {
            val pos = pm.get(entity)
            val tex = tm.get(entity)

            if (tex == null || tex.isHidden)
                continue

            // draw entity
            batch.draw(tex.region, pos.position.x, pos.position.y)
        }
        batch.end()
    }
}