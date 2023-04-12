package com.zadorotskas.blockchain

object Miner {
    fun generateNewBlock(oldBlock: Block, data: String, nonceFunction: (Int) -> Int): Block {
        var nonce = 0
        var newBlock: Block
        val prevHash = oldBlock.hash
        val index = oldBlock.index + 1

        do {
            newBlock = Block(
                index = index,
                prevHash = prevHash,
                data = data,
                nonce = nonce,
            )
            nonce = nonceFunction(nonce)
        }
        while (!newBlock.hash.endsWith("0000"))

        return newBlock
    }
}