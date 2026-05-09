package com.fantamomo.mapgit.core.model

import kotlin.test.Test

class MetaDataTest {
    @Test
    fun `metadata boolean roundtrip`() {
        val meta = MetaData.boolean(true)
        val deserialized = roundTrip(meta, MetaData.getReadWriter<Boolean>()::write, MetaData.getReadWriter<Boolean>()::read)
        assert(meta == deserialized)
    }

    @Test
    fun `metadata string roundtrip`() {
        val meta = MetaData.string("test")
        val deserialized = roundTrip(meta, MetaData.getReadWriter<String>()::write, MetaData.getReadWriter<String>()::read)
        assert(meta == deserialized)
    }

    @Test
    fun `metadata int roundtrip`() {
        val meta = MetaData.int(123)
        val deserialized = roundTrip(meta, MetaData.getReadWriter<Int>()::write, MetaData.getReadWriter<Int>()::read)
        assert(meta == deserialized)
    }
}