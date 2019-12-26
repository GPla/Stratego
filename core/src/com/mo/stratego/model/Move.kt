package com.mo.stratego.model

import com.badlogic.gdx.math.GridPoint2
import kotlinx.serialization.*
import kotlinx.serialization.internal.IntArraySerializer
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.internal.StringSerializer


/**
 * Class that represents a move on the playing field.
 *
 * @property position Position of the [Piece]
 * @property move Move of the [Piece]
 */
@Serializable
class Move(val position: GridPoint2, val move: GridPoint2) {
    private val className = Move::class.java.name

    /**
     * Override of == operator.
     * @param other Any?
     * @return True if equal.
     */
    override fun equals(other: Any?): Boolean {
        return other?.let {
            if (it !is Move)
                return false

            if (it.move == this.move && it.position == this.position)
                return true
            false
        } ?: false
    }

    /**
     * Custom serializer for [Move] class.
     * Adapted from https://github.com/Kotlin/kotlinx.serialization/blob/
     * master/docs/custom_serializers.md
     */
    @Serializer(forClass = Move::class)
    companion object : KSerializer<Move> {

        override val descriptor: SerialDescriptor =
                object : SerialClassDescImpl("Move") {
                    init {
                        addElement("position") // index 0
                        addElement("move") // index 1
                        addElement("className") // index 2
                    }
                }


        override fun serialize(encoder: Encoder, obj: Move) {
            val compositeOutput = encoder.beginStructure(descriptor)
            with(compositeOutput) {
                encodeSerializableElement(descriptor, 0, IntArraySerializer,
                                          pointToArray(obj.position))
                encodeSerializableElement(descriptor, 1, IntArraySerializer,
                                          pointToArray(obj.move))
                encodeSerializableElement(descriptor, 2, StringSerializer,
                                          Move::class.java.name)
                endStructure(descriptor)
            }
        }

        override fun deserialize(decoder: Decoder): Move {
            val dec: CompositeDecoder = decoder.beginStructure(descriptor)
            var pos: GridPoint2? = null
            var mov: GridPoint2? = null
            loop@ while (true) {
                when (val i = dec.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0                          ->
                        pos = arrayToPoint(
                                dec.decodeSerializableElement(descriptor, 0,
                                                              IntArraySerializer))
                    1                          ->
                        mov = arrayToPoint(
                                dec.decodeSerializableElement(descriptor, 1,
                                                              IntArraySerializer))
                    2                          ->
                        // needs to be decoded otherwise endStructure throws an error
                        dec.decodeStringElement(descriptor, 2)
                    else                       ->
                        throw SerializationException("Unknown index $i")
                }
            }
            dec.endStructure(descriptor)
            return Move(
                    pos ?: throw MissingFieldException("pos"),
                    mov ?: throw MissingFieldException("mov")
                       )
        }

        /**
         * Convert [GridPoint2] to [IntArray].
         * @param point2
         * @return Converted [IntArray] of point.
         */
        private fun pointToArray(point2: GridPoint2): IntArray {
            return intArrayOf(point2.x, point2.y)
        }

        /**
         * Convert [IntArray] to [GridPoint2].
         * @param arr
         * @return Converted [GridPoint2] of array.
         */
        private fun arrayToPoint(arr: IntArray): GridPoint2 {
            if (arr.size < 2)
                throw  Exception("Invalid Input.")
            return GridPoint2(arr[0], arr[1])
        }
    }
}