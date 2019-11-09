package com.mo.stratego.model

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.mo.stratego.model.component.HighlightComponent


/**
 * Enum of highlight types
 */
enum class HighlightType(val fileName: String) {
    SQUARE("highlight_square"),
    CIRCLE("highlight_circle");

    companion object {
        /**
         * Factory method for highlights. This methods add
         * a [HighlightComponent] and a [TextureComponent] for the given type
         * to the entity.
         * @param entity Entity
         * @param type HighlightType
         */
        fun createHightlight(entity: Entity, type: HighlightType) {
            entity.add(HighlightComponent(type))
            entity.add(Atlas.getHighlightTexture(type))
        }

        /**
         * Deletes all hightlights from the engine.
         * @param engine Engine
         */
        fun deleteHighlight(engine: Engine) {
            val family = Family.all(HighlightComponent::class.java).get()
            val highlights = engine.getEntitiesFor(family)
            // backwards iteration needed to delete all highlights
            for (index in highlights.size() - 1 downTo 0)
                engine.removeEntity(highlights[index])
        }
    }


}