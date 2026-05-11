package com.fantamomo.mapgit.core.protocol.body

import com.fantamomo.mapgit.core.registry.StorableObjectRegistry
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.readSafeString
import com.fantamomo.mapgit.core.storage.writeSafeString
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlin.reflect.KClass

class MapGitObjectBody<T : StorableObject<T>>(val obj: T) : Body {
    override val bodyDefinition = getDefinition<T>()

    @Suppress("UNCHECKED_CAST")
    constructor(obj: StorableObject<*>, @Suppress("unused") ignore: Unit = Unit) : this(obj as T)

    companion object : BodyDefinition<MapGitObjectBody<*>> {
        override val bodyClass: KClass<MapGitObjectBody<*>> = MapGitObjectBody::class
        override val contentType: String = "application/x-mapgit-object"

        @Suppress("UNCHECKED_CAST")
        fun <T : StorableObject<T>> getDefinition() = this as BodyDefinition<MapGitObjectBody<T>>

        override fun serialize(sink: Sink, body: MapGitObjectBody<*>) {
            sink.writeSafeString(body.obj.readWriter.type)
            @Suppress("UNCHECKED_CAST", "UPPER_BOUND_VIOLATED_IN_TYPE_OPERATOR_OR_PARAMETER_BOUNDS_WARNING")
            (body.obj as StorableObject<Any>).readWriter.write(sink, body.obj)
        }

        @Suppress("UNCHECKED_CAST")
        override fun deserialize(source: Source): MapGitObjectBody<*> {
            val type = source.readSafeString()
            val readWriter =
                StorableObjectRegistry.objects.values.find { it.type == type } ?: error("Unknown type $type")
            val obj = readWriter.read(source)
            return MapGitObjectBody(obj)
        }
    }
}