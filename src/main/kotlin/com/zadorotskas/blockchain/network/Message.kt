package com.zadorotskas.blockchain.network

import com.zadorotskas.blockchain.mining.Block
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val type: MessageType,
    val senderAddress: String,
    val blocks: List<Block>? = null
)

enum class MessageType {
    BLOCK_GENERATED,
    REQUEST_BLOCKS,
    REQUEST_BLOCKS_RESPONSE
}
