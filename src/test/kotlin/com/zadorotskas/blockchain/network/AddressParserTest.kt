package com.zadorotskas.blockchain.network

import io.ktor.network.sockets.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AddressParserTest {

    @Test
    fun `test parse with valid IPv4 address and port number`() {
        // given
        val addressWithPort = "192.168.0.1:8080"

        // when
        val result = AddressParser.parse(addressWithPort)

        // then
        val expectedSocketAddress = InetSocketAddress("192.168.0.1", 8080)
        assertEquals(expectedSocketAddress, result)
    }

    @Test
    fun `test parse with invalid port number`() {
        // given
        val addressWithPort = "192.168.0.1:abc"

        // then
        assertThrows<NumberFormatException> {
            // when
            AddressParser.parse(addressWithPort)
        }
    }

    @Test
    fun `test parse with empty string`() {
        // given
        val addressWithPort = ""

        // then
        assertThrows<IndexOutOfBoundsException> {
            // when
            AddressParser.parse(addressWithPort)
        }
    }
}