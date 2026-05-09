package com.fantamomo.mapgit.core.model

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class UserTest {

    @Test
    fun `author roundtrip serialization`() {
        val user = User("Steve", UUID.randomUUID())

        val result = roundTrip(
            user,
            User::write,
            User::read
        )

        assertEquals(user, result)
    }

    @Test
    fun `author hash is deterministic`() {
        val uuid = UUID.randomUUID()

        val a1 = User("Test", uuid)
        val a2 = User("Test", uuid)

        assertEquals(a1.hash(), a2.hash())
    }

    @Test
    fun `author equality ignores instance identity`() {
        val uuid = UUID.randomUUID()

        val a1 = User("Steve", uuid)
        val a2 = User("Steve", uuid)

        assertEquals(a1, a2)
    }

    @Test
    fun `author roundtrip preserves uuid and name`() {
        val uuid = UUID.randomUUID()
        val user = User("UnitTest", uuid)

        val result = roundTrip(user, User::write, User::read)

        assertEquals("UnitTest", result.name)
        assertEquals(uuid, result.uuid)
    }
}