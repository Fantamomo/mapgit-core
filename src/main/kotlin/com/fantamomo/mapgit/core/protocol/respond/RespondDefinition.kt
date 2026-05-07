package com.fantamomo.mapgit.core.protocol.respond

import com.fantamomo.mapgit.core.protocol.body.Body
import com.fantamomo.mapgit.core.protocol.body.BodyDefinition

interface RespondDefinition<R : Respond<B>, B : Body> {
    val bodyDefinition: BodyDefinition<B>

    fun build(body: B): R
}