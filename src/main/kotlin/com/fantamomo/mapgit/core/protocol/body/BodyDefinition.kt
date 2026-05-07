package com.fantamomo.mapgit.core.protocol.body

import kotlinx.io.Sink
import kotlinx.io.Source

interface BodyDefinition<B : Body> {
    val contentType: String

    fun serialize(sink: Sink, body: B)

    fun deserialize(source: Source): B
}