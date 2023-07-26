FROM maven:3.6.0-jdk-8-slim

WORKDIR /opt/application
ADD . .
RUN mvn clean install

EXPOSE 4040

#FROM maven:3.6.0-jdk-8-slim AS build
#
#WORKDIR /opt/application
#ADD . .
#RUN mvn clean compile package
#
#FROM openjdk:8-jre-slim
#COPY --from=build /opt/application/background-securityWallet/target/background-securityWallet-0.0.1-SNAPSHOT.war /opt/app.war
#EXPOSE 4040
## ENTRYPOINT ["java","-jar","/opt/app.jar"]
