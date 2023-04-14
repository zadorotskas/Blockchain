package com.zadorotskas.blockchain

import com.zadorotskas.blockchain.network.AddressParser
import com.zadorotskas.blockchain.network.Node
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    val config = Config(args)
    val node = Node(
        address = AddressParser.parse(config.thisAddress),
        otherNodes = config.networkAddresses.split(";").map { AddressParser.parse(it) },
        nonceFunctionName = config.nonceFunction
    )

    runBlocking {
        node.launchNode(config.needToGenerateGenesis)
    }
}