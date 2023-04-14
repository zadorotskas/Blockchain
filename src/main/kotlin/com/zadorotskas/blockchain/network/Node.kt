package com.zadorotskas.blockchain.network

import com.zadorotskas.blockchain.mining.Block
import com.zadorotskas.blockchain.mining.Blockchain
import com.zadorotskas.blockchain.mining.CalculateNonceFactory
import com.zadorotskas.blockchain.mining.Hash.hasCorrectHash
import com.zadorotskas.blockchain.network.MessageSender.receive
import com.zadorotskas.blockchain.network.MessageSender.send
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import mu.KotlinLogging
import java.util.concurrent.CountDownLatch

class Node(
    val address: SocketAddress,
    val otherNodes: List<SocketAddress>,
    nonceFunctionName: String
) {
    val blockchain: Blockchain
    private val countDownLatch = CountDownLatch(1)
    private val waitingForConsensusBlocks = mutableListOf<Block>()
    private val logger = KotlinLogging.logger { }

    init {
        blockchain = Blockchain(
            CalculateNonceFactory.create(nonceFunctionName)
        )
    }

    private lateinit var listenSocketsJob: Job
    private lateinit var miningJob: Job

    suspend fun launchNode(needToGenerateGenesis: Boolean) = coroutineScope {
        if (needToGenerateGenesis) {
            blockchain.generateGenesis()
            countDownLatch.countDown()
            notifyOtherNodesAboutNewBlock(blockchain.lastBlock!!)
        }

        listenSocketsJob = launch {
            listenToSockets()
        }
        miningJob = launch {
            withContext(Dispatchers.IO) {
                countDownLatch.await()
            }
            logger.info("Node $address: start mining")
            blockchain.mine(::notifyOtherNodesAboutNewBlock)
        }
    }

    private suspend fun listenToSockets() {
        withContext(Dispatchers.IO) {
            val serverSocket = aSocket(SelectorManager(Dispatchers.IO))
                .tcp().bind(address)

            while (isActive) {
                serverSocket.accept().receiveAndHandleIncomingMessage()
            }
        }
    }

    private suspend fun Socket.receiveAndHandleIncomingMessage() {
        val incomingMessage = this.receive()

        when (incomingMessage.type) {
            MessageType.BLOCK_GENERATED -> handleBlockGeneratedMessage(incomingMessage)
            MessageType.REQUEST_BLOCKS -> handleRequestBlock(incomingMessage)
            MessageType.REQUEST_BLOCKS_RESPONSE -> handleRequestBlockResponse(incomingMessage)
        }
    }

    private suspend fun handleBlockGeneratedMessage(incomingMessage: Message) {
        val incomingBlock = incomingMessage.blocks?.first() ?: error("didn't receive block in message")
        val lastBlock = blockchain.lastBlock ?: run {
            blockchain.addBlock(incomingBlock)
            countDownLatch.countDown()
            return
        }

        when {
            lastBlock.index == incomingBlock.index - 1 -> {
                if (incomingBlock.prevHash == lastBlock.hash && incomingBlock.hasCorrectHash()) {
                    blockchain.addBlock(incomingBlock)
                } else if (incomingBlock.prevHash != lastBlock.hash && waitingForConsensusBlocks.isEmpty()) {
                    requestBlocks(lastBlock, incomingBlock, incomingMessage.senderAddress)
                }
            }
            lastBlock.index == incomingBlock.index -> {
                if (incomingBlock.prevHash != lastBlock.prevHash || !incomingBlock.hasCorrectHash() || waitingForConsensusBlocks.isNotEmpty()) {
                    return
                }
                requestBlocks(lastBlock, incomingBlock, incomingMessage.senderAddress)
            }
            lastBlock.index < incomingBlock.index - 1 -> {
                if (incomingBlock.hasCorrectHash()) {
                    requestBlocks(lastBlock, incomingBlock, incomingMessage.senderAddress)
                }
            }
        }
    }

    private suspend fun requestBlocks(lastBlock: Block, incomingBlock: Block, notSendTo: String) {
        waitingForConsensusBlocks.add(lastBlock)
        waitingForConsensusBlocks.add(incomingBlock)
        val message = Message(
            type = MessageType.REQUEST_BLOCKS,
            senderAddress = address.toString().removePrefix("/")
        )
        send(message,
            otherNodes
                .filterNot { it == AddressParser.parse(notSendTo) }
                .first()
        )
    }

    private suspend fun handleRequestBlock(incomingMessage: Message) {
        val message = Message(
            type = MessageType.REQUEST_BLOCKS_RESPONSE,
            senderAddress = address.toString().removePrefix("/"),
            blocks = blockchain.allBlocks
        )
        send(message,
            otherNodes
            .first { it == AddressParser.parse(incomingMessage.senderAddress) }
        )
    }

    private fun handleRequestBlockResponse(incomingMessage: Message) {
        val first = waitingForConsensusBlocks.first()
        val second = waitingForConsensusBlocks[1]
        waitingForConsensusBlocks.clear()
        val blocksFromThird = incomingMessage.blocks?: error("didn't receive blocks")
        if (blocksFromThird.size <= first.index) {
            if (first.time > second.time) {
                blockchain.removeUntil(first)
                blockchain.addBlock(second)
            }
            return
        }

        val third = incomingMessage.blocks[first.index]
        when {
            first.data == third.data -> {
                // do nothing
            }
            second.data == third.data -> {
                blockchain.initFromBlocks(incomingMessage.blocks)
            }
            first.time < second.time && first.time < third.time -> {
                // do nothing
            }
            second.time < first.time && second.time < third.time -> {
                blockchain.removeUntil(first)
                blockchain.addBlock(second)
            }
            third.time < first.time && third.time < second.time -> {
                blockchain.removeUntil(first)
                blockchain.addBlock(third)
            }
        }

    }

    private suspend fun notifyOtherNodesAboutNewBlock(block: Block) {
        withContext(Dispatchers.IO) {
            val message = Message(
                type = MessageType.BLOCK_GENERATED,
                senderAddress = address.toString().removePrefix("/"),
                blocks = listOf(block)
            )

            otherNodes.forEach {
                send(message, it)
            }
        }
    }

    fun close() {
        listenSocketsJob.cancel()
        miningJob.cancel()
    }
}