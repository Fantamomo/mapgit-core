package com.fantamomo.mapgit.core.protocol.respond

import com.fantamomo.mapgit.core.protocol.body.Body

interface Respond<B : Body> {
    val body: B
}