package ru.sliva.mcklassic

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Main {

    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val port = 25565

    val world = World(128, 128, 128)

    @JvmStatic
    fun main(arr: Array<String>) = runBlocking {
        val serverSocket = aSocket(selectorManager).tcp().bind(port = port)

        while(true) {
            Connection(serverSocket.accept())
        }
    }
}