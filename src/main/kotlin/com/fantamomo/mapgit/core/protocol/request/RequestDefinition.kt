package com.fantamomo.mapgit.core.protocol.request

import com.fantamomo.mapgit.core.protocol.body.Body
import com.fantamomo.mapgit.core.protocol.body.BodyDefinition
import com.fantamomo.mapgit.core.protocol.util.HttpMethode

interface RequestDefinition<R : Request<R, B>, B : Body> {
    val bodyDefinition: BodyDefinition<B>

    val httpMethode: HttpMethode
    val serverPath: String

    fun getPath(request: R): String = serverPath

    fun build(parameters: Map<String, String>, body: B): R
}