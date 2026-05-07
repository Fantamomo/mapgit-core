package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.util.Hash

data class ChunkTree(
    val chunks: List<Pair<ChunkPos, Hash>>
) : StorableObject<ChunkTree> {
    override val readWriter = Companion

    companion object : StorableReadWriter<ChunkTree> {
        override val type: String = "chunk_tree"

        override fun read(buf: FriendlyByteBuf): ChunkTree {
            val chunkCount = buf.readInt()
            val chunks = List(chunkCount) {
                val chunkPos = buf.readStorableObject(ChunkPos)
                val chunkHash = buf.readHash()
                chunkPos to chunkHash
            }
            return ChunkTree(chunks)
        }

        override fun write(
            buf: FriendlyByteBuf,
            obj: ChunkTree
        ) {
            val chunks = obj.chunks
            buf.writeInt(chunks.size)
            for (chunk in chunks) {
                buf.writeStorableObject(chunk.first)
                buf.writeHash(chunk.second)
            }
        }
    }
}