package com.fantamomo.mapgit.core.protocol.body

import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KClass

/**
 * Base class for serializable body implementations in the protocol system.
 *
 * This class provides a foundation for representing request or response bodies that can be
 * serialized and deserialized for communication.
 *
 * @param B The specific type of the body subclass implementing [SerializableBody].
 * This allows type-safe access to body definitions when used as a base class.
 *
 * Features:
 * - Implements [Body] and provides access to a [BodyDefinition] instance for serialization behavior.
 * - Includes the ability to define specific serialization paths for subclasses using the
 *   [SerializableBodyPath] annotation.
 *
 * Serialization and Deserialization:
 * This class itself does not natively support serialization or deserialization. Instead, subclasses
 * and their companion objects must define the appropriate serialization and deserialization logic in
 * their respective [BodyDefinition] implementations.
 */
@Serializable
open class SerializableBody<B : SerializableBody<B>> : Body {
    @Transient
    @Suppress("UNCHECKED_CAST")
    override val bodyDefinition: BodyDefinition<B> = definition()

    /**
     * Annotation used to define a specific field path to serialize when a subclass of [SerializableBody]
     * contains a single property. This annotation helps avoid unnecessary JSON output wrapping by
     * serializing only the specified property directly.
     *
     * For example, if a subclass of [SerializableBody] is annotated with `@SerializableBodyPath("data")`,
     * the serialization framework would directly serialize the "data" property without wrapping it.
     *
     * Constraints:
     * - This annotation is only valid when applied directly to a subclass of [SerializableBody].
     *
     * Parameters:
     * @property path The specific field path within the body to serialize.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class SerializableBodyPath(val path: String)

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