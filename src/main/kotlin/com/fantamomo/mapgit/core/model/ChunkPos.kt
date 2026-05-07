package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter

data class ChunkPos(
    val x: Int,
    val y: Int,
    val z: Int
) : StorableObject<ChunkPos> {
    override val readWriter = Companion

    companion object : StorableReadWriter<ChunkPos> {
        val ZERO = ChunkPos(0, 0, 0)

        override val type: String = "chunk_pos"

        override fun read(buf: FriendlyByteBuf) = ChunkPos(
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        )

        override fun write(
            buf: FriendlyByteBuf,
            obj: ChunkPos
        ) {
            buf.writeInt(obj.x)
            buf.writeInt(obj.y)
            buf.writeInt(obj.z)
        }
    }
}