package com.fantamomo.mapgit.core.protocol.respond

import com.fantamomo.mapgit.core.protocol.body.Body
import com.fantamomo.mapgit.core.protocol.body.BodyDefinition
import kotlin.reflect.KClass

interface RespondDefinition<R : Respond<B>, B : Body> {
    val bodyDefinition: BodyDefinition<B>

    val bodyClass: KClass<B>
        get() = bodyDefinition.bodyClass

    fun build(body: B): R
}