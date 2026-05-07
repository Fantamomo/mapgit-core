package com.fantamomo.mapgit.core.protocol.body

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf

interface BodyDefinition<B : Body> {
    val contentType: String

    fun serialize(buf: FriendlyByteBuf, body: B)

    fun deserialize(buf: FriendlyByteBuf): B
}