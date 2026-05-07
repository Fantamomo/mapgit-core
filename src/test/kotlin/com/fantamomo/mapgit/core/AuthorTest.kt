package com.fantamomo.mapgit.core

import com.fantamomo.mapgit.core.model.Author
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class AuthorTest {

    @Test
    fun `author roundtrip serialization`() {
        val author = Author("Steve", UUID.randomUUID())

        val result = roundTrip(
            author,
            Author::write,
            Author::read
        )

        assertEquals(author, result)
    }

    @Test
    fun `author hash is deterministic`() {
        val uuid = UUID.randomUUID()

        val a1 = Author("Test", uuid)
        val a2 = Author("Test", uuid)

        assertEquals(a1.hash(), a2.hash())
    }
}