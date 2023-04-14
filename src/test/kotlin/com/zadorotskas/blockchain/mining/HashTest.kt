package com.zadorotskas.blockchain.mining

import com.zadorotskas.blockchain.mining.Hash.hasCorrectHash
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class HashTest {

    @Test
    fun testCalculateHash() {
        //given
        val inputData = "hello world"

        //when
        val actualOutput = Hash.calculateHash(inputData)

        //then
        val expectedOutput = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"
        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testCalculateHashWithEmptyString() {
        //given
        val inputData = ""

        //when
        val actualOutput = Hash.calculateHash(inputData)

        //then
        val expectedOutput = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
        assertEquals(expectedOutput, actualOutput, "Empty string hash should be equal to SHA-256 empty hash.")
    }

    @Test
    fun testCalculateHashWithSpecialCharacters() {
        // given
        val inputData = "hello, world!"

        // when
        val actualOutput = Hash.calculateHash(inputData)

        // then
        val expectedOutput = "68e656b251e67e8358bef8483ab0d51c6619f3e7a1a9f0e75838d41ff368f728"
        assertEquals(
            expectedOutput,
            actualOutput,
            "Hash of a string with special characters should match the expected output."
        )
    }

    @Test
    fun `block with correct hash returns true`() {
        val block = Block(
            index = 1,
            prevHash = "abc",
            data = "Hello",
            nonce = 0,
            hash = "806a90d1a05e1d64e004fdb7a5c79f867e9d0df0ada95bbaa8e72e0abca94c37",
            time = 0L
        )
        assertTrue(block.hasCorrectHash())
    }

    @Test
    fun `block with incorrect hash returns false`() {
        val block = Block(index = 1, prevHash = "abc", data = "Hello", nonce = 0, hash = "wrong_hash", time = 0L)
        assertFalse(block.hasCorrectHash())
    }

    @Test
    fun `block with incorrect index returns false`() {
        val block = Block(
            index = 2,
            prevHash = "abc",
            data = "Hello",
            nonce = 0,
            hash = "806a90d1a05e1d64e004fdb7a5c79f867e9d0df0ada95bbaa8e72e0abca94c37",
            time = 0L
        )
        assertFalse(block.hasCorrectHash())
    }

    @Test
    fun `block with incorrect prevHash returns false`() {
        val block = Block(
            index = 1,
            prevHash = "def",
            data = "Hello",
            nonce = 0,
            hash = "806a90d1a05e1d64e004fdb7a5c79f867e9d0df0ada95bbaa8e72e0abca94c37",
            time = 0L
        )
        assertFalse(block.hasCorrectHash())
    }

    @Test
    fun `block with incorrect data returns false`() {
        val block = Block(
            index = 1,
            prevHash = "abc",
            data = "Hi",
            nonce = 0,
            hash = "806a90d1a05e1d64e004fdb7a5c79f867e9d0df0ada95bbaa8e72e0abca94c37",
            time = 0L
        )
        assertFalse(block.hasCorrectHash())
    }

    @Test
    fun `block with incorrect nonce returns false`() {
        val block = Block(
            index = 1,
            prevHash = "abc",
            data = "Hello",
            nonce = 1,
            hash = "806a90d1a05e1d64e004fdb7a5c79f867e9d0df0ada95bbaa8e72e0abca94c37",
            time = 0L
        )
        assertFalse(block.hasCorrectHash())
    }
}