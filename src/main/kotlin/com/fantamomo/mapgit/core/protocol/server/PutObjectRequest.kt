package com.fantamomo.mapgit.core.protocol.server

import com.fantamomo.mapgit.core.protocol.body.MapGitObjectBody
import com.fantamomo.mapgit.core.protocol.request.Request
import com.fantamomo.mapgit.core.protocol.request.RequestDefinition
import com.fantamomo.mapgit.core.protocol.util.HttpMethode

class PutObjectRequest(
    override val body: MapGitObjectBody<*>
) : Request<PutObjectRequest, MapGitObjectBody<*>> {
    override val definition = Companion

    companion object : RequestDefinition<PutObjectRequest, MapGitObjectBody<*>> {
        override val bodyDefinition = MapGitObjectBody
        override val httpMethode = HttpMethode.PUT
        override val serverPath: String = "/repo/{repo}/objects"

        override fun build(
            parameters: Map<String, String>,
            body: MapGitObjectBody<*>
        ) = PutObjectRequest(body)
    }
}