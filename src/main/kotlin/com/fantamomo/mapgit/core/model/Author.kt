package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.*
import kotlinx.io.Sink
import kotlinx.io.Source
import java.util.*

data class Author(
    val name: String,
    val uuid: UUID
) : StorableObject<Author> {
    override val readWriter = Companion

    companion object : StorableReadWriter<Author> {
        override val type: String = "author"

        override fun read(source: Source): Author {
            val name = source.readSafeString()
            val uuid = source.readUUID()
            return Author(name, uuid)
        }

        override fun write(
            sink: Sink,
            obj: Author
        ) {
            sink.writeSafeString(obj.name)
            sink.writeUUID(obj.uuid)
        }
    }
}