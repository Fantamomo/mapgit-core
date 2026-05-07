package com.fantamomo.mapgit.core.storage

import kotlin.enums.enumEntries

inline fun <reified E : Enum<E>> FriendlyByteBuf.readEnum(): E = readEnum(enumEntries<E>())