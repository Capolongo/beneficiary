FROM 840472296963.dkr.ecr.sa-east-1.amazonaws.com/livelo-jre21:prd-latest
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 8080
CMD java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
