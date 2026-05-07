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

    require(bytes.size <= Short.MAX_VALUE) {
        "String too large: ${bytes.size} bytes"
    }

    writeShort(bytes.size.toShort())
    write(bytes)
}

fun Source.readSafeString(): String {
    val size = readShort().toInt() and 0xFFFF
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
    writeShort(enum.ordinal.toShort())
}

fun <E : Enum<E>> Source.readEnum(entries: EnumEntries<E>): E {
    val ordinal = readShort().toInt() and 0xFFFF

    return entries.getOrNull(ordinal)?.takeIf { it.ordinal == ordinal }
        ?: throw IndexOutOfBoundsException("Invalid enum ordinal: $ordinal")
}

inline fun <reified E : Enum<E>> Source.readEnum(): E = readEnum(enumEntries<E>())