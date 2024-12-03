package ru.sliva.mcklassic.protocol

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Zips the byte array.
 *
 * @return The zipped byte array.
 */
fun ByteArray.gzipped(): ByteArray {
    val outputStream = ByteArrayOutputStream(size)
    java.util.zip.GZIPOutputStream(outputStream).use {
        it.write(this)
    }
    return outputStream.toByteArray()
}

/**
 * Unzips the byte array.
 *
 * @return The unzipped byte array.
 */
fun ByteArray.ungzipped(): ByteArray {
    val outputStream = ByteArrayOutputStream(size)
    java.util.zip.GZIPInputStream(ByteArrayInputStream(this)).use {
        it.copyTo(outputStream)
    }
    return outputStream.toByteArray()
}

fun ByteArray.chunked(size: Int) = toList().chunked(size).map { it.toByteArray() }