package com.fantamomo.mapgit.core

import com.fantamomo.mapgit.core.model.Chunk
import com.fantamomo.mapgit.core.util.Hash
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChunkTest {

    @Test
    fun `chunk single value optimization`() {
        val hash = Hash.hash("stone")
        val chunk = Chunk(List(4096) { hash })

        val result = roundTrip(
            chunk,
            Chunk::write,
            Chunk::read
        )

        assertEquals(chunk, result)
    }

    @Test
    fun `chunk mixed values`() {
        val chunk = Chunk(List(4096) {
            Hash.hash(it.toString())
        })

        val result = roundTrip(
            chunk,
            Chunk::write,
            Chunk::read
        )

        assertEquals(chunk, result)
    }
}