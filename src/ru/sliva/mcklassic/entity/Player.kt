package ru.sliva.mcklassic.entity

import kotlinx.coroutines.launch
import ru.sliva.mcklassic.Connection
import ru.sliva.mcklassic.protocol.DisconnectPlayerS2C
import ru.sliva.mcklassic.protocol.MessageS2C

class Player(val connection: Connection, val nickname: String) {

    fun kick(reason: String) = connection.launch {
        connection.sendPacket(DisconnectPlayerS2C().also {
            it.reason = reason
        })

        connection.close()
    }

    fun sendMessage(message: String) = connection.launch {
        connection.sendPacket(MessageS2C().also {
            it.message = message
        })
    }

    companion object {
        val players = mutableListOf<Player>()

        fun findPlayer(nickname: String) = players.find { it.nickname == nickname }
    }
}