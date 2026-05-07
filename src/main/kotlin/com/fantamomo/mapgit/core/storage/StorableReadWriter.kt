package com.fantamomo.mapgit.core.storage

interface StorableReadWriter<T : StorableObject<T>> {
    val type: String

    fun read(buf: FriendlyByteBuf): T
    fun write(buf: FriendlyByteBuf, obj: T)
}