package com.fantamomo.mapgit.core.protocol.body

import kotlinx.serialization.Transient

interface Body {
    @Transient
    val bodyDefinition: BodyDefinition<out Body>
}