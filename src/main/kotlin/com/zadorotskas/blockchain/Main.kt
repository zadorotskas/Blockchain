package com.zadorotskas.blockchain

import com.zadorotskas.blockchain.network.AddressParser
import com.zadorotskas.blockchain.network.Node
import kotlinx.coroutines.runBlocking

fun main() {
    val address = System.getenv("ADDRESS")
    val otherNodes = System.getenv("NETWORK")
    val nonceFunctionName = System.getenv("NONCE") ?: "random"
    val node = Node(
        address = AddressParser.parse(address),
        otherNodes = otherNodes.split(";").map { AddressParser.parse(it) },
        nonceFunctionName = nonceFunctionName
    )

    val needToGenerateGenesis = System.getenv("GENESIS")?.toBoolean() ?: false
    runBlocking {
        node.launchNode(needToGenerateGenesis)
    }
}