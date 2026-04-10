# Configuration Tracker

This application implements a simple append-only history service for tracking configuration changes.

It supports adding, updating and deleting configuration values, alongside creating and listing configuration
types.

## How to run

Pull this repository and run:

```bash
./gradlew bootRun
```

The service starts on `http://localhost:8080`.

To run the tests:

```bash
./gradlew check
```

### Useful endpoints

- `GET /configuration-types` - list configuration types
- `POST /configuration-types` - create a configuration type
- `GET /configuration-types/{id}` - get a configuration type by id
- `GET /configuration-changes` - list configuration changes (filterable by type and time)
- `POST /configuration-changes` - record a new change (add / update / delete)
- `GET /configuration-changes/{id}` - get a specific change by id
- `GET /health` - health check (Spring Boot Actuator)
- `GET /metrics` - Prometheus metrics
- `GET /h2-console` - H2 database console (for inspection during development)

## Design decisions

### Append-only log

This service is an append-only log. Even a "delete" operation on a configuration does not remove anything -
it writes a new `Removed` state to the history. This gives full transparency on how configurations evolved
over time and makes it possible to roll back any unwanted changes.

### API-first

An `API-first` approach is used: Spring interfaces are generated from the OpenAPI specification
(`src/main/resources/openapi/api.yaml`). This is valuable in production environments where multiple systems
depend on this service: versioning is explicit, you are forced to think twice before changing the contract,
and you get a compile-time guarantee that your code change does not silently break the public API.

### Optimistic locking

The system uses the `optimistic locking` pattern. If two colleagues try to change the same configuration
concurrently, only one will succeed and the other will fail with a conflict. This protects against lost
updates where two decisions are made concurrently based on the same stale state - e.g. current state `A`
allows transitions to both `B` and `C`, but transitioning from `B` to `C` (or from `C` to `B`) afterward
is not a valid business operation.

### Illegal states unrepresentable

All configuration change types (`Added`, `Removed`, `Updated`) are modelled as a sealed type hierarchy in
the Java type system, together with an explicit state machine. This makes illegal states unrepresentable:
for example, it is simply impossible to construct a "remove" change on an already-removed configuration -
the domain layer rejects it rather than silently corrupting history.

### Best-effort notification delivery

The system uses a `best-effort` strategy to avoid side effects on transaction failure. The notification to
the external monitoring service is dispatched via a `@TransactionalEventListener(AFTER_COMMIT)`, so the
notification is only sent once the database transaction has successfully committed. This eliminates the
classic bug where a notification is sent for a change that was later rolled back.

The external call is additionally wrapped in a `resilience4j` retry and circuit breaker
(see `NotificationService`).

If at-least-once delivery of notifications were a hard requirement, this would be a good place to introduce
the `outbox pattern` - persist pending notifications in the same transaction as the business change and
have a scheduled poller deliver them asynchronously. I deliberately did not implement this here to avoid
overengineering a simulated external call, but it is the natural next step for a production system.

## Assumptions

- **Configuration types are never deleted.** For simplicity, I assumed that configuration types are not
  removable. Whether this should be allowed depends on business needs - in some cases it might be
  acceptable to remove an unused configuration type.
- **No strict value validation per configuration type.** It is possible to register new validators for
  specific configuration types, but that was omitted to keep the project small. The hook is there if
  stricter rules are needed later.
- **In-memory persistence via H2.** The assignment asks for in-memory persistence and does not require a
  database. I chose H2 in in-memory mode so I could use JPA, optimistic locking and realistic repository
  code without changing the deployment model.

## Requirements checklist

1. [x] REST API
2. [x] In-memory persistence (H2 in-memory database)
3. [x] [Input validation](src/main/java/com/ohpenl/midoffice/configurationtracker/validator)
4. [x] [Clear error handling](src/main/java/com/ohpenl/midoffice/configurationtracker/problem) - this part was reused from my [other project](https://github.com/JIkvict/JIkvictBackend/tree/main/problem-handling), where I implemented [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457.html)
5. [x] [External integration with a simulated notification receiver](src/main/java/com/ohpenl/midoffice/configurationtracker/client)
6. [x] Health check via Spring Boot Actuator
7. [x] Unit and integration tests
8. [x] README
9. [x] Metrics exposed on `/metrics` (Prometheus)
10. [x] Tracing with OpenTelemetry (correlation ids in log pattern)
11. [x] Retry and circuit breaker on the external call (resilience4j)
12. [x] [Edge case handling via a state machine](src/main/java/com/ohpenl/midoffice/configurationtracker/domain)
