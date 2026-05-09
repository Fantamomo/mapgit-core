package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.storage.readStorableObject
import com.fantamomo.mapgit.core.storage.writeStorableObject
import kotlinx.io.Sink
import kotlinx.io.Source
import java.util.*

class BlockMetaDataSet(
    val data: SortedMap<Key, MetaData<*>>
) : StorableObject<BlockMetaDataSet> {
    override val readWriter = Companion

    constructor(data: Map<Key, MetaData<*>>) : this(data.toSortedMap())

    constructor(vararg data: Pair<Key, MetaData<*>>) : this(data.toMap(TreeMap()))

    data class Key(
        val key: com.fantamomo.mapgit.core.model.Key,
        val chunk: ChunkPos,
        val block: BlockPos
    ) : StorableObject<Key>, Comparable<Key> {
        override val readWriter = Companion

        override fun compareTo(other: Key): Int {
            val keyComparison = key.compareTo(other.key)
            if (keyComparison != 0) return keyComparison
            val chunkComparison = chunk.compareTo(other.chunk)
            if (chunkComparison != 0) return chunkComparison
            return block.compareTo(other.block)
        }

        companion object : StorableReadWriter<Key> {
            override val type: String = "key"
            override fun read(source: Source): Key {
                val key = source.readStorableObject(com.fantamomo.mapgit.core.model.Key)
                val chunk = source.readStorableObject(ChunkPos)
                val block = source.readStorableObject(BlockPos)
                return Key(key, chunk, block)
            }

            override fun write(sink: Sink, obj: Key) {
                sink.writeStorableObject(obj.key)
                sink.writeStorableObject(obj.chunk)
                sink.writeStorableObject(obj.block)
            }
        }
    }

    companion object : StorableReadWriter<BlockMetaDataSet> {
        override val type: String = "block-meta-data-set"

        override fun read(source: Source): BlockMetaDataSet {
            val size = source.readInt()
            val data = mutableMapOf<Key, MetaData<*>>()
            repeat(size) {
                val key = source.readStorableObject(Key)
                val metaData = source.readStorableObject(MetaData.getReadWriter<Any>()) as MetaData<*>
                data[key] = metaData
            }
            return BlockMetaDataSet(data)
        }

        override fun write(sink: Sink, obj: BlockMetaDataSet) {
            val data = obj.data
            sink.writeInt(data.size)
            for ((key, metaData) in data) {
                sink.writeStorableObject(key)
                sink.writeStorableObject(metaData)
            }
        }
    }
}