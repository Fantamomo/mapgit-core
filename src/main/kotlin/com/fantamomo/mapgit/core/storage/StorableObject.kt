package com.fantamomo.mapgit.core.storage

import com.fantamomo.mapgit.core.util.Hash
import com.fantamomo.mapgit.core.util.Hashable
import kotlinx.io.Buffer

interface StorableObject<T : StorableObject<T>> : Hashable {
    val readWriter: StorableReadWriter<T>

    override fun hash(): Hash {
        val buf = Buffer()
        @Suppress("UNCHECKED_CAST")
        readWriter.write(buf, this as T)
        return Hash.hash(buf)
    }
}