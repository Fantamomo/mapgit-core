package com.fantamomo.mapgit.core.protocol.client

import com.fantamomo.mapgit.core.protocol.body.Body
import com.fantamomo.mapgit.core.protocol.body.BodyDefinition
import com.fantamomo.mapgit.core.protocol.body.MapGitObjectBody
import com.fantamomo.mapgit.core.protocol.respond.Respond
import com.fantamomo.mapgit.core.protocol.respond.RespondDefinition
import com.fantamomo.mapgit.core.registry.StorableObjectRegistry
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.readSafeString
import com.fantamomo.mapgit.core.storage.writeSafeString
import kotlinx.io.Sink
import kotlinx.io.Source

class GetObjectPatchRespond<T : StorableObject<T>>(
    override val body: MapGitObjectBody<T>,
) : Respond<MapGitObjectBody<T>> {

    @Suppress("UNCHECKED_CAST")
    private constructor(
        body: MapGitObjectBody<*>,
        @Suppress("unused") ignore: Unit = Unit
    ) : this(body as MapGitObjectBody<T>)

    class MapGitObjectPatchBody(val objects: List<StorableObject<*>>) : Body {
        override val bodyDefinition = Companion

        companion object : BodyDefinition<MapGitObjectPatchBody> {
            override val contentType: String = "application/x-mapgit-object-patch"

            override fun serialize(
                sink: Sink,
                body: MapGitObjectPatchBody
            ) {
                sink.writeInt(body.objects.size)
                body.objects.forEach { obj ->
                    sink.writeSafeString(obj.readWriter.type)
                    @Suppress("UNCHECKED_CAST", "UPPER_BOUND_VIOLATED_IN_TYPE_OPERATOR_OR_PARAMETER_BOUNDS_WARNING")
                    (obj as StorableObject<Any>).readWriter.write(sink, obj)
                }
            }

            override fun deserialize(source: Source): MapGitObjectPatchBody {
                val size = source.readInt()
                val objects = List(size) {
                    val type = source.readSafeString()
                    val readWriter =
                        StorableObjectRegistry.objects.values.find { it.type == type } ?: error("Unknown type $type")
                    readWriter.read(source)
                }
                return MapGitObjectPatchBody(objects)
            }
        }
    }

    companion object : RespondDefinition<Respond<MapGitObjectBody<*>>, MapGitObjectBody<*>> {
        override val bodyDefinition: BodyDefinition<MapGitObjectBody<*>> = MapGitObjectBody

        override fun build(body: MapGitObjectBody<*>): Respond<MapGitObjectBody<*>> {
            @Suppress("UNCHECKED_CAST")
            return GetObjectPatchRespond(body, Unit) as Respond<MapGitObjectBody<*>>
        }
    }
}