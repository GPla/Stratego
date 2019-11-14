package com.mo.stratego.ui.control

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.TextureComponent

class ImageList(skin: Skin) : List<Piece>(skin) {

    private val texMapper = ComponentMapper.getFor(TextureComponent::class.java)

    override fun drawItem(batch: Batch?, font: BitmapFont?, index: Int,
                          item: Piece?, x: Float, y: Float,
                          width: Float): GlyphLayout {

        val texture = texMapper.get(item)

        texture?.let {
            batch?.draw(it.region, x, y)
        }

        return font?.draw(batch, "abc", x + texture.region.regionWidth + 5, y,
                          0,
                          3, width - texture.region.regionWidth - 5, 1, false,
                          "...") ?: GlyphLayout()
    }

}