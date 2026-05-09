package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.storage.readStorableObject
import com.fantamomo.mapgit.core.storage.writeStorableObject
import kotlinx.io.Sink
import kotlinx.io.Source
import java.util.*

class GlobalMetaDataSet(
    val data: SortedMap<Key, MetaData<*>> = TreeMap()
) : StorableObject<GlobalMetaDataSet> {
    override val readWriter = Companion

    constructor(data: Map<Key, MetaData<*>>) : this(data.toSortedMap())

    constructor(vararg data: Pair<Key, MetaData<*>>) : this(data.toMap(TreeMap()))

    companion object : StorableReadWriter<GlobalMetaDataSet> {
        override val type: String = "global-meta-data-set"

        override fun read(source: Source): GlobalMetaDataSet {
            val size = source.readInt()
            val data = TreeMap<Key, MetaData<*>>()
            repeat(size) {
                val key = source.readStorableObject(Key)
                val metaData = source.readStorableObject(MetaData.getReadWriter<Any>()) as MetaData<*>
                data[key] = metaData
            }
            return GlobalMetaDataSet(data)
        }

        override fun write(sink: Sink, obj: GlobalMetaDataSet) {
            val data = obj.data
            sink.writeInt(data.size)
            for ((key, metaData) in data) {
                sink.writeStorableObject(key)
                sink.writeStorableObject(metaData)
            }
        }
    }
}