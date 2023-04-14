package com.zadorotskas.blockchain.network

import com.zadorotskas.blockchain.mining.Blockchain
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class NodeTest {

    @Test
    fun `3 nodes should goes to same blockchain`() = runBlocking {
        val first = Node(
            address = AddressParser.parse("127.0.0.1:8080"),
            otherNodes = "127.0.0.1:8081;127.0.0.1:8082".split(";").map { AddressParser.parse(it) },
            nonceFunctionName = "increment"
        )
        val second = Node(
            address = AddressParser.parse("127.0.0.1:8081"),
            otherNodes = "127.0.0.1:8080;127.0.0.1:8082".split(";").map { AddressParser.parse(it) },
            nonceFunctionName = "increment"
        )
        val third = Node(
            address = AddressParser.parse("127.0.0.1:8082"),
            otherNodes = "127.0.0.1:8080;127.0.0.1:8081".split(";").map { AddressParser.parse(it) },
            nonceFunctionName = "increment"
        )

        launch {
            third.launchNode(false)
        }
        launch {
            second.launchNode(false)
        }
        launch {
            first.launchNode(true)
        }

        while(first.blockchain.allBlocks.size < 3 && second.blockchain.allBlocks.size < 3 && third.blockchain.allBlocks.size < 3) {
            delay(1000)
        }

        first.close()
        second.close()
        third.close()

        assertBlockchainEquals(
            first.blockchain,
            second.blockchain
        )
        assertBlockchainEquals(
            first.blockchain,
            third.blockchain
        )
        assertBlockchainEquals(
            second.blockchain,
            third.blockchain
        )
    }

    private fun assertBlockchainEquals(first: Blockchain, second: Blockchain) {
        val firstBlocks = if (first.allBlocks.size < second.allBlocks.size) first.allBlocks else second.allBlocks
        val secondBlocks = if (first.allBlocks.size < second.allBlocks.size) second.allBlocks else first.allBlocks

        firstBlocks.forEachIndexed { index, firstBlock ->
            val secondBlock = secondBlocks[index]
            assertEquals(firstBlock, secondBlock)
        }
    }
}