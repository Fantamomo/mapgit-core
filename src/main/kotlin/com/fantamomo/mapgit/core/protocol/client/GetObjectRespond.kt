package com.fantamomo.mapgit.core.protocol.client

import com.fantamomo.mapgit.core.protocol.body.BodyDefinition
import com.fantamomo.mapgit.core.protocol.body.MapGitObjectBody
import com.fantamomo.mapgit.core.protocol.respond.Respond
import com.fantamomo.mapgit.core.protocol.respond.RespondDefinition
import com.fantamomo.mapgit.core.storage.StorableObject

/**
 * Server respond for [com.fantamomo.mapgit.core.protocol.server.GetObjectRequest]
 */
class GetObjectRespond<T : StorableObject<T>>(
    override val body: MapGitObjectBody<T>,
) : Respond<MapGitObjectBody<T>> {

    @Suppress("UNCHECKED_CAST")
    private constructor(body: MapGitObjectBody<*>, @Suppress("unused") ignore: Unit = Unit) : this(body as MapGitObjectBody<T>)

    companion object : RespondDefinition<Respond<MapGitObjectBody<*>>, MapGitObjectBody<*>> {
        override val bodyDefinition: BodyDefinition<MapGitObjectBody<*>> = MapGitObjectBody

        override fun build(body: MapGitObjectBody<*>): Respond<MapGitObjectBody<*>> {
            @Suppress("UNCHECKED_CAST")
            return GetObjectRespond(body, Unit) as Respond<MapGitObjectBody<*>>
        }
    }
}