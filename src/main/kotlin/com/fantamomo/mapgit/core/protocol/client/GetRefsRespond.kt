package com.fantamomo.mapgit.core.protocol.client

import com.fantamomo.mapgit.core.protocol.body.BodyDefinition
import com.fantamomo.mapgit.core.protocol.body.SerializableBody
import com.fantamomo.mapgit.core.protocol.respond.Respond
import com.fantamomo.mapgit.core.protocol.respond.RespondDefinition

class GetRefsRespond(
    override val body: GetRefsRespondBody
) : Respond<GetRefsRespond.GetRefsRespondBody> {
    /**
     * @param refs Map of ref name to ref hash.
     * The hash is a hex string and can be converted to a [com.fantamomo.mapgit.core.util.Hash] using [com.fantamomo.mapgit.core.util.Hash.fromHexString]
     */
    class GetRefsRespondBody(val refs: Map<String, String>) : SerializableBody<GetRefsRespondBody>()

    companion object : RespondDefinition<GetRefsRespond, GetRefsRespondBody> {
        override val bodyDefinition: BodyDefinition<GetRefsRespondBody> = SerializableBody.definition()

        override fun build(body: GetRefsRespondBody) = GetRefsRespond(body)
    }
}