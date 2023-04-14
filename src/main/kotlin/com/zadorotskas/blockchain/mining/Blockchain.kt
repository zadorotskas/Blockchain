package com.zadorotskas.blockchain.mining

import com.zadorotskas.blockchain.mining.Miner.generateFirstBlock
import com.zadorotskas.blockchain.mining.Miner.generateNewBlock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Blockchain(
    private val nonceFunction: (Int) -> Int
) {
    private var blocks = mutableListOf<Block>()
    private val lock = ReentrantLock()

    fun initFromBlocks(newBlocks: List<Block>) {
        blocks = newBlocks.toMutableList()
    }

    fun addBlock(block: Block) {
        blocks.add(block)
    }

    fun removeUntil(block: Block) {
        lock.withLock {
            blocks.removeIf {
                it.index >= block.index
            }
        }
    }

    fun generateGenesis() {
        blocks.add(generateFirstBlock(generateData(), nonceFunction))
    }

    val lastBlock
        get() = if (blocks.isNotEmpty()) blocks.last() else null

    val allBlocks
        get() = blocks.toList()

    suspend fun mine(actionAfterSuccessGeneration: suspend (Block) -> Unit) {
        withContext(Dispatchers.Default) {
            while (isActive) {
                val oldBLockBeforeGeneration = blocks.last()
                val newGeneratedBlock = generateNewBlock(
                    oldBlock = oldBLockBeforeGeneration,
                    data = generateData(),
                    nonceFunction = nonceFunction,
                )
                val blockWasAdded =
                lock.withLock {
                    if (blocks.last() == oldBLockBeforeGeneration) {
                        blocks.add(newGeneratedBlock)
                        return@withLock true
                    } else {
                        return@withLock false
                    }
                }
                if (blockWasAdded) actionAfterSuccessGeneration(newGeneratedBlock)
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