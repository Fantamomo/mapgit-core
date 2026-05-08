package com.fantamomo.mapgit.core.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChunkPosTest {

    @Test
    fun `chunkpos roundtrip`() {
        val pos = ChunkPos(10, 20, 30)

        val result = roundTrip(
            pos,
            ChunkPos::write,
            ChunkPos::read
        )

        assertEquals(pos, result)
    }

    @Test
    fun `chunkpos equality works`() {
        val a = ChunkPos(1, 2, 3)
        val b = ChunkPos(1, 2, 3)

        assertEquals(a, b)
    }
}