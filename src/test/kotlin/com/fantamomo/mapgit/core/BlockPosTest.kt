package com.fantamomo.mapgit.core

import com.fantamomo.mapgit.core.model.BlockPos
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BlockPosTest {

    @Test
    fun `blockpos roundtrip`() {
        val pos = BlockPos(1, 200, 15)

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
}