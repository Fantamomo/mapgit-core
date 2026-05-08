package com.fantamomo.mapgit.core.protocol.body

import kotlinx.io.Sink
import kotlinx.io.Source
import kotlin.reflect.KClass

interface BodyDefinition<B : Body> {
    val bodyClass: KClass<B>

    val contentType: String

    fun serialize(sink: Sink, body: B)

    fun deserialize(source: Source): B
}