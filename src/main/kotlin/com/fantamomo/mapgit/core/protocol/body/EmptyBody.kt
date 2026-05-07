package com.fantamomo.mapgit.core.protocol.body

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf

/**
 * A singleton object representing an empty body for a request or response.
 *
 * This implementation of [Body] and [BodyDefinition] is used when no body content is required.
 * It represents an empty body that serializes to an empty byte array and deserializes back to itself.
 *
 * The implementation should handle this object specifically
 */
data object EmptyBody : Body, BodyDefinition<EmptyBody> {
    override val bodyDefinition = this
    @Deprecated("EmptyBody should be handled specially, this properties should not be used directly.")
    override val contentType: String = ""

    @Deprecated("EmptyBody should be handled specially, this method should not be used directly.")
    override fun serialize(buf: FriendlyByteBuf, body: EmptyBody) {}

    @Deprecated("EmptyBody should be handled specially, this method should not be used directly.")
    override fun deserialize(buf: FriendlyByteBuf): EmptyBody = this
}