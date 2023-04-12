package com.zadorotskas.blockchain

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

class Config(args: Array<String>) {
    private val argParser = ArgParser("Blockchain node")

    val thisAddress by argParser.option(
        ArgType.String,
        fullName = "address",
        shortName = "a",
        description = "this node address and port"
    )

    val networkAddresses by argParser.option(
        ArgType.String,
        fullName = "network",
        shortName = "n",
        description = "other node addresses in network"
    )

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