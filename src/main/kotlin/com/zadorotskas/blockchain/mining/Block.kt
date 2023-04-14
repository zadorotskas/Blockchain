package com.zadorotskas.blockchain.mining

import kotlinx.serialization.Serializable

@Serializable
data class Block(
    val index: Int,
    val prevHash: String,
    val data: String,
    val nonce: Int,
    val hash: String = Hash.calculateHash("" + index + prevHash + data + nonce),
    val time: Long
)