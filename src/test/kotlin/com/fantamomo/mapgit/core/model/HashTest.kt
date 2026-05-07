package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.util.Hash
import kotlinx.io.Buffer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class HashTest {

    @Test
    fun `hash deterministic string`() {
        val h1 = Hash.hash("abc")
        val h2 = Hash.hash("abc")

        assertEquals(h1, h2)
    }

    @Test
    fun `hash different inputs differ`() {
        val h1 = Hash.hash("a")
        val h2 = Hash.hash("b")

        assertNotEquals(h1, h2)
    }

    @Test
    fun `hash bytebuf deterministic`() {
        val buf1 = Buffer()
        buf1.write(byteArrayOf(1, 2, 3))
        val buf2 = Buffer()
        buf2.write(byteArrayOf(1, 2, 3))

        val h1 = Hash.hash(buf1)
        val h2 = Hash.hash(buf2)

        assertEquals(h1, h2)
    }
}