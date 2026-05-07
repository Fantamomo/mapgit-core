package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.storage.readHash
import com.fantamomo.mapgit.core.storage.writeHash
import com.fantamomo.mapgit.core.util.Hash
import kotlinx.io.Sink
import kotlinx.io.Source

data class Chunk(
    val blocks: List<Hash>
) : StorableObject<Chunk> {
    override val readWriter = Companion

    init {
        require(blocks.size == BLOCKS_PER_CHUNK) { "Chunk must contain 16x16x16 ($BLOCKS_PER_CHUNK) blocks, but contains ${blocks.size}" }
    }

    companion object : StorableReadWriter<Chunk> {
        const val BLOCKS_PER_CHUNK = 16 * 16 * 16 // 4096
        const val OPTIMISATION_THRESHOLD = 2048 // Subject to change. Changing this variable will not affect readability.
        override val type: String = "chunk"

        override fun read(source: Source): Chunk {
            val blocks = when (val type = source.readByte().toInt()) {
                1 -> {
                    val hash = source.readHash()
                    List(BLOCKS_PER_CHUNK) { hash }
                }

                2 -> {
                    val size = source.readByte().toInt() and 0xFF
                    val unique = ArrayList<Hash>(size)

                    repeat(size) {
                        unique.add(source.readHash())
                    }

                    List(BLOCKS_PER_CHUNK) {
                        val index = source.readByte().toInt() and 0xFF
                        unique[index]
                    }
                }

                3 -> {
                    val size = source.readInt()
                    val unique = ArrayList<Hash>(size)

                    repeat(size) {
                        unique.add(source.readHash())
                    }

                    List(BLOCKS_PER_CHUNK) {
                        val index = source.readShort().toInt() and 0xFFFF
                        unique[index]
                    }
                }

                4 -> {
                    List(BLOCKS_PER_CHUNK) { source.readHash() }
                }

                else -> throw IllegalStateException("Unknown chunk encoding type: $type")
            }

            return Chunk(blocks)
        }

        override fun write(
            sink: Sink,
            obj: Chunk
        ) {
            val blocks = obj.blocks

            val indexMap = LinkedHashMap<Hash, Int>()
            val uniqueList = ArrayList<Hash>()

            for (block in blocks) {
                if (!indexMap.containsKey(block)) {
                    indexMap[block] = uniqueList.size
                    uniqueList.add(block)
                }
            }

            val uniqueSize = uniqueList.size

            when {
                uniqueSize == 1 -> {
                    sink.writeByte(1)
                    sink.writeHash(uniqueList[0])
                }

                uniqueSize <= 256 -> {
                    sink.writeByte(2)
                    sink.writeByte(uniqueSize.toByte())

                    uniqueList.forEach { sink.writeHash(it) }

                    for (block in blocks) {
                        sink.writeByte(indexMap[block]!!.toByte())
                    }
                }

                uniqueSize <= OPTIMISATION_THRESHOLD -> {
                    sink.writeByte(3)
                    sink.writeInt(uniqueSize)

                    uniqueList.forEach { sink.writeHash(it) }

                    for (block in blocks) {
                        sink.writeShort(indexMap[block]!!.toShort())
                    }
                }

                else -> {
                    sink.writeByte(4)
                    blocks.forEach { sink.writeHash(it) }
                }
            }
        }
    }
}