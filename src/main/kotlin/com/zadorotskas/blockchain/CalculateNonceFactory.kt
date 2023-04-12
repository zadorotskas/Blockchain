package com.zadorotskas.blockchain

import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

object CalculateNonceFactory {
    fun create(calculateNonceType: String): (Int) -> Int {
        return when (calculateNonceType) {
            "increment" -> { it -> it + 1 }
            "fibonacci" -> { it -> if (it == 0) 1 else (it * (1 + sqrt(5.0)) / 2.0).roundToInt() }
            "random" -> { _ -> Random.nextInt() }
            else -> error("Unknown calculate nonce type")
        }
    }
}
