package com.fantamomo.mapgit.core.loader

import com.fantamomo.mapgit.core.model.Block
import com.fantamomo.mapgit.core.model.Chunk
import com.fantamomo.mapgit.core.model.ChunkTree
import com.fantamomo.mapgit.core.model.Commit
import com.fantamomo.mapgit.core.util.Hash

interface Loader {

    suspend fun loadBlock(hash: Hash): LoaderResult<Block>

    suspend fun loadChunk(hash: Hash): LoaderResult<Chunk>

    suspend fun loadChunkTree(hash: Hash): LoaderResult<ChunkTree>

    suspend fun loadCommit(hash: Hash): LoaderResult<Commit>
}
