# Project overview

- Java 17 and Spring Boot 3.5.9.
- Maven-based modular monolith organized by business domains such as inventory, product, purchase, sales, and shake.
- Preserve the existing Clean/Hexagonal architectural direction.

# Architecture boundaries

- Domain models must not depend on JPA entities.
- Controllers must depend on use-case interfaces, never directly on JPA repositories.
- JPA entities, mappers, specifications, and repository adapters belong in infrastructure.
- Do not perform architectural refactors outside the requested task.
- If an architectural decision is ambiguous, present the tradeoff and ask before making a broad change.

# Decision ownership

The developer retains ownership of significant architectural, business, and implementation decisions.

Notify the developer and request confirmation before implementing decisions involving:

- Architectural boundaries or dependency direction.
- New architectural patterns.
- Domain rules, invariants, or business behavior.
- Public API contracts or use-case interfaces.
- Persistent data models, entity relationships, or migrations.
- Communication between business domains.
- Transaction boundaries or consistency strategies.
- New frameworks, dependencies, or infrastructure mechanisms.
- Broad implementation choices with meaningful long-term consequences.

When one of these decisions appears, explain:

1. The context and concrete problem.
2. The viable options.
3. The tradeoffs.
4. The recommended option and reasoning.
5. The specific decision needed from the developer.

Do not implement a significant decision until the developer confirms the direction.

Remain autonomous for small, local, reversible implementation decisions that follow established project conventions and do not affect architecture, business behavior, public contracts, or persistence design.

When requested, help document an important decision using:

- Context
- Options
- Tradeoffs
- Decision
- Consequences

# Coding conventions

- Prefer focused, minimal changes.
- Use Java 17-compatible APIs; do not use `List.getFirst()`.
- Follow the conventions of the relevant business domain and surrounding code.
- Do not modify unrelated files or overwrite existing user changes.
- Before editing, inspect the relevant production code and existing tests.

# Testing

- Use JUnit 5 assertions.
- Use Mockito only when isolation is appropriate.
- Repository integration tests use `@DataJpaTest`, Testcontainers, and MySQL.
- Keep tests focused on essential behavior and avoid excessive assertions.
- Run the smallest relevant test set first.

# Safe workflow

- Never commit, push, delete files, or run destructive Git commands unless explicitly requested.
- Preserve existing user changes and work around unrelated modifications.
- Keep investigation and implementation scoped to the requested task.

# Completion report

Explain:

- What changed.
- Why it changed.
- Which validation or tests were run.
