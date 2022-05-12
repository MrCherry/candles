# Candlesticks generation application

## Dependencies
Application uses Redis as a storage of candlesticks.

## How to use
1. Build an image (two options)
   * `/gradlew bootBuildImage` — Gradle task bootBuildImage builds an image builds much faster (recommended).
   * `docker-compose build app` — there is a Dockerfile
2. Set proper value to `TICKS_SOURCE_URL` environment variable in docker-compose.yml.
I left it empty in the repository in order to hide it from the society. =)
3. Run `docker-compose — docker-compose up`
4. Start receiving ticks with `curl -v http://127.0.0.1:8080/start`
5. Wait for it...
6. Request candles with `curl -v http://127.0.0.1:8080/candles?stock=AAPL&from=1652331300&to=1662331300`
7. Enjoy ^_^
8. You can stop receiving ticks with `curl -v http://127.0.0.1:8080/stop`

# Note
There is an intermediate solution in the repository in files:
* `class CandlesServiceRealtimeImpl`.
* `class TicksProcessorRealtime`

It was my first solution. In this case application builds candles in realtime for every request.
Works pretty fast. You can check it by switching `@Primary` annotations to these classes.