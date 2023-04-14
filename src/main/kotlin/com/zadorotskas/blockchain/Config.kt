package com.zadorotskas.blockchain

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required

class Config(args: Array<String>) {
    private val argParser = ArgParser("Blockchain node")

    val thisAddress by argParser.option(
        ArgType.String,
        fullName = "address",
        shortName = "a",
        description = "this node address and port"
    ).required()

    val networkAddresses by argParser.option(
        ArgType.String,
        fullName = "network",
        shortName = "n",
        description = "other node addresses in network"
    ).required()

    val nonceFunction by argParser.option(
        ArgType.String,
        fullName = "nonce",
        shortName = "nf",
        description = "function that will be used for nonce generation"
    ).required()

    val needToGenerateGenesis by argParser.option(
        ArgType.Boolean,
        fullName = "genesis",
        shortName = "g",
        description = "Generate genesis"
    ).default(false)

    init {
        argParser.parse(args)
    }
}