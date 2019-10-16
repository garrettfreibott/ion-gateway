FROM openjdk:11-jre-slim
LABEL maintainer=connexta
LABEL com.connexta.application.name=ion-gateway
ARG JAR_FILE
COPY ${JAR_FILE} /ion-gateway
ENTRYPOINT ["java","-Djavax.net.debug=ssl","-Djava.security.egd=file:/dev/./urandom","-jar","/ion-gateway"]
# "-Djavax.net.debug=ssl"
# -Djava.security.egd=file:/dev/./urandom -Djavax.net.ssl.truststore=/certs/ion-truststore.jks -Djavax.net.ssl.trustStorePassword=example