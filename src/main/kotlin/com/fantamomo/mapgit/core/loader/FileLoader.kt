package com.fantamomo.mapgit.core.loader

import com.fantamomo.mapgit.core.model.*
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.storage.readSafeString
import com.fantamomo.mapgit.core.util.Hash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.buffered
import kotlinx.io.files.SystemFileSystem
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists
import kotlinx.io.files.Path as KPath

class FileLoader(val dir: Path, val typePrefixed: Boolean = false) : Loader {

    init {
        require(dir.exists() && dir.isDirectory()) { "Path must exist and must be a directory" }
    }

    override suspend fun loadBlock(hash: Hash): LoaderResult<Block> = load(hash, Block)

    override suspend fun loadBlockMetaDataSet(hash: Hash): LoaderResult<BlockMetaDataSet> = load(hash, BlockMetaDataSet)

    override suspend fun loadChunk(hash: Hash): LoaderResult<Chunk> = load(hash, Chunk)

    override suspend fun loadChunkTree(hash: Hash): LoaderResult<ChunkTree> = load(hash, ChunkTree)

    override suspend fun loadCommit(hash: Hash): LoaderResult<Commit> = load(hash, Commit)

    override suspend fun loadGlobalMetaDataSet(hash: Hash): LoaderResult<GlobalMetaDataSet> = load(hash, GlobalMetaDataSet)

    private suspend fun <T : StorableObject<T>> load(
        hash: Hash,
        readWriter: StorableReadWriter<T>
    ): LoaderResult<T> = withContext(Dispatchers.IO) {
        val hex = hash.toHexString()
        val directory = dir.resolve(hex.substring(0, 2))
        val file = directory.resolve("${hex.substring(2)}.mg")
        if (file.notExists()) return@withContext LoaderResult.notFound()

        return@withContext try {
            val path = KPath(file.absolutePathString())
            val source = SystemFileSystem.source(path).buffered()
            if (source.exhausted()) return@withContext LoaderResult.empty()
            if (typePrefixed) {
                if (source.readSafeString() != readWriter.type) {
                    return@withContext LoaderResult.typeMismatch()
                }
            }
            LoaderResult.success(readWriter.read(source))
        } catch (e: Exception) {
            LoaderResult.failure(e)
        }
    }
}