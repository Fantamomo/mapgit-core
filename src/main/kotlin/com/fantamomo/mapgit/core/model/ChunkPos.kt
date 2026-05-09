package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import kotlinx.io.Sink
import kotlinx.io.Source

data class ChunkPos(
    val x: Int,
    val y: Int,
    val z: Int
) : StorableObject<ChunkPos>, Comparable<ChunkPos> {
    override val readWriter = Companion

    override fun compareTo(other: ChunkPos): Int {
        val xComparison = x.compareTo(other.x)
        if (xComparison != 0) return xComparison
        val yComparison = y.compareTo(other.y)
        if (yComparison != 0) return yComparison
        return z.compareTo(other.z)
    }

    companion object : StorableReadWriter<ChunkPos> {
        val ZERO = ChunkPos(0, 0, 0)

        override val type: String = "chunk_pos"

        override fun read(source: Source) = ChunkPos(
            source.readInt(),
            source.readInt(),
            source.readInt()
        )

        override fun write(
            sink: Sink,
            obj: ChunkPos
        ) {
            sink.writeInt(obj.x)
            sink.writeInt(obj.y)
            sink.writeInt(obj.z)
        }
    }
}