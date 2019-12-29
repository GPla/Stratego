package com.mo.stratego.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.mo.stratego.ui.Atlas

/**
 * TODO
 */
object MarkdownParser {
    private val allowExtensions = arrayOf("md", "txt")
    private val sWidth: Float = Constants.screenWidth - 40f

    /**
     * Parses a markdown document to scene2D ui elements.
     * @param file File
     * @return Scene2D table of markdown content.
     */
    fun parseMarkdownToScene2D(file: FileHandle): Table {
        if (file.extension() !in allowExtensions)
            throw Exception("Unsupported extension.")

        val resultTable = Table()
        resultTable.align(Align.top)

        var contentBlock: Label? = null
        val content = file.readString().split('\n')
        for (line in content) {
            when {
                line.startsWith("# ")                               -> {
                    // header
                    contentBlock = null
                    val lbl = processHeader(line.substring(2), 0)
                    with(resultTable) {
                        row()
                        add(lbl).expandX().pad(30f, 50f, 0f, 10f)
                                .align(Align.left)
                    }
                }

                line.matches("""!\[\w*\]\(\w*.png\)\s""".toRegex()) -> {
                    // image
                    contentBlock = null
                    val path = line.substring(line.indexOf('(') + 1,
                                              line.indexOf(')'))
                    val tex = Texture(Gdx.files.internal(
                            file.parent().path() + "/" + path))
                    with(resultTable) {
                        row()
                        add(Image(tex)).width(sWidth)
                                .height(scaleImage(tex, sWidth))

                    }
                }

                line.equals('\n')                                   -> {
                    // ignore
                }

                else                                                -> {
                    // text
                    if (contentBlock == null) {
                        contentBlock = Label("", Atlas.uiSkin)
                        contentBlock.setWrap(true)

                        with(resultTable) {
                            row()
                            add(contentBlock).pad(20f).width(sWidth)
                        }
                    }
                    contentBlock.text.append("$line\n")
                }
            }
        }

        return resultTable
    }

    /**
     * TODO
     *
     * @param text
     * @param level
     * @return
     */
    private fun processHeader(text: String, level: Int): Label {
        val font = when (level) {
            0    -> Atlas.font48
            1    -> Atlas.font32
            else -> Atlas.font26
        }
        val style = Label.LabelStyle(font, Constants.YELLOW)
        return Label(text, style)
    }

    /**
     * TODO
     *
     * @param texture
     * @param width
     * @return
     */
    private fun scaleImage(texture: Texture, width: Float): Float {
        val scale = texture.width / texture.height.toFloat()
        return width * scale
    }
}