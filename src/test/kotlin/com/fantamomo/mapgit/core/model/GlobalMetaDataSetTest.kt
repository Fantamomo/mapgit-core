package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.util.InternalMapGitApi
import kotlin.test.Test

class GlobalMetaDataSetTest {
    @OptIn(InternalMapGitApi::class)
    @Test
    fun `global metadata set roundtrip`() {
        val testData = mapOf(
            Key.string("key1") to MetaData.string("value1"),
            Key.plugin("com.fantamomo.mc.amongus", "key2") to MetaData.string("value2")
        )
        val globalMetaDataSet = GlobalMetaDataSet(testData)
        val deserialized = roundTrip(
            globalMetaDataSet,
            GlobalMetaDataSet::write,
            GlobalMetaDataSet::read
        )
        assert(globalMetaDataSet == deserialized)
    }
}