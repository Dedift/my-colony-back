# Architecture

## Module roles

- `app`: composition root. Contains only Spring Boot bootstrap, runtime configuration, entry adapters, and infrastructure wiring.
- `shared`: shared kernel. Contains tiny cross-domain abstractions that are stable and truly common.
- `player`: player bounded context. Owns the player aggregate and player use cases.
- `colony`: colony bounded context. Owns the colony aggregate and colony use cases.
- `auth`: identity bounded context. Owns credentials, login, token issuing, and registration orchestration.

## Allowed dependencies

- `app -> auth, player, colony, shared`
- `auth -> player, colony, shared`
- `player -> shared`
- `colony -> shared`
- `shared -> nothing`

`app` must not be depended on by any feature module.

## Package roles inside feature modules

- `domain`: entities, value objects, domain services, domain exceptions
- `application.port.in`: use cases exposed to other modules or entry adapters
- `application.port.out`: dependencies required by the application layer
- `adapter.in`: controllers, GraphQL resolvers, message consumers
- `adapter.out`: persistence, messaging, external API clients

## Interaction rules

1. Entry adapters call only `application.port.in`.
2. Application services depend only on `domain` and `application.port.out`.
3. Outbound adapters implement `application.port.out`.
4. Cross-module collaboration goes through the target module's `application.port.in`.
5. `app` wires implementations together and owns framework configuration.

## Ownership

- `auth` owns `player_credentials` and token/authentication concerns
- `player` owns `player`
- `colony` owns `colony`
- database migrations may physically live under `app`, but changes should still be authored from the owning module's perspective
