package com.fantamomo.mapgit.core.protocol.server

import com.fantamomo.mapgit.core.protocol.body.EmptyBody
import com.fantamomo.mapgit.core.protocol.request.Request
import com.fantamomo.mapgit.core.protocol.request.RequestDefinition
import com.fantamomo.mapgit.core.protocol.util.HttpMethode
import com.fantamomo.mapgit.core.util.Hash

/**
 * Server respond: [com.fantamomo.mapgit.core.protocol.client.GetObjectRespond]
 */
class GetObjectRequest(
    val repo: String,
    val objectHash: Hash
) : Request<GetObjectRequest, EmptyBody> {
    override val body = EmptyBody
    override val definition = Companion

    companion object : RequestDefinition<GetObjectRequest, EmptyBody> {
        override val bodyDefinition = EmptyBody
        override val httpMethode = HttpMethode.GET
        override val serverPath: String = "/repo/{repo}/objects/{objectHash}"

        override fun build(
            parameters: Map<String, String>,
            body: EmptyBody
        ): GetObjectRequest {
            val repo = parameters["repo"] ?: throw IllegalArgumentException("Missing 'repo' parameter")
            val objectHash = Hash.fromHexString(parameters["objectHash"] ?: throw IllegalArgumentException("Missing 'objectHash' parameter"))
            return GetObjectRequest(repo, objectHash)
        }

        override fun getPath(request: GetObjectRequest) =
            serverPath.replace("{repo}", request.repo).replace("{objectHash}", request.objectHash.toHexString())
    }
}