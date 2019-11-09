package com.mo.stratego.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mo.stratego.model.component.TextureComponent

/**
 * Object that loads the [TextureAtlas] with all game assets.
 */
object Atlas {
    val atlas: TextureAtlas

    init {
        atlas = TextureAtlas("pics.atlas")
    }

    /**
     * Loads the appropriate texture from the atlas.
     * @param rank Rank
     * @param owner Player
     * @param backside if true the backside is shown
     * @return A [TextureComponent] with the [Piece]'s texture.
     * Returns null if no texture was found.
     */
    fun getPieceTexture(rank: Rank, playerId: Int,
                        backside: Boolean = false): TextureComponent {

        // get texture region from the atlas
        val atlasRegion =
                atlas.findRegions("${rank.rank}_${rank.title.toLowerCase()}")

        // no valid texture
        if (atlasRegion == null ||
            playerId >= atlasRegion.size ||
            atlasRegion.size == 0)
            return returnNotFound()

        return TextureComponent(atlasRegion[playerId])
    }

    /**
     * Loads the backside texture for the playerId.
     * @param playerId Int
     * @return A [TextureComponent] with the backside texture for the playerId.
     * Returns dummy texture if no texture was found.
     */
    fun getPieceBacksideTexture(playerId: Int): TextureComponent {
        // get texture from atlas
        val atlasRegion = atlas.findRegions("backside")

        // no valid texture
        if (atlasRegion == null ||
            playerId >= atlasRegion.size ||
            atlasRegion.size == 0)
            return returnNotFound()

        return TextureComponent(atlasRegion[playerId])
    }


    /**
     * Loads the texture for the given [HighlightType].
     * @param type HighlightType
     * @return A [TextureComponent] with the texture for the [HighlightType].
     * Returns null if no texture was found for the type.
     */
    fun getHighlightTexture(type: HighlightType): TextureComponent {
        val atlasRegion =
                atlas.findRegion(type.fileName) ?: return returnNotFound()

        return TextureComponent(atlasRegion)
    }

    /**
     * @return A [TextureComponent] with a "not found" Texture.
     */
    private fun returnNotFound() =
            TextureComponent(TextureRegion(Texture("not_found.png"), 64, 64))

}
