package com.fantamomo.mapgit.core.protocol.body

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class SerializableBody<B : SerializableBody<B>> : Body {
    @Transient
    @Suppress("UNCHECKED_CAST")
    override val bodyDefinition: BodyDefinition<B> = definition()

    // Since SerializableBody is meant to be a base class for other bodies, it does not implement serialization/deserialization itself.
    // the implementation should handle this object specifically
    companion object : BodyDefinition<SerializableBody<*>> {
        fun <B : SerializableBody<B>> definition(): BodyDefinition<B> {
            @Suppress("UNCHECKED_CAST")
            return this as BodyDefinition<B>
        }

        override val contentType: String = "application/json"

        @Deprecated("Serialization is not natively supported for SerializableBody")
        override fun serialize(buf: FriendlyByteBuf, body: SerializableBody<*>) {
            throw UnsupportedOperationException(
                "Serialization is not natively supported for SerializableBody"
            )
        }

        @Deprecated("Deserialization is not natively supported for SerializableBody")
        override fun deserialize(buf: FriendlyByteBuf): SerializableBody<*> {
            throw UnsupportedOperationException(
                "Deserialization is not natively supported for SerializableBody"
            )
        }
    }
}