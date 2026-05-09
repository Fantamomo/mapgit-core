package com.fantamomo.mapgit.core.registry

import com.fantamomo.mapgit.core.model.*
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import kotlin.reflect.KClass

object StorableObjectRegistry {
    val objects: Map<KClass<out StorableObject<*>>, StorableReadWriter<out StorableObject<*>>> = mapOf(
        User::class to User,
        Block::class to Block,
        BlockPos::class to BlockPos,
        Chunk::class to Chunk,
        ChunkPos::class to ChunkPos,
        ChunkTree::class to ChunkTree,
        Commit::class to Commit,
    )
}