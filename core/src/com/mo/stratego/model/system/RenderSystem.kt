package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mo.stratego.model.comparator.ZComparator
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.util.Constants

/**
 * System that renders all entities with a [PositionComponent] and a
 * [TextureComponent]. The render order is specified through the
 * z value of the [PositionComponent].
 */
class RenderSystem(private val batch: SpriteBatch,
                   private val camera: OrthographicCamera) :
    SortedIteratingSystem(
            Family.all(PositionComponent::class.java,
                       TextureComponent::class.java).get(),
            ZComparator()) {

    // component mapper to retrieve components of an entity
    private val pm: ComponentMapper<PositionComponent> =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val tm: ComponentMapper<TextureComponent> =
            ComponentMapper.getFor(TextureComponent::class.java)

    /**
     * Called every frame by the [Engine]
     */
    override fun update(deltaTime: Float) {
        batch.projectionMatrix = camera.combined
        // render entities in one batch
        batch.begin()
        // super#update iterates through sorted list of entities and calls processEntity
        super.update(deltaTime)
        batch.end()
    }

    /**
     * Draw [Entity] on screen by the defined texture of the [TextureComponent]
     * at the position defined by the [PositionComponent].
     */
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null)
            return

        // get relevant components
        val position = pm.get(entity) ?: return
        val texture = tm.get(entity) ?: return

        // draw sprite
        batch.draw(
                texture.region,
                position.position.x.toFloat(),
                position.position.y.toFloat(),
                texture.origin.x, texture.origin.y,
                texture.region.regionWidth.toFloat(),
                texture.region.regionHeight.toFloat(),
                Constants.getPixelToUnit(texture.scale.x),
                Constants.getPixelToUnit(texture.scale.y),
                texture.rotation
                  )

    }

}