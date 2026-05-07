package com.fantamomo.mapgit.core.storage

import kotlinx.io.Source

interface StorableReader<out T : StorableObject<@UnsafeVariance T>> {
    fun read(source: Source): T
}