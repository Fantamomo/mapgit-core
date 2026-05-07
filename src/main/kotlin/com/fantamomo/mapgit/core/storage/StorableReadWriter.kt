package com.fantamomo.mapgit.core.storage

interface StorableReadWriter<T : StorableObject<T>> : StorableWriter<T>, StorableReader<T>{
    val type: String

    override fun read(buf: FriendlyByteBuf): T
    override fun write(buf: FriendlyByteBuf, obj: T)
}