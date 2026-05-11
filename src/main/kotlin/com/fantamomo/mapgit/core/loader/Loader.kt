package com.fantamomo.mapgit.core.loader

import com.fantamomo.mapgit.core.model.*
import com.fantamomo.mapgit.core.util.Hash

interface Loader {

    suspend fun loadBlock(hash: Hash): LoaderResult<Block>

    suspend fun loadBlockMetaDataSet(hash: Hash): LoaderResult<BlockMetaDataSet>

    suspend fun loadChunk(hash: Hash): LoaderResult<Chunk>

    suspend fun loadChunkTree(hash: Hash): LoaderResult<ChunkTree>

    suspend fun loadCommit(hash: Hash): LoaderResult<Commit>

    suspend fun loadGlobalMetaDataSet(hash: Hash): LoaderResult<GlobalMetaDataSet>
}
