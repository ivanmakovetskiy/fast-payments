ARG APP_BUILDER=openjdk:17
ARG APP_RUNNER=$APP_BUILDER

FROM $app_BUILDER as builder

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update
RUN apt-get install sbt

WORKDIR /build
COPY . .
RUN cd FastPayments && sbt stage

FROM $APP_RUNNER

WORKDIR /app
COPY --from=builder /build/fun-cart/target/universal/stage/bin bin
COPY --from=builder /build/fun-cart/target/universal/stage/lib lib

ENTRYPOINT sleep 2 && bin/cart-db