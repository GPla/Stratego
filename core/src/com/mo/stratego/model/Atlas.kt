package com.mo.stratego.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.model.player.PlayerId

/**
 * Object that loads the [TextureAtlas] with all game assets.
 */
object Atlas {
    private val atlas: TextureAtlas = TextureAtlas("pics.atlas")
    val uiSkin =
            Skin(Gdx.files.internal("ui/plain-james/skin/plain-james-ui.json"))
    val defaultSkin =
            Skin(Gdx.files.internal("ui/default/skin/uiskin.json"))


    /**
     * Loads the appropriate texture from the atlas.
     * @param rank Rank
     * @param owner Player
     * @return A [TextureComponent] with the [Piece]'s texture.
     * Returns null if no texture was found.
     */
    fun getPieceTexture(rank: Rank, playerId: PlayerId): TextureComponent {

        // get texture region from the atlas
        val atlasRegion =
                atlas.findRegions(
                        "${rank.rank.toLowerCase()}_${rank.title.toLowerCase()}")

        // no valid texture
        if (atlasRegion == null ||
            playerId.id >= atlasRegion.size ||
            atlasRegion.size == 0)
            return returnNotFound()

        return TextureComponent(atlasRegion[playerId.id])
    }

    /**
     * Loads the backside texture for the playerId.
     * @param playerId Int
     * @return A [TextureComponent] with the backside texture for the playerId.
     * Returns dummy texture if no texture was found.
     */
    fun getPieceBacksideTexture(playerId: PlayerId): TextureComponent {
        // get texture from atlas
        val atlasRegion = atlas.findRegions("backside")

        // no valid texture
        if (atlasRegion == null ||
            playerId.id >= atlasRegion.size ||
            atlasRegion.size == 0)
            return returnNotFound()

        return TextureComponent(atlasRegion[playerId.id])
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
