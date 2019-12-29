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
 * Class to parse an markdown (.md) file to a scene2d representation.
 */
object MarkdownParser {
    private val allowExtensions = arrayOf("md", "txt")
    private val sWidth: Float = Constants.SCREEN_WIDTH - 40f
    private val lineBreaks = arrayOf("\n", "\r", "\r\n", "")

    /**
     * Parses a markdown document to scene2D ui elements.
     * It can parse 3 level of headers, images and text.
     * @param file File
     * @return Scene2D table of markdown content.
     */
    fun parseMarkdownToScene2D(file: FileHandle): Table {
        if (file.extension() !in allowExtensions)
            throw Exception("Unsupported extension.")

        val resultTable = Table()
        resultTable.align(Align.top)
        resultTable.width = Constants.SCREEN_WIDTH

        // parent folder
        val folder = file.parent().path()

        // block of text, will be divided into sections by headers and images
        var contentBlock: Label? = null
        val content = file.readString("UTF-8").split('\n')
        for (line in content) {
            when {
                // image header
                line.matches("""#+.*!\[[^\s]*\]\([^\s]+\.png\)\s*""".toRegex())
                -> {
                    contentBlock = null
                    // use inner table to avoid multiple columns in result table
                    val innerTable = Table()
                    with(innerTable) {
                        add(processHeader(line.substringBefore("!")))
                                .pad(10f, 0f, 0f, 10f)
                        add(processImage(line.substringAfter("!"), folder))
                    }

                    with(resultTable) {
                        row()
                        add(innerTable).align(Align.left)
                    }
                }

                // header
                line.startsWith("#") -> {
                    contentBlock = null
                    val lbl = processHeader(line)
                    with(resultTable) {
                        row()
                        add(lbl).pad(10f, 0f, 0f, 20f).align(Align.left)
                    }
                }

                // image
                line.matches("""!\[[^\s]*\]\([^\s]+\.png\)\s*""".toRegex()) -> {
                    contentBlock = null

                    val img = processImage(line, folder)

                    with(resultTable) {
                        row()
                        add(img).width(img.width).height(img.height)
                                .padBottom(10f)
                    }
                }

                // text
                line !in lineBreaks -> {
                    // start new section
                    if (contentBlock == null) {
                        contentBlock = Label(line, Atlas.uiSkin)
                        contentBlock.setWrap(true)

                        with(resultTable) {
                            row()
                            add(contentBlock).pad(20f).width(sWidth)
                        }
                    } else {
                        contentBlock.text.appendLine("$line")
                    }
                }
            }
        }

        return resultTable
    }

    /**
     * @param line Line
     * @return Label of the text with the appropriate font size.
     */
    private fun processHeader(line: String): Label {
        val level = line.lastIndexOf('#') // header level
        val text = line.substring(level + 1) // extract text
        val font = when (level) {
            0 -> Atlas.font48
            1 -> Atlas.font32
            else -> Atlas.font26
        }
        val style = Label.LabelStyle(font, Constants.YELLOW)
        return Label(text, style)
    }

    /**
     * Processes an image and scales it to fit the screen.
     * @param line Line
     * @param prefix Path prefix
     * @return The loaded image.
     */
    private fun processImage(line: String, prefix: String): Image {
        // extract path
        val path = line.substring(line.indexOf('(') + 1, line.indexOf(')'))

        // load texture
        val texture = Texture(Gdx.files.internal("$prefix/$path"))

        return scaleImage(Image(texture))
    }

    /**
     * Scales the image dimensions, if the image exceeds the screen width,
     * in same aspect ratio as the original.
     * @param img Image
     * @return Image for chaining.
     */
    private fun scaleImage(img: Image): Image {
        if (img.width < sWidth)
            return img

        val scale = img.height / img.width
        img.width = if (img.width >= sWidth) sWidth else img.width
        img.height = img.width * scale
        return img
    }
}