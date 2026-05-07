package com.fantamomo.mapgit.core.loader

import com.fantamomo.mapgit.core.model.Block
import com.fantamomo.mapgit.core.model.Chunk
import com.fantamomo.mapgit.core.model.ChunkTree
import com.fantamomo.mapgit.core.model.Commit
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.util.Hash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.Buffer
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists
import kotlin.io.path.readBytes

class FileLoader(val dir: Path) : Loader {

    init {
        require(dir.exists() && dir.isDirectory()) { "Path must exist and must be a directory" }
    }

    override suspend fun loadBlock(hash: Hash): LoaderResult<Block> = load(hash, Block)

    override suspend fun loadChunk(hash: Hash): LoaderResult<Chunk> = load(hash, Chunk)

    override suspend fun loadChunkTree(hash: Hash): LoaderResult<ChunkTree> = load(hash, ChunkTree)

    override suspend fun loadCommit(hash: Hash): LoaderResult<Commit> = load(hash, Commit)

    private suspend fun <T : StorableObject<T>> load(
        hash: Hash,
        readWriter: StorableReadWriter<T>
    ): LoaderResult<T> = withContext(Dispatchers.IO) {
        val hex = hash.toHexString()
        val directory = dir.resolve(hex.substring(0, 2))
        val file = directory.resolve("${hex.substring(2)}.mg")
        if (file.notExists()) return@withContext LoaderResult.notFound()

        return@withContext try {
            val bytes = file.readBytes()
            if (bytes.isEmpty()) return@withContext LoaderResult.empty()
            val buf = Buffer()
            LoaderResult.success(readWriter.read(buf))
        } catch (e: Exception) {
            LoaderResult.failure(e)
        }
    }
}