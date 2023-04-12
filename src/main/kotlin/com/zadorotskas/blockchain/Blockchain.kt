package com.zadorotskas.blockchain

import com.zadorotskas.blockchain.Miner.generateNewBlock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

class Blockchain(
    private val nonceFunction: (Int) -> Int
) {
    private var blocks = mutableListOf<Block>()

    fun initFromBlocks(vararg newBlocks: Block) {
        blocks = newBlocks.toMutableList()
    }

    suspend fun mine() {
        withContext(Dispatchers.Default) {
            while (isActive) {
                val oldBLockBeforeGeneration = blocks.last()
                val newGeneratedBlock = generateNewBlock(
                    oldBlock = oldBLockBeforeGeneration,
                    data = generateData(),
                    nonceFunction = nonceFunction,
                )
                if (blocks.last() == oldBLockBeforeGeneration) {
                    blocks.add(newGeneratedBlock)
                }
            }
        }
    }

    private fun generateData(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..256)
            .map { allowedChars.random() }
            .joinToString("")
    }
}