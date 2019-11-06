package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mo.stratego.model.component.InvisibleComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.util.Constants
import com.mo.stratego.util.ZComparator

/**
 * System that renders all entities with a [PositionComponent] and a
 * [TextureComponent]. The render order is specified through the z value
 * of the [PositionComponent].
 */
class RenderSystem(private val batch: SpriteBatch, private val camera: OrthographicCamera) :
        SortedIteratingSystem(
                Family.all(PositionComponent::class.java,
                        TextureComponent::class.java)
                      .exclude(InvisibleComponent::class.java).get(),
                ZComparator()
        ) {

    // component mapper to retrieve components of an entity
    private val pm : ComponentMapper<PositionComponent> =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val tm : ComponentMapper<TextureComponent> =
            ComponentMapper.getFor(TextureComponent::class.java)

    override fun update(deltaTime: Float) {
        batch.projectionMatrix = camera.combined
        // render entities in one batch
        batch.begin()
        // super#update iterates through sorted list of entities and calls processEntity
        super.update(deltaTime)
        batch.end()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        // get relevant components
        val position = pm.get(entity)
        val texture = tm.get(entity)

        // nothing to draw
        if (texture == null || texture.isHidden)
            return

        // draw sprite
        batch.draw(
                texture.region,
                position.position.x, position.position.y,
                texture.origin.x, texture.origin.y,
                texture.region.regionWidth.toFloat(), texture.region.regionHeight.toFloat(),
                Constants.getPixelToUnit(texture.scale.x) , Constants.getPixelToUnit(texture.scale.y),
                texture.rotation
                )

    }

}