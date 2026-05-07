package com.fantamomo.mapgit.core

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import io.netty.buffer.Unpooled

inline fun <T> roundTrip(
    obj: T,
    write: (FriendlyByteBuf, T) -> Unit,
    read: (FriendlyByteBuf) -> T
): T {
    val buf = FriendlyByteBuf(Unpooled.buffer())

    try {
        write(buf, obj)
        return read(buf)
    } finally {
        buf.release()
    }
}