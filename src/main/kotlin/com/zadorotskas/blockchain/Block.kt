package com.zadorotskas.blockchain

data class Block(
    val index: Int,
    val prevHash: String,
    val data: String,
    val nonce: Int,
    val hash: String = Hash.calculateHash("" + index + prevHash + data + nonce)
)