package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.util.InternalMapGitApi
import kotlin.test.Test

@OptIn(InternalMapGitApi::class)
class KeyTest {
    @Test
    fun `string key roundtrip`() {
        val key = Key.string("test")
        val deserialized = roundTrip(key, Key::write, Key::read)
        assert(key == deserialized)
    }

    @Test
    fun `plugin key roundtrip`() {
        val key = Key.plugin("com.fantamomo.mc.amongus", "test")
        val deserialized = roundTrip(key, Key::write, Key::read)
        assert(key == deserialized)
    }
}