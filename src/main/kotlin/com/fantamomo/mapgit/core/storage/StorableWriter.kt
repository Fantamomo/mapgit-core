package com.fantamomo.mapgit.core.storage

import kotlinx.io.Sink

interface StorableWriter<in T : StorableObject<@UnsafeVariance T>> {
    fun write(sink: Sink, obj: T)
}