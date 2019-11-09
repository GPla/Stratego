package com.mo.stratego.model

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.mo.stratego.model.component.TextureComponent
import com.mo.stratego.model.player.Player

object Atlas  {
    val atlas : TextureAtlas

    init {
        atlas = TextureAtlas("pics.atlas")
    }

    fun getPieceTexture(rank : Rank,  owner : Player) : TextureComponent{
        val atlasRegion = atlas.findRegions("${rank.rank}_${rank.title.toLowerCase()}")
        val region = atlasRegion[1]
        return TextureComponent(region)
    }


}
