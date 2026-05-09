package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.util.InternalMapGitApi
import kotlin.test.Test

class BlockMetaDataSetTest {
    @Test
    @OptIn(InternalMapGitApi::class)
    fun `block metadata set roundtrip`() {
        val testData = mapOf(
            BlockMetaDataSet.Key(Key.string("ttest"), ChunkPos(12, 0, 23), BlockPos(1, 3, 1)) to MetaData.string("value1"),
            BlockMetaDataSet.Key(Key.string("key2"), ChunkPos(0, 0, 0), BlockPos(0, 0, 0)) to MetaData.string("value2")
        )
        val blockMetaDataSet = BlockMetaDataSet(testData)
        val deserialized = roundTrip(
            blockMetaDataSet,
            BlockMetaDataSet::write,
            BlockMetaDataSet::read
        )
        assert(blockMetaDataSet == deserialized)
    }
}