package com.fantamomo.mapgit.core.loader

import com.fantamomo.mapgit.core.util.Hash
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class CachedLoader(private val delegate: Loader) : Loader {
    private val cache = mutableMapOf<Hash, Any>()

    override suspend fun loadBlock(hash: Hash) = cacheGet(hash, delegate::loadBlock)

    override suspend fun loadBlockMetaDataSet(hash: Hash) = cacheGet(hash, delegate::loadBlockMetaDataSet)

    override suspend fun loadChunk(hash: Hash) = cacheGet(hash, delegate::loadChunk)

    override suspend fun loadChunkTree(hash: Hash) = cacheGet(hash, delegate::loadChunkTree)

    override suspend fun loadCommit(hash: Hash) = cacheGet(hash, delegate::loadCommit)

    override suspend fun loadGlobalMetaDataSet(hash: Hash) = cacheGet(hash, delegate::loadGlobalMetaDataSet)

    @OptIn(ExperimentalContracts::class)
    @Suppress("UNCHECKED_CAST")
    private suspend fun <T> cacheGet(hash: Hash, loader: suspend (Hash) -> LoaderResult<T>): LoaderResult<T> {
        contract { callsInPlace(loader, InvocationKind.AT_MOST_ONCE) }
        val cached = cache[hash]
        if (cached != null) {
            return LoaderResult.success(cached as T)
        }
        val result = loader(hash)
        if (result is LoaderResult.Success<*>) {
            cache[hash] = result.value as Any
        }
        return result
    }
}