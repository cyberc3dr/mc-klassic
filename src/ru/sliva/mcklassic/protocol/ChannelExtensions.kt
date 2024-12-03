package ru.sliva.mcklassic.protocol

import io.ktor.utils.io.*
import kotlinx.io.Buffer
import kotlinx.io.writeUByte

/**
 * Reads a string from the channel.
 *
 * @return The string.
 * @receiver The channel.
 */
suspend fun ByteReadChannel.readString() = readByteArray(64).decodeToString().trim()

/**
 * Writes a string to the channel.
 *
 * @param string The string.
 * @receiver The channel.
 */
fun Buffer.writeString(string: String) = write(string.encodeToByteArray().copyInto(ByteArray(64)))

/**
 * Reads a fixed-point short from the channel.
 *
 * @return The short.
 * @receiver The channel.
 */
suspend fun ByteReadChannel.readFShort() = readShort() / 32.0

/**
 * Writes a fixed-point short to the channel.
 *
 * @param value The short.
 * @receiver The channel.
 */
fun Buffer.writeFShort(value: Short) = writeShort((value * 32).toShort())

/**
 * Reads an unsigned byte from the channel.
 *
 * @return The unsigned byte.
 * @receiver The channel.
 */
suspend fun ByteReadChannel.readUByte() = readByte().toUByte()

/**
 * Reads a packet from the channel.
 *
 * @receiver The channel.
 * @return The packet.
 */
suspend fun ByteReadChannel.readClientPacket() : Packet.Serverbound? {
    val packetId = readUByte()

    return serverboundPackets
        .firstOrNull { it.packetId == packetId }
        ?.clone()?.also {
            it.read(this)
        }
}

/**
 * Writes a packet to the channel.
 *
 * @param packet The packet.
 * @receiver The channel.
 */
suspend fun ByteWriteChannel.writeServerPacket(packet: Packet.Clientbound) {
    val buf = Buffer().apply {
        writeUByte(packet.packetId)
        packet.write(this)
    }

    writePacket(buf)

    flush()
}