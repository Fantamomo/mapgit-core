package com.fantamomo.mapgit.core.model

import com.fantamomo.mapgit.core.storage.StorableObject
import com.fantamomo.mapgit.core.storage.StorableReadWriter
import com.fantamomo.mapgit.core.storage.readSafeString
import com.fantamomo.mapgit.core.storage.writeSafeString
import com.fantamomo.mapgit.core.util.InternalMapGitApi
import kotlinx.io.Sink
import kotlinx.io.Source

/**
 * Represents a sealed interface for a key that can be stored and retrieved.
 * Implementations of this interface are responsible for providing unique identifying
 * information as well as the ability to serialize and deserialize themselves.
 *
 * @author Fantamomo
 * @since 1.0-SNAPSHOT
 */
@OptIn(InternalMapGitApi::class)
sealed interface Key : StorableObject<Key>, Comparable<Key> {
    override val readWriter: StorableReadWriter<Key>
        get() = Companion

    /**
     * Represents a string-based key used within the system.
     * These keys are reserved for internal usage within MapGit and should not
     * be used by plugins or external entities to ensure no conflicts arise.
     *
     * @param value The string value representing the key.
     */
    @InternalMapGitApi
    data class StringKey(val value: String) : Key

    /**
     * Represents a key-value pair tied to a specific plugin.
     *
     * This class is used to avoid conflicts between plugins and internal keys by associating
     * a plugin-specific key with the plugin's unique identifier. The plugin identifier is
     * typically the plugin's ID in the plugin registry.
     *
     * @property plugin The unique identifier of the plugin.
     * @property key The key associated with the plugin.
     */
    data class PluginKey(val plugin: String, val key: String) : Key {
        init {
            require(plugin.isNotBlank()) { "Plugin identifier cannot be blank" }
            require(key.isNotBlank()) { "Key cannot be blank" }
        }
    }

    override fun compareTo(other: Key) = when (this) {
        is StringKey if other is StringKey -> this.value.compareTo(other.value)
        is PluginKey if other is PluginKey -> {
            val pluginComparison = this.plugin.compareTo(other.plugin)
            if (pluginComparison != 0) pluginComparison else this.key.compareTo(other.key)
        }

        is StringKey if other is PluginKey -> 1
        is PluginKey if other is StringKey -> -1
        else -> throw IllegalStateException("Unknown key types: ${this::class}, ${other::class}")
    }

    companion object : StorableReadWriter<Key> {
        override val type: String = "key"

        @InternalMapGitApi
        fun string(value: String) = StringKey(value)

        fun plugin(plugin: String, key: String) = PluginKey(plugin, key)

        // these functions may be ignored if the storage implementation can optimize them
        override fun read(source: Source): Key {
            val type = source.readByte()
            return when (type.toInt()) {
                1 -> StringKey(source.readSafeString())
                2 -> PluginKey(source.readSafeString(), source.readSafeString())
                else -> throw IllegalArgumentException("Invalid key type: $type")
            }
        }

        // these functions may be ignored if the storage implementation can optimize them
        override fun write(sink: Sink, obj: Key) {
            when (obj) {
                is StringKey -> {
                    sink.writeByte(1)
                    sink.writeSafeString(obj.value)
                }

                is PluginKey -> {
                    sink.writeByte(2)
                    sink.writeSafeString(obj.plugin)
                    sink.writeSafeString(obj.key)
                }
            }
        }
    }
}