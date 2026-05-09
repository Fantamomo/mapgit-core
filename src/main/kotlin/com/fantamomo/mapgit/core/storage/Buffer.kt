package com.fantamomo.mapgit.core.storage

import com.fantamomo.mapgit.core.util.Hash
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.readString
import java.util.*
import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

fun Sink.writeSafeString(str: String) {
    val bytes = str.encodeToByteArray()

    require(bytes.size <= Int.MAX_VALUE) {
        "String too large: ${bytes.size} bytes, max ${Int.MAX_VALUE}"
    }

    writeVarInt(bytes.size)
    write(bytes)
}

fun Source.readSafeString(): String {
    val size = readVarInt()
    return readString(size.toLong())
}

fun Sink.writeUUID(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
}

fun Source.readUUID() = UUID(readLong(), readLong())

fun Sink.writeHash(hash: Hash) {
    hash.writeTo(this)
}

fun Source.readHash() = Hash.readFromBuffer(this)

fun <T : StorableObject<T>> Sink.writeStorableObject(obj: T) {
    obj.readWriter.write(this, obj)
}

fun <T : StorableObject<T>> Source.readStorableObject(
    readWriter: StorableReadWriter<T>
): T = readWriter.read(this)

fun Sink.writeEnum(enum: Enum<*>) {
    writeVarInt(enum.ordinal)
}

fun Sink.writeEnumName(enum: Enum<*>) {
    writeSafeString(enum.name)
}

fun <E : Enum<E>> Source.readEnum(entries: EnumEntries<E>): E {
    val ordinal = readVarInt()

    return entries.getOrNull(ordinal)?.takeIf { it.ordinal == ordinal }
        ?: throw IndexOutOfBoundsException("Invalid enum ordinal: $ordinal")
}

inline fun <reified E : Enum<E>> Source.readEnum(): E = readEnum(enumEntries<E>())

fun <E : Enum<E>> Source.readEnumName(entries: EnumEntries<E>): E {
    val name = readSafeString()
    return entries.firstOrNull { it.name == name } ?: throw IllegalArgumentException("Invalid enum name: $name")
}

inline fun <reified E : Enum<E>> Source.readEnumName(): E = readEnumName(enumEntries<E>())

fun Sink.writeVarInt(value: Int) {
    var v = value

    while ((v and 0x7F.inv()) != 0) {
        writeByte(((v and 0x7F) or 0x80).toByte())
        v = v ushr 7
    }

    writeByte(v.toByte())
}

fun Source.readVarInt(): Int {
    var result = 0
    var shift = 0

    while (true) {
        require(shift < 32) { "VarInt too long" }

        val byte = readByte().toInt()
        result = result or ((byte and 0x7F) shl shift)

        if ((byte and 0x80) == 0) {
            return result
        }

        shift += 7
    }
}

fun Sink.writeVarLong(value: Long) {
    var v = value

    while ((v and 0x7FL.inv()) != 0L) {
        writeByte(((v and 0x7F) or 0x80).toByte())
        v = v ushr 7
    }

    writeByte(v.toByte())
}

fun Source.readVarLong(): Long {
    var result = 0L
    var shift = 0

    while (true) {
        require(shift < 64) { "VarLong too long" }

        val byte = readByte().toLong()
        result = result or ((byte and 0x7F) shl shift)

        if ((byte and 0x80) == 0L) {
            return result
        }

        shift += 7
    }
}