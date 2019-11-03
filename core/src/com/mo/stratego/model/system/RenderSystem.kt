package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.util.ZComparator

/**
 * System that renders all entities with a [PositionComponent] and a
 * [TextureComponent]. The render order is specified through the z value
 * of the [PositionComponent].
 */
class RenderSystem(private val batch: SpriteBatch, private val camera: OrthographicCamera) :
        SortedIteratingSystem(
                Family.all(PositionComponent::class.java,
                        TextureComponent::class.java).get(),
                ZComparator()
        ) {

    // scale pixels to game units, 32 pixels = 1 game unit
    private val unitScale : Float = 1/32f

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
                0f, 0f, // origin for position and rotation in bottom left corner
                texture.region.regionWidth.toFloat(), texture.region.regionHeight.toFloat(),
                pixelToUnit(texture.scale.x) , pixelToUnit(texture.scale.y),
                texture.rotation
                )

    }

    /**
     * Translates pixels to game units
     * @param pixel
     * @return corresponding game units
     */
    private fun pixelToUnit(pixel : Float) = pixel * unitScale

}