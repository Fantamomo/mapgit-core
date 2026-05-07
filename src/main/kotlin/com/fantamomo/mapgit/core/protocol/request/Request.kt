package com.fantamomo.mapgit.core.protocol.request

import com.fantamomo.mapgit.core.protocol.body.Body

interface Request<R : Request<R, B>, B : Body> {
    val body: B

    val definition: RequestDefinition<R, B>
}