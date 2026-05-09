package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.*
import kotlinx.io.Sink
import kotlinx.io.Source
import java.util.*

data class User(
    val name: String,
    val uuid: UUID
) : StorableObject<User> {
    override val readWriter = Companion

    companion object : StorableReadWriter<User> {
        override val type: String = "author"

        override fun read(source: Source): User {
            val name = source.readSafeString()
            val uuid = source.readUUID()
            return User(name, uuid)
        }

        override fun write(
            sink: Sink,
            obj: User
        ) {
            sink.writeSafeString(obj.name)
            sink.writeUUID(obj.uuid)
        }
    }
}