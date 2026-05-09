package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.util.Hash
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class CommitTest {

    @Test
    fun `commit roundtrip`() {
        val commit = Commit(
            timestamp = 123456789L,
            parents = listOf(Hash.hash("p1"), Hash.hash("p2")),
            author = User("me", UUID.randomUUID()),
            commiter = User("me", UUID.randomUUID()),
            message = "hello world",
            chunkTree = Hash.hash("tree"),
            globalMetaDataSet = Hash.hash("global"),
            blockMetaDataSet = Hash.hash("block")
        )

        val result = roundTrip(
            commit,
            Commit::write,
            Commit::read
        )

        assertEquals(commit, result)
    }
}