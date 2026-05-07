package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.FriendlyByteBuf
import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.util.Hash

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

        override fun read(buf: FriendlyByteBuf): Chunk {
            val blocks = when (val type = buf.readByte().toInt()) {
                1 -> {
                    val hash = buf.readHash()
                    List(BLOCKS_PER_CHUNK) { hash }
                }

                2 -> {
                    val size = buf.readByte().toInt() and 0xFF
                    val unique = ArrayList<Hash>(size)

                    repeat(size) {
                        unique.add(buf.readHash())
                    }

                    List(BLOCKS_PER_CHUNK) {
                        val index = buf.readByte().toInt() and 0xFF
                        unique[index]
                    }
                }

                3 -> {
                    val size = buf.readInt()
                    val unique = ArrayList<Hash>(size)

                    repeat(size) {
                        unique.add(buf.readHash())
                    }

                    List(BLOCKS_PER_CHUNK) {
                        val index = buf.readShort().toInt() and 0xFFFF
                        unique[index]
                    }
                }

                4 -> {
                    List(BLOCKS_PER_CHUNK) { buf.readHash() }
                }

                else -> throw IllegalStateException("Unknown chunk encoding type: $type")
            }

            return Chunk(blocks)
        }

        override fun write(
            buf: FriendlyByteBuf,
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
                    buf.writeByte(1)
                    buf.writeHash(uniqueList[0])
                }

                uniqueSize <= 256 -> {
                    buf.writeByte(2)
                    buf.writeByte(uniqueSize)

                    uniqueList.forEach { buf.writeHash(it) }

                    for (block in blocks) {
                        buf.writeByte(indexMap[block]!!)
                    }
                }

                uniqueSize <= OPTIMISATION_THRESHOLD -> {
                    buf.writeByte(3)
                    buf.writeInt(uniqueSize)

                    uniqueList.forEach { buf.writeHash(it) }

                    for (block in blocks) {
                        buf.writeShort(indexMap[block]!!)
                    }
                }

                else -> {
                    buf.writeByte(4)
                    blocks.forEach { buf.writeHash(it) }
                }
            }
        }
    }
}