package com.fantamomo.mapgit.core.protocol.body

import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KClass

/**
 * Represents a serializable body type that can be extended for various forms of request/response bodies.
 *
 * This class implements the [Body] interface and provides a generic structure for defining serializable
 * payloads. It is primarily designed to be a base class for other specific types of bodies.
 *
 * @param B Represents the type extending this base class.
 * @property serializableBodyPath Defines the specific field path to serialize when the body contains
 * a single property. This avoids unnecessary wrapping in JSON output. For example, if set to "data",
 * the serialization framework would directly serialize the "data" property instead of wrapping it
 * in an object.
 *
 * @author Fantamomo
 * @since 1.0-SNAPSHOT
 */
@Serializable
open class SerializableBody<B : SerializableBody<B>>(
    @Transient
    val serializableBodyPath: String? = null
) : Body {
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