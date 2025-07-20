help:
    @just --list

deps:
    bun install

build:
    bunx @tailwindcss/cli \
        -i ./src/main/resources/static/input.css \
        -o ./src/main/resources/static/output.css

dev: build
    ./gradlew run

format:
    ./gradlew ktlintFormat

lint:
    ./gradlew ktlintCheck

test:
    ./gradlew test

check: format lint test

docker-build:
    docker build -t mvkvc/site:latest .

docker-run:
    docker run -it --network host mvkvc/site:latest
