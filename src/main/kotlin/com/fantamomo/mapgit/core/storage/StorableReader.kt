package com.fantamomo.mapgit.core.storage

interface StorableReader<out T : StorableObject<@UnsafeVariance T>> {
    fun read(buf: FriendlyByteBuf): T
}