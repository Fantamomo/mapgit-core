package com.fantamomo.mapgit.core.protocol.body

import com.fantamomo.mapgit.core.registry.StorableObjectRegistry
import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import com.fantamomo.mapgit.core.storage.StorableObject

class MapGitObjectBody<T : StorableObject<T>>(val obj: T) : Body {
    override val bodyDefinition = getDefinition<T>()

    @Suppress("UNCHECKED_CAST")
    private constructor(obj: StorableObject<*>) : this(obj as T)

    companion object : BodyDefinition<MapGitObjectBody<*>> {
        override val contentType: String = "application/x-mapgit-object"

        @Suppress("UNCHECKED_CAST")
        fun <T : StorableObject<T>> getDefinition() = this as BodyDefinition<MapGitObjectBody<T>>

        override fun serialize(buf: FriendlyByteBuf, body: MapGitObjectBody<*>) {
            buf.writeString(body.obj.readWriter.type)
            @Suppress("UNCHECKED_CAST", "UPPER_BOUND_VIOLATED_IN_TYPE_OPERATOR_OR_PARAMETER_BOUNDS_WARNING")
            (body.obj as StorableObject<Any>).readWriter.write(buf, body.obj)
        }

        @Suppress("UNCHECKED_CAST")
        override fun deserialize(buf: FriendlyByteBuf): MapGitObjectBody<*> {
            val type = buf.readString()
            val readWriter =
                StorableObjectRegistry.objects.values.find { it.type == type } ?: error("Unknown type $type")
            val obj = readWriter.read(buf)
            return MapGitObjectBody(obj)
        }
    }
}