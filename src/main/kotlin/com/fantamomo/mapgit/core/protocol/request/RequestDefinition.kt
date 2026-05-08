package com.fantamomo.mapgit.core.protocol.request

import com.fantamomo.mapgit.core.protocol.body.Body
import com.fantamomo.mapgit.core.protocol.body.BodyDefinition
import com.fantamomo.mapgit.core.protocol.util.HttpMethode
import kotlin.reflect.KClass

interface RequestDefinition<R : Request<R, B>, B : Body> {
    val bodyDefinition: BodyDefinition<B>

    val bodyClass: KClass<B>
        get() = bodyDefinition.bodyClass

    val httpMethode: HttpMethode
    val serverPath: String

    fun getPath(request: R): String = serverPath

    fun build(parameters: Map<String, String>, body: B): R
}