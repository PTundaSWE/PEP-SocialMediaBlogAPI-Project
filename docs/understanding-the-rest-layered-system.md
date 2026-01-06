# Understanding the Rest Layered System

---

This is **core REST / backend architecture knowledge**, and it _will_ come up in interviews. Below is a **clean, practical explanation** of each layer, why it exists, and what _must_ / _must not_ go in it.

---

## 1️⃣ Model (Domain / Entity Layer)

**Purpose:**
Represents the **data structure** of your application.

**What lives here:**

- Entity / POJO classes
- Fields that map to DB columns or JSON fields
- Getters / setters
- `equals()`, `hashCode()`, `toString()`
- Validation annotations (optional)

**Examples:**

```java
public class User {
    private int id;
    private String username;
    private String password;
}
```

**Rules:**

- The model layer does not handle business, SQL, or HTTP logic. It's just pure data representation.

---

## 2️⃣ DAO (Data Access Object)

**Purpose:**
Handles **all database interactions**.

**What lives here:**

- SQL queries
- JDBC / JPA / ORM logic
- CRUD operations
- ResultSet → Model mapping

**Examples:**

```java
User findById(int id);
List<User> findAll();
void save(User user);
```

**Rules:**

- The DAO layer does not handle business or HTTP logic, and does not handle request validation. It's just pure data representation. It's purpose is Only data persistence.

---

## 3️⃣ Service (Business Logic Layer)

**Purpose:**
Contains **business rules and workflows**.

**What lives here:**

- Application rules
- Validation logic (non-HTTP)
- Combining multiple DAO calls
- Transactions

**Examples:**

```java
public User registerUser(User user) {
    if (userExists(user.getUsername())) {
        throw new IllegalArgumentException("Username already taken");
    }
    return userDao.save(user);
}
```

**Rules:**

- ❌ No SQL
- ❌ No HTTP objects
- ✅ Orchestrates DAOs
- ✅ Enforces business rules

---

## 4️⃣ Controller (API / Presentation Layer)

**Purpose:**
Handles **HTTP requests and responses**.

**What lives here:**

- REST endpoints (`@GET`, `@POST`, etc.)
- Request parsing
- Response formatting
- HTTP status codes

**Examples:**

```java
@POST
@Path("/users")
public Response createUser(User user) {
    return Response.status(201).entity(service.registerUser(user)).build();
}
```

**Rules:**

- ❌ No SQL
- ❌ Minimal logic
- ❌ No business rules
- ✅ Delegates to service layer

---

## 5️⃣ Util (Utility / Helper Layer)

**Purpose:**
Provides **reusable helper logic** used across layers.

**What lives here:**

- Hashing / encryption
- Date formatting
- Token generation
- Validation helpers
- Constants

**Examples:**

```java
public class PasswordUtil {
    public static String hash(String rawPassword) { ... }
}
```

**Rules:**

- ❌ No state
- ❌ No DB access
- ✅ Stateless helpers
- ✅ Reusable everywhere

---

## 🔁 Layer interaction flow

```
Client
  ↓
Controller
  ↓
Service
  ↓
DAO
  ↓
Database
```

**Never skip layers.**
(Controller → DAO directly is a design smell.)

---

## 🧠 Why this architecture exists (interview answer)

> Layered architecture enforces **separation of concerns**, improves **testability**, **maintainability**, and allows each layer to evolve independently without breaking others.

---

## 🚫 Common beginner mistakes

| Mistake                 | Why it’s bad   |
| ----------------------- | -------------- |
| SQL in controller       | Tight coupling |
| Business logic in DAO   | Hard to test   |
| HTTP objects in service | Breaks reuse   |
| Models doing logic      | Violates SRP   |

---

## ✅ One-sentence purpose summary (memorize this)

- **Model** → Data representation
- **DAO** → Database access
- **Service** → Business rules
- **Controller** → HTTP handling
- **Util** → Shared helpers

---

If you want, I can:

- map this to **Spring Boot** annotations
- show **Jersey vs Spring MVC** equivalents
- design a **real REST API folder structure**
- refactor an existing messy project into layers

Just say the word.
