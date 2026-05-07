package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.*
import com.fantamomo.mapgit.core.util.Hash
import kotlinx.io.Sink
import kotlinx.io.Source

data class ChunkTree(
    val chunks: List<Pair<ChunkPos, Hash>>
) : StorableObject<ChunkTree> {
    override val readWriter = Companion

    companion object : StorableReadWriter<ChunkTree> {
        override val type: String = "chunk_tree"

        override fun read(source: Source): ChunkTree {
            val chunkCount = source.readInt()
            val chunks = List(chunkCount) {
                val chunkPos = source.readStorableObject(ChunkPos)
                val chunkHash = source.readHash()
                chunkPos to chunkHash
            }
            return ChunkTree(chunks)
        }

        override fun write(
            sink: Sink,
            obj: ChunkTree
        ) {
            val chunks = obj.chunks
            sink.writeInt(chunks.size)
            for (chunk in chunks) {
                sink.writeStorableObject(chunk.first)
                sink.writeHash(chunk.second)
            }
        }
    }
}