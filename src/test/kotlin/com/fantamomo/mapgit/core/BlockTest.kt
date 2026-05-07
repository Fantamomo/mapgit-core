package com.fantamomo.mapgit.core

import com.fantamomo.mapgit.core.model.Block
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BlockTest {

    @Test
    fun `block roundtrip serialization`() {
        val block = Block("stone", byteArrayOf(1, 2, 3, 4, 5))

        val result = roundTrip(
            block,
            Block::write,
            Block::read
        )

        assertEquals(block, result)
    }

    @Test
    fun `block equals uses byte content`() {
        val b1 = Block("stone", byteArrayOf(1, 2, 3))
        val b2 = Block("stone", byteArrayOf(1, 2, 3))

        assertEquals(b1, b2)
    }

    @Test
    fun `block hash is stable`() {
        val b1 = Block("stone", byteArrayOf(9, 9, 9))
        val b2 = Block("stone", byteArrayOf(9, 9, 9))

        assertEquals(b1.hash(), b2.hash())
    }
}