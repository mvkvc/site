FROM eclipse-temurin:17-jdk-noble

RUN apt-get update && apt-get install -y \
    curl \
    unzip

RUN curl -fsSL https://bun.sh/install | bash
ENV PATH="/root/.bun/bin:${PATH}"

WORKDIR /app

COPY package.json bun.lock* ./
RUN bun install --frozen-lockfile

COPY . .

RUN bunx @tailwindcss/cli \
    -i ./src/main/resources/static/input.css \
    -o ./src/main/resources/static/output.css

RUN chmod +x ./gradlew
RUN ./gradlew buildFatJar --no-daemon

CMD ["java", "-jar", "build/libs/site.jar"]
