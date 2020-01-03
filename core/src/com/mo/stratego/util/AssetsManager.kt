package com.mo.stratego.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.assets.loaders.SoundLoader
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.utils.Disposable
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.Piece
import com.mo.stratego.model.Rank
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.model.sound.SoundType
import com.mo.stratego.ui.Font


/**
 * Object that provides and manages all assets for the game.
 */
object AssetsManager : Disposable {

    lateinit var manager: AssetManager
        private set

    private lateinit var atlas: TextureAtlas

    // skins
    lateinit var uiSkin: Skin
        private set
    lateinit var uiSkinMed: Skin
        private set
    lateinit var uiSkinBig: Skin
        private set
    lateinit var defaultSkin: Skin
        private set

    // textures
    lateinit var mainMenu: Texture
        private set
    lateinit var endLost: Texture
        private set
    lateinit var endWon: Texture
        private set
    lateinit var endDraw: Texture
        private set
    lateinit var backArrow: Texture
        private set

    /**
     * Map of Font to BitmapFont.
     */
    lateinit var fontMap: MutableMap<Font, BitmapFont>

    /**
     * Map of [SoundType] to Sound.
     */
    lateinit var soundMap: MutableMap<SoundType, Sound>
        private set

    fun init() {
        manager = AssetManager()
        loadAtlas()
        loadFonts()
        loadSkins()
        loadTextures()
        loadSounds()
    }

    /**
     * Loads atlas.
     */
    private fun loadAtlas() {
        manager.load(Constants.ATLAS_PATH, TextureAtlas::class.java)
        manager.finishLoading()
        atlas = manager.get(Constants.ATLAS_PATH)
    }

    /**
     * Loads and generates fonts.
     *
     */
    private fun loadFonts() {
        val resolver = InternalFileHandleResolver()
        manager.setLoader(FreeTypeFontGenerator::class.java,
                          FreeTypeFontGeneratorLoader(resolver))
        manager.setLoader(BitmapFont::class.java, ".ttf",
                          FreetypeFontLoader(resolver))

        fontMap = mutableMapOf()

        // load
        Font.values().forEach {
            FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
                fontFileName = Constants.FONT_PATH
                fontParameters.size = it.size
                manager.load(it.internalName, BitmapFont::class.java, this)
            }
        }
        manager.finishLoading()

        // store
        Font.values().forEach {
            fontMap[it] = manager.get(it.internalName)
        }
    }

    /**
     * Loads all skins.
     *
     */
    private fun loadSkins() {
        val resolver = InternalFileHandleResolver()
        manager.setLoader(Skin::class.java, SkinLoader(resolver))

        manager.load(Constants.SKIN_PATH, Skin::class.java)
        manager.finishLoading()

        uiSkin = Skin(Gdx.files.internal(Constants.UISKIN_PATH))
        uiSkinMed = Skin(Gdx.files.internal(Constants.UISKIN_PATH))
        uiSkinBig = Skin(Gdx.files.internal(Constants.UISKIN_PATH))
        defaultSkin = manager.get(Constants.SKIN_PATH)

        val skins = arrayOf(uiSkin, uiSkinMed, uiSkinBig)


        //setup skins with different sized fonts
        for (item in skins.zip(Font.values())) {
            with(item.first) {
                get(TextButtonStyle::class.java).font = fontMap[item.second]
                get(ListStyle::class.java).font = fontMap[item.second]
                get(LabelStyle::class.java).font = fontMap[item.second]
                get(TextFieldStyle::class.java).font = fontMap[item.second]
                get(WindowStyle::class.java).titleFont = fontMap[item.second]
                get(CheckBoxStyle::class.java).font = fontMap[item.second]
                get(SelectBoxStyle::class.java).font = fontMap[item.second]
                get(SelectBoxStyle::class.java).listStyle.font =
                        fontMap[item.second]
            }
        }
    }

    /**
     * Loads all textures.
     */
    private fun loadTextures() {
        val resolver = InternalFileHandleResolver()
        manager.setLoader(Texture::class.java, TextureLoader(resolver))

        manager.load(Constants.MAIN_BG_PATH, Texture::class.java)
        manager.load(Constants.LOST_BG_PATH, Texture::class.java)
        manager.load(Constants.WON_BG_PATH, Texture::class.java)
        manager.load(Constants.DRAW_BG_PATH, Texture::class.java)
        manager.load(Constants.ARROW_LEFT_PATH, Texture::class.java)
        manager.finishLoading()

        mainMenu = manager.get(Constants.MAIN_BG_PATH)
        endLost = manager.get(Constants.LOST_BG_PATH)
        endWon = manager.get(Constants.WON_BG_PATH)
        endDraw = manager.get(Constants.DRAW_BG_PATH)
        backArrow = manager.get(Constants.ARROW_LEFT_PATH)
    }

    /**
     * Loads all sounds defined in [SoundType].
     */
    private fun loadSounds() {
        val resolver = InternalFileHandleResolver()
        manager.setLoader(Sound::class.java, SoundLoader(resolver))

        soundMap = mutableMapOf()

        // load
        SoundType.values().forEach {
            manager.load(it.path, Sound::class.java)
        }
        manager.finishLoading()

        // store
        SoundType.values().forEach {
            soundMap[it] = manager.get(it.path)
        }
    }

    /**
     * Loads the appropriate frontside texture from the atlas.
     * @param rank Rank
     * @param playerId Player id
     * @return A [TextureComponent] with the [Piece]'s texture.
     * Returns null if no texture was found.
     */
    fun getPieceTexture(rank: Rank, playerId: PlayerId): TextureComponent {
        // get texture region from the atlas
        val atlasRegion = atlas.findRegions(
                "${rank.rank.toLowerCase()}_${rank.title.toLowerCase()}")

        // no valid texture, replace with not found texture
        if (atlasRegion == null ||
            playerId.id >= atlasRegion.size ||
            atlasRegion.size == 0)
            return returnNotFound()

        return TextureComponent(atlasRegion[playerId.id])
    }

    /**
     * Loads the backside texture from the atlas for the playerId.
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

    /**
     * Disposes all resources.
     */
    override fun dispose() {
        manager.dispose()
        atlas.dispose()
        fontMap.values.forEach {
            it.dispose()
        }
        soundMap.values.forEach {
            it.dispose()
        }
        uiSkin.dispose()
        uiSkinMed.dispose()
        uiSkinBig.dispose()
        mainMenu.dispose()
        endDraw.dispose()
        endWon.dispose()
        endLost.dispose()
        backArrow.dispose()
        manager.dispose()
    }
}
