package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.storage.readSafeString
import com.fantamomo.mapgit.core.storage.writeSafeString
import kotlinx.io.Sink
import kotlinx.io.Source

sealed class MetaData<T> private constructor(val value: T, private val type: Byte) : StorableObject<MetaData<T>> {

    protected abstract fun writeTo(sink: Sink)

    override val readWriter: StorableReadWriter<MetaData<T>> = getReadWriter()

    private class BooleanMetaData(value: Boolean) : MetaData<Boolean>(value, 1) {
        override fun writeTo(sink: Sink) = sink.writeByte(if (value) 1 else 0)
    }

    private class StringMetaData(value: String) : MetaData<String>(value, 2) {
        override fun writeTo(sink: Sink) = sink.writeSafeString(value)
    }

    private class IntMetaData(value: Int) : MetaData<Int>(value, 3) {
        override fun writeTo(sink: Sink) = sink.writeInt(value)
    }

    private class ReadWriter<T> : StorableReadWriter<MetaData<T>> {
        override val type: String = "metadata"

        @Suppress("UNCHECKED_CAST")
        override fun read(source: Source): MetaData<T> {
            val type = source.readByte()
            return when (type.toInt()) {
                1 -> BooleanMetaData(source.readByte() != 0.toByte())
                2 -> StringMetaData(source.readSafeString())
                3 -> IntMetaData(source.readInt())
                else -> throw IllegalStateException("Unsupported meta data type: $type")
            } as MetaData<T>
        }

        override fun write(sink: Sink, obj: MetaData<T>) {
            sink.writeByte(obj.type)
            obj.writeTo(sink)
        }
    }

    companion object {
        private val readWriter = ReadWriter<Any>()
        @Suppress("UNCHECKED_CAST")
        fun <T> getReadWriter(): StorableReadWriter<MetaData<T>> = readWriter as StorableReadWriter<MetaData<T>>

        fun boolean(value: Boolean): MetaData<Boolean> = BooleanMetaData(value)
        fun string(value: String): MetaData<String> = StringMetaData(value)
        fun int(value: Int): MetaData<Int> = IntMetaData(value)
    }
}