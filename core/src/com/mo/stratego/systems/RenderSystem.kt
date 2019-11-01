package com.mo.stratego.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mo.stratego.comparators.ZComparator
import com.mo.stratego.components.PositionComponent
import com.mo.stratego.components.TextureComponent

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

    // scale pixel to game unit, 32 pixel = 1 game unit
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


        // origin = center of texture
        val originX = texture.region.regionWidth / 2f
        val originY = texture.region.regionHeight / 2f

        //TODO: fix position
        // draw entity
        batch.draw(
                texture.region,
                position.position.x, position.position.y,
                originX, originY,
                texture.region.regionWidth.toFloat(), texture.region.regionHeight.toFloat(),
                pixelToUnit(texture.scale.x) , pixelToUnit(texture.scale.y),
                texture.rotation
                )

    }

    /**
     * Translates pixel to game unit
     * @param pixel
     * @return corresponding game unit
     */
    private fun pixelToUnit(pixel : Float) = pixel * unitScale

}