package com.fantamomo.mapgit.core.storage

import kotlinx.io.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VarLongTest {

    @Test
    fun testVarLongValues() {
        val values = listOf(
            0L,
            1L,
            2L,
            127L,
            128L,
            255L,
            300L,
            16384L,
            Long.MAX_VALUE,
            -1L,
            Long.MIN_VALUE
        )

        for (value in values) {
            val buffer = Buffer()

            buffer.writeVarLong(value)
            val decoded = buffer.readVarLong()

            assertEquals(value, decoded)
        }
    }

    @Test
    fun testInvalidVarLongTooLong() {
        val buffer = Buffer()

        repeat(11) {
            buffer.writeByte(0x80.toByte())
        }

        assertFailsWith<IllegalArgumentException> {
            buffer.readVarLong()
        }
    }
}