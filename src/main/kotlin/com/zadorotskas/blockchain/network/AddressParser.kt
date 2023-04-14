package com.zadorotskas.blockchain.network

import io.ktor.network.sockets.*

object AddressParser {
    fun parse(addressWithPort: String): SocketAddress {
        val split = addressWithPort.split(":")
        val address = split[0]
        val port = split[1]
        return InetSocketAddress(address, port.toInt())
    }
}