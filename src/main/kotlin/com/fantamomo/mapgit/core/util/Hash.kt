package com.fantamomo.mapgit.core.util

import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.readTo
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.math.min

@ConsistentCopyVisibility
data class Hash private constructor(
    private val value: ByteArray
) {
    init {
        require(value.size == 32) { "Hash must be 32 bytes long" }
    }

    fun toHexString(format: HexFormat = HexFormat.Default): String =
        value.toHexString(format)

    fun writeTo(buf: Sink) {
        buf.write(value)
    }

    companion object {
        val NULL = Hash(ByteArray(32))

        private val DIGEST = ThreadLocal.withInitial {
            MessageDigest.getInstance("SHA-256")
        }

        private fun digest(): MessageDigest {
            val d = DIGEST.get()
            d.reset()
            return d
        }

        fun fromHexString(hexString: String, format: HexFormat = HexFormat.Default): Hash =
            Hash(hexString.hexToByteArray(format))

        fun fromBytes(bytes: ByteArray): Hash {
            require(bytes.size == 32)
            return Hash(bytes.copyOf())
        }

        fun readFromBuffer(source: Source): Hash {
            val bytes = ByteArray(32)
            source.readTo(bytes)
            return Hash(bytes)
        }

        fun hash(buf: Buffer): Hash {
            val digest = digest()
            val buffer = ByteArray(8192)

            while (buf.size > 0) {
                val toRead = min(buffer.size.toLong(), buf.size).toInt()
                buf.readTo(buffer, 0, toRead)
                digest.update(buffer, 0, toRead)
            }
            return Hash(digest.digest())
        }

        fun hash(data: ByteArray): Hash {
            val digest = digest()
            return Hash(digest.digest(data))
        }

        fun hash(data: String): Hash =
            hash(data.toByteArray(StandardCharsets.UTF_8))

        fun hash(input: InputStream): Hash {
            val digest = digest()
            val buffer = ByteArray(8192)

            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                digest.update(buffer, 0, read)
            }

            return Hash(digest.digest())
        }

        fun hashVarargs(vararg values: Any?): Hash {
            val digest = digest()

            values.forEach {
                when (it) {
                    null -> digest.update(0)
                    is ByteArray -> digest.update(it)
                    is String -> digest.update(it.toByteArray(StandardCharsets.UTF_8))
                    is Int -> digest.update(it.toByte())
                    is Long -> digest.update(it.toByte())
                    else -> digest.update(it.toString().toByteArray(StandardCharsets.UTF_8))
                }
            }

            return Hash(digest.digest())
        }
    }

    override fun equals(other: Any?): Boolean =
        other is Hash && value.contentEquals(other.value)

    override fun hashCode(): Int =
        value.contentHashCode()
}