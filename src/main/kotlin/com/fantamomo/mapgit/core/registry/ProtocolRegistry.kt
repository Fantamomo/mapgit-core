package com.fantamomo.mapgit.core.registry

import com.fantamomo.mapgit.core.protocol.request.Request
import com.fantamomo.mapgit.core.protocol.request.RequestDefinition
import com.fantamomo.mapgit.core.protocol.server.GetObjectPatchRequest
import com.fantamomo.mapgit.core.protocol.server.GetObjectRequest
import kotlin.reflect.KClass

object ProtocolRegistry {
    val requests: Map<KClass<out Request<*, *>>, RequestDefinition<*, *>> = mapOf(
        GetObjectRequest::class to GetObjectRequest,
        GetObjectPatchRequest::class to GetObjectPatchRequest
    )
}