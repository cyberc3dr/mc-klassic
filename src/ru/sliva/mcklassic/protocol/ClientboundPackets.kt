package ru.sliva.mcklassic.protocol

import kotlinx.io.Buffer
import kotlinx.io.writeUByte

class ServerIdentification : Packet.Clientbound {

    override val packetId: UByte = 0x00u

    var protocolVersion: UByte = 0x07u
    var serverName = ""
    var serverMotd = ""
    var userType: UByte = 0x00u

    override fun write(buffer: Buffer) {
        buffer.writeUByte(protocolVersion)
        buffer.writeString(serverName)
        buffer.writeString(serverMotd)
        buffer.writeUByte(userType)
    }
}

class Ping : Packet.Clientbound {

    override val packetId: UByte = 0x01u

    override fun write(buffer: Buffer) {
        // do nothing
    }
}

class LevelInitialize : Packet.Clientbound {

    override val packetId: UByte = 0x02u

    override fun write(buffer: Buffer) {
        // do nothing
    }
}

class LevelDataChunk : Packet.Clientbound {

    override val packetId: UByte = 0x03u

    var length: Short = 0
    var data = ByteArray(1024)
    var percent: UByte = 0x00u

    override fun write(buffer: Buffer) {
        buffer.writeShort(length)
        buffer.write(data)
        buffer.writeUByte(percent)
    }
}

class LevelFinalize : Packet.Clientbound {

    override val packetId: UByte = 0x04u

    var x: Short = 100
    var y: Short = 100
    var z: Short = 100

    override fun write(buffer: Buffer) {
        buffer.writeShort(x)
        buffer.writeShort(y)
        buffer.writeShort(z)
    }
}

class MessageS2C : Packet.Clientbound {

    override val packetId: UByte = 0x0du

    var playerID: Byte = -1
    var message = ""

    override fun write(buffer: Buffer) {
        buffer.writeByte(playerID)
        buffer.writeString(message)
    }
}

class SpawnPlayer : Packet.Clientbound {

    override val packetId: UByte = 0x07u

    var playerId: Byte = -1
    var playerName = ""
    var playerX: Short = 0
    var playerY: Short = 0
    var playerZ: Short = 0
    var yaw: Byte = 0x00
    var pitch: Byte = 0x00

    override fun write(buffer: Buffer) {
        buffer.writeByte(playerId)
        buffer.writeString(playerName)
        buffer.writeShort(playerX)
        buffer.writeShort(playerY)
        buffer.writeShort(playerZ)
        buffer.writeByte(yaw)
        buffer.writeByte(pitch)
    }
}

class DisconnectPlayerS2C : Packet.Clientbound {

    override val packetId: UByte = 0x0eu

    var reason = ""

    override fun write(buffer: Buffer) {
        buffer.writeString(reason)
    }
}