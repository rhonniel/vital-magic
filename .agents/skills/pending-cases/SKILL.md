# Pending Cases Skill

## Purpose

Capture findings that should not be implemented immediately but should not be lost.

This skill converts large analysis outputs into a small, actionable backlog of:

1. deferred testing scenarios;
2. decisions that require human evaluation.

The goal is **not** to maximize test coverage or project robustness.

Vital Magic is currently a learning project. Prefer preserving learning momentum and architectural progression over exhaustive hardening.

---

## When to use this skill

Use this skill when analysis reveals:

- relevant test scenarios that are intentionally deferred;
- edge cases that are valid but not currently worth implementing;
- behaviors whose expected result is unclear;
- architectural or domain decisions that require human judgment;
- assumptions that Codex would otherwise need to make;
- findings that should be preserved for future review.

Do not use this skill for issues that:

- are already approved for implementation;
- are clearly bugs that the current task explicitly asks to fix;
- duplicate an existing pending case;
- are purely cosmetic or negligible;
- have already been resolved or discarded.

---

# Categories

## Testing backlog

Destination:

`docs/pending-cases/testing.md`

Use this category when the expected behavior is sufficiently clear but implementing the test is intentionally postponed.

Examples:

- additional boundary scenarios;
- secondary failure paths;
- extra persistence scenarios;
- robustness tests;
- additional integration combinations;
- scenarios useful for higher confidence but unnecessary for the current learning objective.

Each entry must use this structure:

```markdown
## TEST-XXX — Short descriptive name

**Status:** OPEN  
**Area:** module/component  
**Priority:** High | Medium | Low  
**Origin:** analysis/task reference

### Scenario

Briefly describe what should be tested.

### Why it matters

Explain the concrete risk or behavior covered.

### Why deferred

Explain why implementing it is not necessary for the current stage.

### Suggested test level

Unit | MVC | JPA | Integration | End-to-End

### Revisit when

Describe the milestone or situation where this scenario becomes relevant.
```

Keep every entry concise.

---

## Decision backlog

Destination:

`docs/pending-cases/decisions.md`

Use this category whenever proceeding would require Codex to invent, infer, or choose a meaningful project decision.

Codex MUST NOT silently make these decisions.

Examples:

- undefined domain behavior;
- unclear validation ownership;
- ambiguous error handling;
- architecture trade-offs;
- transaction boundaries;
- consistency expectations;
- API behavior;
- persistence decisions;
- assumptions about future scalability;
- uncertainty about what a test should assert.

Each entry must use this structure:

```markdown
## DEC-XXX — Short descriptive question

**Status:** OPEN  
**Area:** module/component  
**Priority:** High | Medium | Low  
**Origin:** analysis/task reference

### Context

Only the minimum context required to understand the decision.

### Decision required

State the decision as one explicit question.

### Why Codex cannot decide this

Explain which product, domain, architecture, or learning trade-off requires human judgment.

### Options

1. Option A — short consequence.
2. Option B — short consequence.
3. Option C — only when genuinely relevant.

### Codex recommendation

Optional.

Provide a recommendation only when evidence strongly favors one option.

Do not treat the recommendation as a decision.

### Impact

Briefly indicate what parts of the project or tests depend on this decision.
```

---

# Consolidation rules

The backlog must remain small and useful.

Before creating a new entry:

1. Search existing pending cases.
2. Merge findings representing the same underlying concern.
3. Prefer one meaningful case over several tiny variations.
4. Do not record every theoretically possible edge case.
5. Record only findings that could reasonably affect future implementation, learning, architecture, or correctness.

A large analysis containing dozens of observations should normally produce only a small number of pending cases.

---

# Learning-project priority

Vital Magic is intended to exercise technologies and architectural concepts.

Current learning progression has priority over exhaustive production hardening.

Examples of higher-value progression include:

- End-to-End testing;
- deployment/cloud;
- distributed systems concepts;
- microservices;
- observability;
- messaging;
- resiliency.

Therefore:

A missing test does NOT automatically mean that it should be implemented now.

Ask:

> Does implementing this scenario teach something important or protect a core behavior required by the current stage?

If no, prefer recording it in the testing backlog.

---

# Human-decision rule

Whenever implementation would require an assumption about meaningful behavior:

**STOP. DO NOT ASSUME.**

Create or update a `DEC-XXX` entry instead.

The agent may explain alternatives and recommend one, but the final decision remains human-owned.

---

# Output behavior

When this skill is explicitly invoked for analysis:

- do not modify production code;
- do not create tests;
- do not resolve pending decisions;
- do not expand the original analysis unnecessarily;
- update only the appropriate pending-case documentation.

At the end, report only:

- number of new testing cases;
- number of new decision cases;
- number of existing cases updated or merged;
- identifiers of the affected cases.

Do not reproduce the entire analysis in the response.