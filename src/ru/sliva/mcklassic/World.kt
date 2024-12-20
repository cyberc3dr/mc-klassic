package ru.sliva.mcklassic

import korlibs.io.compression.compress
import korlibs.io.compression.deflate.GZIP
import kotlinx.coroutines.delay
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import ru.sliva.mcklassic.protocol.*

class World(val width: Int, val height: Int, val depth: Int) {

    val map = ByteArray(width * height * depth)

    init {
        for(x in 0 until width) {
            for(z in 0 until height) {
                setBlock(x, 0, z, 1)
            }
        }
    }

    fun getBlock(x: Int, y: Int, z: Int): Byte {
        return map[x + depth * (z + width * y)];
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Byte) {
        map[x + depth * (z + width * y)] = block;
    }

    fun serialize() : ByteArray {
        val buf = Buffer()

        buf.writeInt(width * height * depth)
        buf.write(map)

        return buf.readByteArray()
    }

    suspend fun sendWorld(connection: Connection) {
        connection.sendPacket(LevelInitialize())

        val chunks = serialize().compress(GZIP)

        chunks.chunked(1024).forEachIndexed { index, it ->
            connection.sendPacket(LevelDataChunk().apply {
                length = it.size.toShort()
                data = it.copyInto(ByteArray(1024))
                percent = (index * 100 / chunks.size).toUByte()
            })

            delay(500)
        }

        connection.sendPacket(LevelFinalize().apply {
            x = width.toShort()
            y = height.toShort()
            z = depth.toShort()
        })
    }

    data class Location(
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float
    )
}