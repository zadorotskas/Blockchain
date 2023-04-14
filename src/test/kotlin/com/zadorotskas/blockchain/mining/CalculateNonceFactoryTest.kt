package com.zadorotskas.blockchain.mining

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class CalculateNonceFactoryTest {

    @Test
    fun `create should return a function that increments the input by 1 when given 'increment' as argument`() {
        val function = CalculateNonceFactory.create("increment")
        assertEquals(2, function(1))
        assertEquals(0, function(-1))
        assertEquals(100, function(99))
    }

    @Test
    fun `create should return a function that calculates the Fibonacci sequence when given 'fibonacci' as argument`() {
        val function = CalculateNonceFactory.create("fibonacci")
        assertEquals(1, function(0))
        assertEquals(2, function(1))
        assertEquals(3, function(2))
        assertEquals(5, function(3))
        assertEquals(8, function(5))
        assertEquals(610, function(377))
        assertEquals(514229, function(317811))
        assertEquals(165580141, function(102334155))
    }

    @Test
    fun `create should return a function that generates a random integer when given 'random' as argument`() {
        val function = CalculateNonceFactory.create("random")
        val result1 = function(0)
        val result2 = function(0)
        val result3 = function(0)
        assert(result1 != result2 && result1 != result3)
    }

    @Test
    fun `create should throw an error when given an unknown calculate nonce type`() {
        assertFailsWith<java.lang.IllegalStateException>("Unknown calculate nonce type") {
            CalculateNonceFactory.create("unknown")
        }
    }
}