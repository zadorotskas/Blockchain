package com.zadorotskas.blockchain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ConfigTest {

    @Test
    fun `test that thisAddress is set correctly`() {
        val config = Config(arrayOf("--address", "127.0.0.1:8080", "--network", "_", "--nonce", "_"))
        assertEquals("127.0.0.1:8080", config.thisAddress)
    }

    @Test
    fun `test that networkAddresses is set correctly`() {
        val config = Config(arrayOf("--network", "127.0.0.2:8080;127.0.0.3:8080", "--address", "_", "--nonce", "_"))
        assertEquals("127.0.0.2:8080;127.0.0.3:8080", config.networkAddresses)
    }

    @Test
    fun `test that nonceFunction is set correctly`() {
        val config = Config(arrayOf("--nonce", "random", "--address", "_", "--network", "_"))
        assertEquals("random", config.nonceFunction)
    }

    @Test
    fun `test that needToGenerateGenesis is set correctly`() {
        val config = Config(arrayOf("--genesis", "--address", "_", "--network", "_",  "--nonce", "_"))
        assertEquals(true, config.needToGenerateGenesis)
    }

    @Test
    fun `test that needToGenerateGenesis is false when not set`() {
        val config = Config(arrayOf("--address", "_", "--network", "_",  "--nonce", "_"))
        assertEquals(false, config.needToGenerateGenesis)
    }
}