package ru.sliva.mcklassic

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.sliva.mcklassic.protocol.*
import kotlin.time.Duration.Companion.seconds

class Connection(val socket: Socket) {

    private var writeChannel: ByteWriteChannel? = null

    init {
        connections += this
    }

    suspend fun init() {
        val read = socket.openReadChannel()

        while(true) {
            try {
                val packet = read.readClientPacket()

                if(packet != null) {
                    handlePacket(packet)
                }
            } catch (e: Throwable) {
                println("Closed")
                break
            }
        }
    }

    fun writeChannel() : ByteWriteChannel {
        if(writeChannel == null) {
            writeChannel = socket.openWriteChannel()
        }

        return writeChannel!!
    }

    suspend fun handlePacket(packet: Packet.Serverbound) {
        when(packet) {
            is PlayerIdentificationC2S -> {
                println("Received PlayerIdentificationC2S packet")
                println("Protocol version: ${packet.protocolVersion}")
                println("Username: ${packet.username}")
                println("Verification key: ${packet.verificationKey}")

                sendPacket(ServerIdentification().apply {
                    serverName = "MCKlassic"
                    serverMotd = "Welcome to MCKlassic!"
                })

                Main.world.sendWorld(this)

            }
            is SetBlockC2S -> {
                println("Received SetBlockC2S packet")
                println("X: ${packet.x}")
                println("Y: ${packet.y}")
                println("Z: ${packet.z}")
                println("Mode: ${packet.mode}")
                println("Block type: ${packet.blockType}")
            }
            is PositionAndOrientationC2S -> {
                println("Received PositionAndOrientationC2S packet")
                println("Player ID: ${packet.playerID}")
                println("X: ${packet.x}")
                println("Y: ${packet.y}")
                println("Z: ${packet.z}")
            }
            is MessageC2S -> {
                println("Received MessageC2S packet")
                println("Message: ${packet.message}")

                connections.forEach {
                    it.sendPacket(MessageS2C().apply {
                        message = packet.message
                    })
                }
            }
        }
    }

    suspend fun sendPacket(packet: Packet.Clientbound) =
        writeChannel().writeServerPacket(packet)

    companion object {
        val connections = mutableListOf<Connection>()
    }
}