package com.fantamomo.mapgit.core.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BlockPosTest {

    @Test
    fun `blockpos roundtrip`() {
        val pos = BlockPos(1, 13, 15)

        val result = roundTrip(
            pos,
            BlockPos::write,
            BlockPos::read
        )

        assertEquals(pos, result)
    }

    @Test
    fun `blockpos bounds validation`() {
        assertFailsWith<IllegalArgumentException> {
            BlockPos(16, 0, 0)
        }
    }

    @Test
    fun `blockpos accepts boundary values`() {
        val pos = BlockPos(0, 0, 0)
        val pos2 = BlockPos(15, 15, 15)

        assertEquals(pos, roundTrip(pos, BlockPos::write, BlockPos::read))
        assertEquals(pos2, roundTrip(pos2, BlockPos::write, BlockPos::read))
    }

    @Test
    fun `blockpos rejects negative values`() {
        assertFailsWith<IllegalArgumentException> {
            BlockPos(-1, 0, 0)
        }
    }
}