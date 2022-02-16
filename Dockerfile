FROM ubuntu:18.04
RUN apt-get update && apt-get -y install sudo;
RUN apt-get -y install curl
RUN sudo apt-get install -y openjdk-11-jdk
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
RUN apt-get update && apt-get install -y unzip
WORKDIR /gradle
RUN curl -L https://services.gradle.org/distributions/gradle-7.3.2-bin.zip -o gradle-7.3.2-bin.zip
RUN unzip gradle-7.3.2-bin.zip
ENV GRADLE_HOME=/gradle/gradle-7.3.2
ENV PATH=$PATH:$GRADLE_HOME/bin
COPY ./ /app_temp
WORKDIR /app_temp/build
RUN gradle clean
RUN gradle assemble