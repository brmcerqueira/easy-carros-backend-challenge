FROM gradle:5.0-jdk11

ARG BACK_HOME=/usr/verticles
ARG BACK_PROJECT=/easy-carros-backend-challenge

ENV VERTICLE_JAR easy-carros-backend-challenge-1.0-SNAPSHOT-fat.jar
ENV EVR dev

WORKDIR $BACK_HOME

COPY . .$BACK_PROJECT

USER root
RUN chown -R gradle .
USER gradle

WORKDIR $BACK_HOME$BACK_PROJECT
RUN gradle build

EXPOSE 8080

ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar build/libs/$VERTICLE_JAR -conf environments/$EVR-conf.json"]