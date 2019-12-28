package com.mo.stratego.model

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.component.HighlightComponent
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.ui.Atlas


/**
 * Enum of highlight types
 */
enum class HighlightType(val fileName: String) {
    SQUARE("highlight_square"),
    MOVE("highlight_square"),
    CIRCLE("highlight_circle");

    companion object {
        private val cm = ComponentMapper.getFor(HighlightComponent::class.java)

        /**
         * Factory method for highlights. This methods add
         * a [HighlightComponent] and a [TextureComponent] for the given type
         * to the entity.
         * @param entity Entity
         * @param type HighlightType
         */
        fun createHighlight(entity: Entity, piece: Piece, type: HighlightType,
                            move: GridPoint2?) {
            entity.add(HighlightComponent(type, piece, move))
            entity.add(
                    Atlas.getHighlightTexture(type))
        }

        /**
         * Deletes all highlights from the engine.
         * @param engine Engine
         */
        fun deleteHighlight(engine: Engine, vararg type: HighlightType) {
            val family = Family.all(HighlightComponent::class.java).get()
            val highlights = engine.getEntitiesFor(family)
            // backwards iteration needed to delete all highlights
            for (index in highlights.size() - 1 downTo 0) {
                if (cm.get(highlights[index]).type in type)
                    engine.removeEntity(highlights[index])
            }
        }


    }


}