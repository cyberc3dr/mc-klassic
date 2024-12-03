package ru.sliva.mcklassic.protocol

import io.ktor.utils.io.*
import kotlinx.io.Buffer

val serverboundPackets = listOf(
    PlayerIdentificationC2S(),
    SetBlockC2S(),
    PositionAndOrientationC2S(),
    MessageC2S()
)

sealed interface Packet {
    val packetId: UByte

    interface Clientbound : Packet, Cloneable {
        fun write(buffer: Buffer)
    }

    interface Serverbound : Packet, Cloneable {
        public override fun clone(): Serverbound

        suspend fun read(channel: ByteReadChannel)
    }
}