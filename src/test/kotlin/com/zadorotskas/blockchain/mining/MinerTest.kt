package com.zadorotskas.blockchain.mining

import com.zadorotskas.blockchain.mining.Miner.generateFirstBlock
import com.zadorotskas.blockchain.mining.Miner.generateNewBlock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class MinerTest {

    @Test
    fun testGenerateNewBlock() {
        // given
        val oldBlock = Block(
            index = 0,
            prevHash = "00000000000000000000000000000000",
            data = "Genesis Block",
            nonce = 0,
            time = 0L
        )
        val data = "Some data"
        val nonceFunction: (Int) -> Int = { it -> it + 1 }

        // given
        val newBlock = generateNewBlock(oldBlock, data, nonceFunction)

        // then
        assertEquals(oldBlock.index + 1, newBlock.index)
        assertEquals(oldBlock.hash, newBlock.prevHash)
        assertEquals(data, newBlock.data)
        assertTrue(newBlock.hash.endsWith("0000"))
        val expectedHash = Hash.calculateHash(
    "" + newBlock.index + newBlock.prevHash + newBlock.data + newBlock.nonce
        )
        assertEquals(
            expectedHash,
            newBlock.hash
        )
    }

    @Test
    fun testGenerateNewBlockWithRandomNonce() {
        // given
        val oldBlock = Block(
            index = 0,
            prevHash = "00000000000000000000000000000000",
            data = "Genesis Block",
            nonce = 0,
            time = 0L
        )
        val data = "Some data"
        val nonceFunction: (Int) -> Int = { Random.nextInt() }

        // given
        val newBlock = generateNewBlock(oldBlock, data, nonceFunction)

        // then
        assertEquals(oldBlock.index + 1, newBlock.index)
        assertEquals(oldBlock.hash, newBlock.prevHash)
        assertEquals(data, newBlock.data)
        assertTrue(newBlock.hash.endsWith("0000"))
        val expectedHash = Hash.calculateHash(
            "" + newBlock.index + newBlock.prevHash + newBlock.data + newBlock.nonce
        )
        assertEquals(
            expectedHash,
            newBlock.hash
        )
    }

    @Test
    fun testGenerateFirstBlock() {
        // given
        val nonceFunction = { n: Int -> n + 1 }
        val data = "Testing the generateFirstBlock function."

        // when
        val firstBlock = generateFirstBlock(data, nonceFunction)

        // then
        assertEquals(0, firstBlock.index)
        assertEquals("", firstBlock.prevHash)
        assertEquals(data, firstBlock.data)
        assertTrue(firstBlock.hash.endsWith("0000"))
        val expectedHash = Hash.calculateHash(
            "" + firstBlock.index + firstBlock.prevHash + firstBlock.data + firstBlock.nonce
        )
        assertEquals(
            expectedHash,
            firstBlock.hash
        )
    }
}