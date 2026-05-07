package com.fantamomo.mapgit.core.storage

import com.fantamomo.mapgit.core.util.Hash
import com.fantamomo.mapgit.core.util.Hashable
import io.netty.buffer.PooledByteBufAllocator

interface StorableObject<T : StorableObject<T>> : Hashable {
    val readWriter: StorableReadWriter<T>

    override fun hash(): Hash {
        val buf = PooledByteBufAllocator.DEFAULT.buffer()
        try {
            @Suppress("UNCHECKED_CAST")
            readWriter.write(FriendlyByteBuf(buf), this as T)
            return Hash.hash(buf)
        } finally {
            buf.release()
        }
    }
}