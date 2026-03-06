FROM eclipse-temurin:17-jdk AS builder

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
RUN curl -fL "https://github.com/sbt/sbt/releases/download/v1.10.11/sbt-1.10.11.tgz" | tar xfz - -C /usr/share
ENV PATH="/usr/share/sbt/bin:$PATH"

WORKDIR /app
COPY project/ project/
COPY build.sbt .
RUN sbt update

COPY src/ src/
RUN sbt assembly && mv target/scala-3.7.1/todo-backend-assembly-*.jar target/app.jar

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/app.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
