package com.fantamomo.mapgit.core.storage

import kotlinx.io.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VarIntTest {

    @Test
    fun testVarIntValues() {
        val values = listOf(
            0,
            1,
            2,
            127,
            128,
            255,
            300,
            16384,
            Int.MAX_VALUE,
            -1,
            Int.MIN_VALUE
        )

        for (value in values) {
            val buffer = Buffer()

            buffer.writeVarInt(value)
            val decoded = buffer.readVarInt()

            assertEquals(value, decoded)
        }
    }

    @Test
    fun testInvalidVarIntTooLong() {
        val buffer = Buffer()

        repeat(6) {
            buffer.writeByte(0x80.toByte())
        }

        assertFailsWith<IllegalArgumentException> {
            buffer.readVarInt()
        }
    }
}