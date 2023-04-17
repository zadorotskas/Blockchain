FROM openjdk:11
RUN mkdir /blockchain
ADD . /blockchain
RUN apt-get update

ENV ADDRESS = address \
    NETWORK = network \
    NONCE = nonce \
    GENESIS = genesis

RUN cd /blockchain && ./gradlew jar