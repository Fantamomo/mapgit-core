package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter

data class BlockPos(
    val x: Int,
    val y: Int,
    val z: Int
) : StorableObject<BlockPos> {
    override val readWriter = Companion

    init {
        require(x in 0..15) { "x must be between 0 and 15" }
        require(y in 0..255) { "y must be between 0 and 15" }
        require(z in 0..15) { "z must be between 0 and 15" }
    }

    companion object : StorableReadWriter<BlockPos> {
        override val type: String = "block_pos"

        override fun read(buf: FriendlyByteBuf): BlockPos {
            val x = buf.readInt()
            val y = buf.readInt()
            val z = buf.readInt()
            return BlockPos(x, y, z)
        }

        override fun write(
            buf: FriendlyByteBuf,
            obj: BlockPos
        ) {
            buf.writeInt(obj.x)
            buf.writeInt(obj.y)
            buf.writeInt(obj.z)
        }
    }
}