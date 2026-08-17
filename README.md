# Minesweeper - Full Stack Coding Exercise

## Context

You have joined a team developing web applications for NHS healthcare organisations.

Your task is to extend and improve a small Minesweeper application.

The application consists of:

- React frontend
- Java REST API
- PostgreSQL database
- Automated tests
- Postman API collection

The exercise is intentionally incomplete.

## Objective

Build a production-quality Minesweeper experience while demonstrating:

- Java development
- React development
- REST API design
- SQL/database knowledge
- Unit testing
- Error handling
- Accessibility
- Problem solving
- Clean code
- Debugging
- Communication

You should explain your decisions as you work.

---

## Running the application

### Requirements

- Java 21+
- Maven 3.9+
- Node.js 20+
- Docker / Docker Compose

### Start PostgreSQL
# 1. Start PostgreSQL
docker compose up -d

# 2. Check PostgreSQL
docker compose ps

# 3. Start backend
./mvnw spring-boot:run

```bash
docker compose up postgres



### Future Architecture

                 ┌─────────────────┐
                 │ React Frontend  │
                 └────────┬────────┘
                          │
                       REST API
                          │
                 ┌────────▼────────┐
                 │ Game Controller │
                 └────────┬────────┘
                          │
                 ┌────────▼────────┐
                 │   Game Service  │
                 └────────┬────────┘
                          │
               ┌──────────┴──────────┐
               │                     │
        ┌──────▼──────┐       ┌──────▼──────┐
        │ Game Store  │       │ Game Events │
        └──────┬──────┘       └─────────────┘
               │
        ┌──────▼──────┐
        │ PostgreSQL  │
        └─────────────┘
