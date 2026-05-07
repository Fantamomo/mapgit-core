package com.fantamomo.mapgit.core.model

import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.Source

inline fun <T> roundTrip(
    obj: T,
    write: (Sink, T) -> Unit,
    read: (Source) -> T
): T {
    val buf = Buffer()

    write(buf, obj)
    return read(buf)
}