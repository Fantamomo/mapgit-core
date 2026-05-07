package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.*
import com.fantamomo.mapgit.core.util.Hash
import kotlinx.io.Sink
import kotlinx.io.Source

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

        override fun read(source: Source): Commit {
            val timestamp = source.readLong()
            val parentCount = source.readInt()
            val parents = List(parentCount) { source.readHash() }
            val author = source.readStorableObject(Author)
            val message = source.readSafeString()
            val tree = source.readHash()
            return Commit(timestamp, parents, author, message, tree)
        }

        override fun write(
            sink: Sink,
            obj: Commit
        ) {
            sink.writeLong(obj.timestamp)
            val parents = obj.parents
            sink.writeInt(parents.size)
            for (parent in parents) {
                sink.writeHash(parent)
            }
            sink.writeStorableObject(obj.author)
            sink.writeSafeString(obj.message)
            sink.writeHash(obj.chunkTree)
        }
    }
}