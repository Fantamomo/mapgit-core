package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import java.util.*

data class Author(
    val name: String,
    val uuid: UUID
) : StorableObject<Author> {
    override val readWriter = Companion

    companion object : StorableReadWriter<Author> {
        override val type: String = "author"

        override fun read(buf: FriendlyByteBuf): Author {
            val name = buf.readString()
            val uuid = buf.readUUID()
            return Author(name, uuid)
        }

        override fun write(
            buf: FriendlyByteBuf,
            obj: Author
        ) {
            buf.writeString(obj.name)
            buf.writeUUID(obj.uuid)
        }
    }
}