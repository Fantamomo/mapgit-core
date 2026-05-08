package com.fantamomo.mapgit.core.model

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

    @Test
    fun `chunk single value edge case`() {
        val hash = Hash.hash("only-one")
        val chunk = Chunk(List(Chunk.BLOCKS_PER_CHUNK) { hash })

        val result = roundTrip(chunk, Chunk::write, Chunk::read)

        assertEquals(chunk, result)
    }

    @Test
    fun `chunk all unique triggers full encoding`() {
        val chunk = Chunk(List(Chunk.BLOCKS_PER_CHUNK) {
            Hash.hash("v$it")
        })

        val result = roundTrip(chunk, Chunk::write, Chunk::read)

        assertEquals(chunk, result)
    }

    @Test
    fun `chunk stability across repeated serialization`() {
        val chunk = Chunk(List(Chunk.BLOCKS_PER_CHUNK) { Hash.hash("x$it") })

        val r1 = roundTrip(chunk, Chunk::write, Chunk::read)
        val r2 = roundTrip(r1, Chunk::write, Chunk::read)

        assertEquals(chunk, r2)
    }
}