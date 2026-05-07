package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter

data class Block(
    val type: String,
    val data: ByteArray
) : StorableObject<Block> {
    override val readWriter = Companion

    companion object : StorableReadWriter<Block> {
        override val type: String = "block"

        override fun read(buf: FriendlyByteBuf): Block {
            val type = buf.readString()
            val dataLength = buf.readInt()
            val data = ByteArray(dataLength)
            buf.readBytes(data)
            return Block(type, data)
        }

        override fun write(
            buf: FriendlyByteBuf,
            obj: Block
        ) {
            buf.writeString(obj.type)
            buf.writeInt(obj.data.size)
            buf.writeBytes(obj.data)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Block

        if (type != other.type) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}