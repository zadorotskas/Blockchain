package com.zadorotskas.blockchain.mining

import io.ktor.util.date.*

object Miner {
    fun generateNewBlock(oldBlock: Block, data: String, nonceFunction: (Int) -> Int): Block {
        val prevHash = oldBlock.hash
        val index = oldBlock.index + 1

        return generateBlock(prevHash, index, data, nonceFunction)
    }

    fun generateFirstBlock(data: String, nonceFunction: (Int) -> Int): Block {
        return generateBlock("", 0, data, nonceFunction)
    }

    private fun generateBlock(prevHash: String, index: Int, data: String, nonceFunction: (Int) -> Int): Block {
        var nonce = 0
        var newBlock: Block

        do {
            newBlock = Block(
                index = index,
                prevHash = prevHash,
                data = data,
                nonce = nonce,
                time = getTimeMillis()
            )
            nonce = nonceFunction(nonce)
        }
        while (!newBlock.hash.endsWith("0000"))

        return newBlock
    }
}