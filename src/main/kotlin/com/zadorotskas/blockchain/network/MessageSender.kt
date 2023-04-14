package com.zadorotskas.blockchain.network

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import kotlin.text.Charsets.UTF_8

object MessageSender {
    private val logger = KotlinLogging.logger {  }

    suspend fun send(message: Message, socketAddress: SocketAddress) {
        val channel = aSocket(SelectorManager())
            .tcp()
            .connect(socketAddress)
            .openWriteChannel(true)

        val bytes = Json.encodeToString(message).toByteArray(UTF_8)

        channel.writeInt(bytes.size)
        channel.writeFully(bytes)
        logger.info("Node ${message.senderAddress}: send message $message")
    }

    suspend fun Socket.receive(): Message {
        val channel = openReadChannel()
        val bytes = ByteArray(channel.readInt())
        channel.readFully(bytes)
        val message: Message = Json.decodeFromString(String(bytes, UTF_8))
        logger.info("Node ${this.localAddress.toString().removePrefix("/")}: receive message $message")
        return message
    }
}