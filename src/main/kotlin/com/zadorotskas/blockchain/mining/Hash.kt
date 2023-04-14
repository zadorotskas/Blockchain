package com.zadorotskas.blockchain.mining

import java.security.MessageDigest

object Hash {
    fun calculateHash(stringData: String): String {
        return MessageDigest
            .getInstance( "SHA-256")
            .digest(stringData.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

    fun Block.hasCorrectHash(): Boolean {
        return hash == calculateHash("" + index + prevHash + data + nonce)
    }
}