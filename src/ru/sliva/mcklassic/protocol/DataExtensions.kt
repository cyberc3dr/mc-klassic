package ru.sliva.mcklassic.protocol

fun ByteArray.chunked(size: Int) = toList().chunked(size).map { it.toByteArray() }