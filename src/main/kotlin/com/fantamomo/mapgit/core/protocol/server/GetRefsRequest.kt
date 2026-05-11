package com.fantamomo.mapgit.core.protocol.server

import com.fantamomo.mapgit.core.protocol.body.EmptyBody
import com.fantamomo.mapgit.core.protocol.request.Request
import com.fantamomo.mapgit.core.protocol.request.RequestDefinition
import com.fantamomo.mapgit.core.protocol.util.HttpMethode

/**
 * Server respond: [com.fantamomo.mapgit.core.protocol.client.GetObjectRespond]
 */
class GetRefsRequest(val repo: String) : Request<GetRefsRequest, EmptyBody> {
    override val body: EmptyBody = EmptyBody
    override val definition = Companion

    companion object : RequestDefinition<GetRefsRequest, EmptyBody> {
        override val bodyDefinition = EmptyBody
        override val httpMethode = HttpMethode.GET
        override val serverPath = "/repo/{repo}/refs"

        override fun build(
            parameters: Map<String, String>,
            body: EmptyBody
        ) = GetRefsRequest(parameters["repo"] ?: throw IllegalArgumentException("Missing 'repo' parameter"))

        override fun getPath(request: GetRefsRequest) = serverPath.replace("{repo}", request.repo)
    }
}