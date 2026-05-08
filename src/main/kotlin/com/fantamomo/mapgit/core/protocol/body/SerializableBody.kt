package com.fantamomo.mapgit.core.protocol.body

import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KClass

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

        override val bodyClass: KClass<SerializableBody<*>> = SerializableBody::class

        override val contentType: String = "application/json"

        @Deprecated("Serialization is not natively supported for SerializableBody")
        override fun serialize(sink: Sink, body: SerializableBody<*>) {
            throw UnsupportedOperationException(
                "Serialization is not natively supported for SerializableBody"
            )
        }

        @Deprecated("Deserialization is not natively supported for SerializableBody")
        override fun deserialize(source: Source): SerializableBody<*> {
            throw UnsupportedOperationException(
                "Deserialization is not natively supported for SerializableBody"
            )
        }
    }
}