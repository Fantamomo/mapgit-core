package com.fantamomo.mapgit.core.storage

interface StorableWriter<in T : StorableObject<@UnsafeVariance T>> {
    fun write(buf: FriendlyByteBuf, obj: T)
}