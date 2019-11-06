package com.mo.stratego.model.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.mo.stratego.util.Constants

/**
 * Singleton Class that loads and renders the Map
 */
object GameMap {
    private val path : String = "maps/map2.tmx"

    private var map : TiledMap
    private val mapRenderer : TiledMapRenderer

    val width : Int     // width of loaded map
    val height : Int    // height of loaded map

    var engine : Engine? = null

    init {
        // load map and init renderer
        map = TmxMapLoader().load(path)
        mapRenderer = OrthogonalTiledMapRenderer(map, Constants.UNITSCALE)

        // get properties of map
        val props = map.properties
        val tilewidth = props.get("tilewidth", Int::class.java)
        val tileheight = props.get("tileheight", Int::class.java)
        width = scaleDimension(props.get("width", Int::class.java), tilewidth)
        height = scaleDimension(props.get("height", Int::class.java), tileheight)
    }

    /**
     * @return Scaled dimension in regard to the tiledimension and unitscale
     */
    private fun scaleDimension(dim : Int, tiledim: Int) =
            (dim * tiledim * Constants.UNITSCALE ).toInt()

    /**
     * Renders the map
     */
    fun render(camera : OrthographicCamera){
        mapRenderer.setView(camera)
        mapRenderer.render()
    }

}