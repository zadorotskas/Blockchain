FROM openjdk:11
RUN mkdir /blockchain
ADD . /blockchain
RUN apt-get update
RUN cd /blockchain && ./gradlew jar