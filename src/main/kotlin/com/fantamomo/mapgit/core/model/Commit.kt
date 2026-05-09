package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.*
import com.fantamomo.mapgit.core.util.Hash
import kotlinx.io.Sink
import kotlinx.io.Source

data class Commit(
    val timestamp: Long,
    val parents: List<Hash>,
    val author: User,
    val commiter: User,
    val message: String,
    val chunkTree: Hash,
    val globalMetaDataSet: Hash,
    val blockMetaDataSet: Hash
) : StorableObject<Commit> {
    override val readWriter = Companion

    companion object : StorableReadWriter<Commit> {
        override val type: String = "commit"

        override fun read(source: Source): Commit {
            val timestamp = source.readLong()
            val parentCount = source.readVarInt()
            val parents = List(parentCount) { source.readHash() }
            val author = source.readStorableObject(User)
            val commiter = source.readStorableObject(User)
            val message = source.readSafeString()
            val tree = source.readHash()
            val metaDataSet = source.readHash()
            val blockMetaDataSet = source.readHash()

            return Commit(timestamp, parents, author, commiter, message, tree, metaDataSet, blockMetaDataSet)
        }

        override fun write(
            sink: Sink,
            obj: Commit
        ) {
            sink.writeLong(obj.timestamp)
            val parents = obj.parents
            sink.writeVarInt(parents.size)
            for (parent in parents) {
                sink.writeHash(parent)
            }
            sink.writeStorableObject(obj.author)
            sink.writeStorableObject(obj.commiter)
            sink.writeSafeString(obj.message)
            sink.writeHash(obj.chunkTree)
            sink.writeHash(obj.globalMetaDataSet)
            sink.writeHash(obj.blockMetaDataSet)
        }
    }
}