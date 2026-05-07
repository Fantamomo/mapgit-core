package com.fantamomo.mapgit.core.storage

import kotlinx.io.Sink
import kotlinx.io.Source

interface StorableReadWriter<T : StorableObject<T>> : StorableWriter<T>, StorableReader<T>{
    val type: String

    override fun read(source: Source): T
    override fun write(sink: Sink, obj: T)
}