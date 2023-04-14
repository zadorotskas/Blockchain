# Blockchain
Simple blockchain project for network programming course

[![Tests](https://github.com/zadorotskas/Blockchain/actions/workflows/blockchain.yml/badge.svg?branch=master)](https://github.com/zadorotskas/Blockchain/actions/workflows/gradle-tests.yml)

## Run with docker
Clone repository
```console
https://github.com/zadorotskas/Blockchain.git
cd Blockchain
```
Build docker image
```console
docker build -t zadorotskas/blockchain .
```
Run docker
```console
docker-compose up
```

## Tests
Implemented tests:
1. Integration tests
2. Unit tests

Tests coverage: 

| Class, %     | Method, %    | Line, %       | 
|--------------|--------------|---------------| 
| 83 % (52/62) | 81% (88/108) | 74% (356/476) |