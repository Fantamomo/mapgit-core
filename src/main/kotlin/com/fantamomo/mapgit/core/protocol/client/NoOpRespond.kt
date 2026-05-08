package com.fantamomo.mapgit.core.protocol.client

import com.fantamomo.mapgit.core.protocol.body.BodyDefinition
import com.fantamomo.mapgit.core.protocol.body.EmptyBody
import com.fantamomo.mapgit.core.protocol.respond.Respond
import com.fantamomo.mapgit.core.protocol.respond.RespondDefinition

/**
 * Only a placeholder respond for requests that don't need to return anything.
 */
data object NoOpRespond : Respond<EmptyBody>, RespondDefinition<NoOpRespond, EmptyBody> {
    override val body = EmptyBody
    override val bodyDefinition: BodyDefinition<EmptyBody> = EmptyBody

    override fun build(body: EmptyBody) = NoOpRespond
}