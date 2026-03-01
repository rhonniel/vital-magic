## Project Overview

VitalMagic is a backend system designed to manage a smoothie shop.

The system handles inventory, product composition and sales, while tracking
custom attributes such as Strength, Speed and Vitality for each shake.

The goal is to model the business logic of the shop in a structured way,
allowing the system to grow from a small MVP into a more complex e-commerce platform over time.

## Architecture Overview

The long-term goal is to evolve toward a microservices architecture.
However, the system grows according to real needs.

### Evolution Strategy

- **Stage 1 – Small Shop (MVP)**
  - Layered monolith
  - Clear separation between main domain entities
  - Focus on business correctness

- **Stage 2 – Growing E-commerce**
  - Modular monolith
  - Strong module boundaries
  - Explicit domain ownership

- **Stage 3 – Large Scale E-commerce**
  - Microservices architecture
  - Service extraction based on domain boundaries
  - Independent scalability
 

## Domain Modules

### Inventory Module
- Item management
- Attribute modeling
- Stock control logic

### Shake Module
- Shake composition
- Aggregated attribute calculation
- Business validation rules

### Sales Module
- Sale transaction handling
- Snapshot-based historical preservation
- Immutable sale records

### Product Module
- Defines sellable merchandise
- Connects domain entities to commercial representation

### Purchase Module
- Manages stock replenishment
- Introduces unit cost tracking into the system


