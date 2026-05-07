package com.fantamomo.mapgit.core

import com.fantamomo.mapgit.core.model.ChunkPos
import com.fantamomo.mapgit.core.model.ChunkTree
import com.fantamomo.mapgit.core.util.Hash
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChunkTreeTest {

    @Test
    fun `chunk tree roundtrip`() {
        val tree = ChunkTree(
            listOf(
                ChunkPos(1, 2, 3) to Hash.hash("a"),
                ChunkPos(4, 5, 6) to Hash.hash("b")
            )
        )

        val result = roundTrip(
            tree,
            ChunkTree::write,
            ChunkTree::read
        )

        assertEquals(tree, result)
    }
}