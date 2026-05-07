package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.storage.readSafeString
import com.fantamomo.mapgit.core.storage.writeSafeString
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.readByteArray

data class Block(
    val type: String,
    val data: ByteArray
) : StorableObject<Block> {
    override val readWriter = Companion

    companion object : StorableReadWriter<Block> {
        override val type: String = "block"

        override fun read(source: Source): Block {
            val type = source.readSafeString()
            val dataLength = source.readInt()
            return Block(type, source.readByteArray(dataLength))
        }

        override fun write(
            sink: Sink,
            obj: Block
        ) {
            sink.writeSafeString(obj.type)
            sink.writeInt(obj.data.size)
            sink.write(obj.data)
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