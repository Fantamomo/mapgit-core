package com.fantamomo.mapgit.core.protocol.server

import com.fantamomo.mapgit.core.protocol.body.BodyDefinition
import com.fantamomo.mapgit.core.protocol.body.SerializableBody
import com.fantamomo.mapgit.core.protocol.request.Request
import com.fantamomo.mapgit.core.protocol.request.RequestDefinition
import com.fantamomo.mapgit.core.protocol.util.HttpMethode
import kotlinx.serialization.Serializable

class GetObjectPatchRequest(
    val repo: String,
    override val body: GetObjectPatchRequestBody
) : Request<GetObjectPatchRequest, GetObjectPatchRequest.GetObjectPatchRequestBody> {

    override val definition = Companion

    @Serializable
    @SerializableBody.SerializableBodyPath("objectHashes")
    class GetObjectPatchRequestBody(val objectHashes: List<String>) : SerializableBody<GetObjectPatchRequestBody>()

    companion object : RequestDefinition<GetObjectPatchRequest, GetObjectPatchRequestBody> {
        override val bodyDefinition: BodyDefinition<GetObjectPatchRequestBody> = SerializableBody.definition()

        override val bodyClass = GetObjectPatchRequestBody::class
        override val httpMethode: HttpMethode = HttpMethode.GET
        override val serverPath: String = "/repo/{repo}/objects/patch"

        override fun build(
            parameters: Map<String, String>,
            body: GetObjectPatchRequestBody
        ) = GetObjectPatchRequest(
            repo = parameters["repo"] ?: throw IllegalArgumentException("Missing 'repo' parameter"),
            body = body
        )

        override fun getPath(request: GetObjectPatchRequest) = serverPath.replace("{repo}", request.repo)
    }
}