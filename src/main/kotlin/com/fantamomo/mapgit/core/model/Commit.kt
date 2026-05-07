package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.util.Hash

data class Commit(
    val timestamp: Long,
    val parents: List<Hash>,
    val author: Author,
    val message: String,
    val chunkTree: Hash
) : StorableObject<Commit> {
    override val readWriter = Companion

    companion object : StorableReadWriter<Commit> {
        override val type: String = "commit"

        override fun read(buf: FriendlyByteBuf): Commit {
            val timestamp = buf.readLong()
            val parentCount = buf.readInt()
            val parents = List(parentCount) { buf.readHash() }
            val author = buf.readStorableObject(Author)
            val message = buf.readString()
            val tree = buf.readHash()
            return Commit(timestamp, parents, author, message, tree)
        }

        override fun write(
            buf: FriendlyByteBuf,
            obj: Commit
        ) {
            buf.writeLong(obj.timestamp)
            val parents = obj.parents
            buf.writeInt(parents.size)
            for (parent in parents) {
                buf.writeHash(parent)
            }
            buf.writeStorableObject(obj.author)
            buf.writeString(obj.message)
            buf.writeHash(obj.chunkTree)
        }
    }
}