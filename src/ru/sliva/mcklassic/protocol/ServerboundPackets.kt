package ru.sliva.mcklassic.protocol

import io.ktor.utils.io.*

class PlayerIdentificationC2S: Packet.Serverbound {

    override val packetId: UByte = 0x00u

    var protocolVersion: Byte = 0x07
    var username: String = ""
    var verificationKey: String = ""

    override suspend fun read(channel: ByteReadChannel) {
        protocolVersion = channel.readByte()
        username = channel.readString()
        verificationKey = channel.readString()
        channel.readByte() // unused byte
    }

    override fun clone() = PlayerIdentificationC2S().also {
        it.protocolVersion = protocolVersion
        it.username = username
        it.verificationKey = verificationKey
    }
}

class SetBlockC2S : Packet.Serverbound {

    override val packetId: UByte = 0x05u

    var x: Short = 0
    var y: Short = 0
    var z: Short = 0
    var mode: UByte = 0x00u
    var blockType: UByte = 0x00u

    override suspend fun read(channel: ByteReadChannel) {
        x = channel.readShort()
        y = channel.readShort()
        z = channel.readShort()
        mode = channel.readUByte()
        blockType = channel.readUByte()
    }

    override fun clone() = SetBlockC2S().also {
        it.x = x
        it.y = y
        it.z = z
        it.mode = mode
        it.blockType = blockType
    }
}

class PositionAndOrientationC2S : Packet.Serverbound {

    override val packetId: UByte = 0x08u

    var playerID: Byte = -1
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    var yaw: Byte = 0
    var pitch: Byte = 0

    override suspend fun read(channel: ByteReadChannel) {
        playerID = channel.readByte()
        x = channel.readFShort()
        y = channel.readFShort()
        z = channel.readFShort()
        yaw = channel.readByte()
        pitch = channel.readByte()
    }

    override fun clone() = PositionAndOrientationC2S().also {
        it.playerID = playerID
        it.x = x
        it.y = y
        it.z = z
        it.yaw = yaw
        it.pitch = pitch
    }
}

class MessageC2S : Packet.Serverbound {

    override val packetId: UByte = 0x0du

    var playerID: Byte = -1
    var message = ""

    override suspend fun read(channel: ByteReadChannel) {
        playerID = channel.readByte()
        message = channel.readString()
    }

    override fun clone() = MessageC2S().also {
        it.playerID = playerID
        it.message = message
    }
}