package com.mo.stratego.model.map

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.Disposable
import com.mo.stratego.util.Constants

/**
 * Singleton Class that loads and renders the Map
 */
object GameMap : Disposable {
    private lateinit var map: TiledMap
    private lateinit var mapRenderer: TiledMapRenderer

    //map properties
    var width: Int = 0    // width of loaded map
        private set
    var height: Int = 0  // height of loaded map
        private set
    var gridBottom: Int = 0 // number of tiles from bottom to grid
        private set
    var gridLeft: Int = 0  // number of tiles from left to grid
        private set
    var tilewidth: Int = 0
        private set
    var tileheight: Int = 0
        private set

    fun init() {
        // load map and init renderer
        map = TmxMapLoader().load(Constants.MAP_PATH)
        mapRenderer = OrthogonalTiledMapRenderer(map, Constants.UNITSCALE)

        // get properties of map
        val props = map.properties
        tilewidth = props.get("tilewidth", Int::class.java)
        tileheight = props.get("tileheight", Int::class.java)

        width = scale(props.get("width", Int::class.java), tilewidth)
        height = scale(props.get("height", Int::class.java), tileheight)

        // user defined properties
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

    /**
     * Disposes loaded resources.
     */
    override fun dispose() {
        map.dispose()
    }
}