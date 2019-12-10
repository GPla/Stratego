package com.mo.stratego.model.map

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
    private const val path: String = "maps/map2.tmx"

    private var map: TiledMap
    private val mapRenderer: TiledMapRenderer

    //map properties
    val width: Int      // width of loaded map
    val height: Int     // height of loaded map
    val gridBottom: Int // number of tiles from bottom to grid
    val gridLeft: Int   // number of tiles from left to grid
    val tilewidth: Int
    val tileheight: Int

    init {
        // load map and init renderer
        map = TmxMapLoader().load(path)
        mapRenderer = OrthogonalTiledMapRenderer(map, Constants.UNITSCALE)

        // get properties of map
        val props = map.properties
        tilewidth = props.get("tilewidth", Int::class.java)
        tileheight = props.get("tileheight", Int::class.java)

        width = scale(props.get("width", Int::class.java), tilewidth)
        height = scale(props.get("height", Int::class.java), tileheight)

        gridBottom = if (props.containsKey("grid_bottom"))
            scale(props.get("grid_bottom", Int::class.java), tileheight) else 0
        gridLeft = if (props.containsKey("grid_left"))
            scale(props.get("grid_left", Int::class.java), tilewidth) else 0

    }

    /**
     * @param dim Int
     * @param tiledim Int
     * @return Scales input dimension in regard to tile dimension and unit scale
     */
    fun scale(dim: Int, tiledim: Int) =
            (dim * tiledim * Constants.UNITSCALE).toInt()

    /**
     * Renders the map
     */
    fun render(camera: OrthographicCamera) {
        mapRenderer.setView(camera)
        mapRenderer.render()
    }

}