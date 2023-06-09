FROM clojure:temurin-17-lein-focal as build

ADD . /app
WORKDIR /app

RUN lein uberjar

# -----------------------------------------------------------------------------

FROM eclipse-temurin:17-focal

ARG ID
ARG TOKEN
ARG NAME
ARG EMAIL
ARG REGISTRY

RUN mkdir /app
COPY --from=build /app/target/*uberjar/meuse-*-standalone.jar /app/meuse.jar
COPY --from=build-env /app/dev/resources/config.yaml /app/dev/resources/config.yaml

RUN apt-get update && apt-get install -y git

ENV ID=$ID
ENV TOKEN=$TOKEN
ENV NAME=$NAME
ENV EMAIL=$EMAIL
ENV REGISTRY=$REGISTRY

RUN git config --global user.email $EMAIL && \
    git config --global user.name "${NAME}" && \
    git config --global url."https://${ID}:${TOKEN}@github.com/".insteadOf "https://github.com/"

RUN git clone $REGISTRY registry && \
    git config --global --add safe.directory /registry

ENTRYPOINT ["java"]

CMD ["-jar", "/app/meuse.jar"]
