package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import kotlinx.io.Sink
import kotlinx.io.Source

data class BlockPos(
    val x: Int,
    val y: Int,
    val z: Int
) : StorableObject<BlockPos>, Comparable<BlockPos> {

    override val readWriter = Companion

    init {
        require(x in 0..15) { "x must be between 0 and 15, was $x" }
        require(y in 0..15) { "y must be between 0 and 15, was $y" }
        require(z in 0..15) { "z must be between 0 and 15, was $z" }
    }

    override fun compareTo(other: BlockPos): Int {
        val xComparison = x.compareTo(other.x)
        if (xComparison != 0) return xComparison
        val yComparison = y.compareTo(other.y)
        if (yComparison != 0) return yComparison
        return z.compareTo(other.z)
    }

    companion object : StorableReadWriter<BlockPos> {

        override val type: String = "block_pos"

        override fun read(source: Source): BlockPos {
            val packed = source.readShort().toInt() and 0xFFFF

            return BlockPos(
                x = (packed shr 8) and 0xF,
                y = (packed shr 4) and 0xF,
                z = packed and 0xF
            )
        }

        override fun write(
            sink: Sink,
            obj: BlockPos
        ) {
            val packed =
                ((obj.x and 0xF) shl 8) or
                        ((obj.y and 0xF) shl 4) or
                        (obj.z and 0xF)

            sink.writeShort(packed.toShort())
        }
    }
}