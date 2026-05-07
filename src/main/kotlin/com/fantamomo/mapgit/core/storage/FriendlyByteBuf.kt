package com.fantamomo.mapgit.core.storage

import com.fantamomo.mapgit.core.util.Hash
import io.netty.buffer.ByteBuf
import io.netty.buffer.WrappedByteBuf
import java.util.*
import kotlin.enums.EnumEntries

class FriendlyByteBuf(buf: ByteBuf) : WrappedByteBuf(buf) {
    fun writeUUID(uuid: UUID): FriendlyByteBuf {
        ensureWritable(16)
        writeLong(uuid.mostSignificantBits)
        writeLong(uuid.leastSignificantBits)
        return this
    }

    fun readUUID(): UUID {
        ensureReadable(16)
        return UUID(readLong(), readLong())
    }

    fun writeHash(hash: Hash): FriendlyByteBuf {
        ensureWritable(32)
        hash.writeTo(this)
        return this
    }

    fun readHash(): Hash {
        ensureReadable(32)
        return Hash.readFromByteBuf(this)
    }

    fun <T : StorableObject<T>> writeStorableObject(obj: T): FriendlyByteBuf {
        obj.readWriter.write(this, obj)
        return this
    }

    fun <T : StorableObject<T>> readStorableObject(readWriter: StorableReadWriter<T>) = readWriter.read(this)

    fun writeString(str: String): FriendlyByteBuf {
        val bytes = str.toByteArray(Charsets.UTF_8)
        ensureWritable(bytes.size + 2)
        writeShort(bytes.size)
        writeBytes(bytes)
        return this
    }

    fun readString(): String {
        val size = readShort().toInt()
        ensureReadable(size)
        val bytes = ByteArray(size)
        readBytes(bytes)
        return String(bytes, Charsets.UTF_8)
    }

    fun writeEnum(enum: Enum<*>): FriendlyByteBuf {
        ensureWritable(2)
        writeShort(enum.ordinal)
        return this
    }

    fun <E : Enum<E>> readEnum(entries: EnumEntries<E>): E {
        val ordinal = readShort().toInt()
        return entries.getOrNull(ordinal)?.takeIf { it.ordinal == ordinal } ?: throw IndexOutOfBoundsException("Invalid enum ordinal: $ordinal")
    }

    fun ensureReadable(count: Int) = require(isReadable(count)) { "Not enough bytes to read: required $count, but only ${readableBytes()} available" }
}