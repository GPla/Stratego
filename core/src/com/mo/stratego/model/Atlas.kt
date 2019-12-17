package com.mo.stratego.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.model.player.PlayerId


/**
 * Object that loads the [TextureAtlas] with all game assets.
 */
object Atlas {
    private val atlas: TextureAtlas = TextureAtlas("pics.atlas")
    val uiSkin =
            Skin(Gdx.files.internal("ui/plain-james/skin/plain-james-ui.json"))
    val uiSkinMed =
            Skin(Gdx.files.internal("ui/plain-james/skin/plain-james-ui.json"))
    val uiSkinBig =
            Skin(Gdx.files.internal("ui/plain-james/skin/plain-james-ui.json"))
    val defaultSkin =
            Skin(Gdx.files.internal("ui/default/skin/uiskin.json"))

    val font26: BitmapFont
    val font32: BitmapFont
    val font48: BitmapFont
    val fontTitle: BitmapFont


    init {
        // load font for uiskin
        val generator = FreeTypeFontGenerator(
                Gdx.files.internal("fonts/OpenSans-Regular.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 26
        font26 = generator.generateFont(parameter)

        parameter.size = 32
        font32 = generator.generateFont(parameter)

        parameter.size = 48
        font48 = generator.generateFont(parameter)

        // title font
        parameter.size = 100
        parameter.borderColor = Color.BLACK
        parameter.borderWidth = 5f
        parameter.shadowColor = Color.BLACK
        parameter.shadowOffsetX = 5
        parameter.shadowOffsetY = 5
        fontTitle = generator.generateFont(parameter)

        generator.dispose()

        //setup skins with different sized fonts
        with(uiSkin) {
            get(TextButtonStyle::class.java).font = font26
            get(ListStyle::class.java).font = font26
            get(LabelStyle::class.java).font = font26
            get(TextFieldStyle::class.java).font = font26
            get(WindowStyle::class.java).titleFont = font26
            get(CheckBoxStyle::class.java).font = font26
            get(SelectBoxStyle::class.java).font = font26
            get(SelectBoxStyle::class.java).listStyle.font = font26
        }

        with(uiSkinBig) {
            get(TextButtonStyle::class.java).font = font48
            get(ListStyle::class.java).font = font48
            get(LabelStyle::class.java).font = font48
            get(TextFieldStyle::class.java).font = font48
            get(WindowStyle::class.java).titleFont = font48
            get(CheckBoxStyle::class.java).font = font48
            get(SelectBoxStyle::class.java).font = font48
            get(SelectBoxStyle::class.java).listStyle.font = font48
        }

        with(uiSkinMed) {
            get(TextButtonStyle::class.java).font = font32
            get(ListStyle::class.java).font = font32
            get(LabelStyle::class.java).font = font32
            get(TextFieldStyle::class.java).font = font32
            get(WindowStyle::class.java).titleFont = font32
            get(CheckBoxStyle::class.java).font = font32
            get(SelectBoxStyle::class.java).font = font32
            get(SelectBoxStyle::class.java).listStyle.font = font32
        }
    }


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
