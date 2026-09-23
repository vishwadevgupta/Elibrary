# 📚 eLibrary

> A modern Java Servlet + Oracle library management system for books, librarians, and circulation.

![Java](https://img.shields.io/badge/Java-8%2B-orange)
![Tomcat](https://img.shields.io/badge/Tomcat-9%2B-yellow)
![Oracle](https://img.shields.io/badge/Database-Oracle-red)

## ✨ What can you do?

### 🛡️ Admin
- Create, edit and remove librarian accounts
- Manage administrative access

### 📖 Librarian
- Add and search books
- Track inventory and issued copies
- Issue books to students
- Record returns
- View circulation history

### 🔐 Security
- PBKDF2-HMAC-SHA256 password hashing with random salts
- Environment-based credentials
- Role-based access checks
- Session timeout and session renewal
- Server-side validation and HTML escaping
- Transaction-safe issue and return operations
- POST-based destructive actions

## 🧭 Quick start

| Goal | Section |
|---|---|
| 🚀 Run locally | [Run it locally](#-run-it-locally) |
| 🗄️ Set up Oracle | [Database setup](#1️⃣-prepare-oracle) |
| 🔑 Configure secrets | [Application configuration](#2️⃣-configure-the-application) |
| 🌐 Open the app | [Local URL](#5️⃣-open-the-application) |
| 🛠️ Fix problems | [Troubleshooting](#-troubleshooting) |

## 🚀 Run it locally

> **Important:** eLibrary is a Java web application. Tomcat and Oracle must be running before the browser can use it.

### 1️⃣ Prepare Oracle

Create a dedicated application user. Do not use the Oracle SYSTEM account.

```sql
CREATE USER elibrary IDENTIFIED BY "change-me";
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO elibrary;
```

Then connect as `elibrary` and run:

```text
WebContent/tablesoracle.sql
```

### 2️⃣ Configure the application

Set these environment variables before starting Tomcat:

```text
ELIBRARY_DB_URL=jdbc:oracle:thin:@localhost:1521:xe
ELIBRARY_DB_USER=elibrary
ELIBRARY_DB_PASSWORD=change-me
ELIBRARY_ADMIN_EMAIL=admin@example.com
ELIBRARY_ADMIN_PASSWORD=change-this-password
```

### 🤔 Why is `localhost` in the database URL?

That `localhost` is the **database host**, not the public website address.

```text
Java application ──► localhost:1521 ──► Oracle XE
```

If Oracle is running on another machine, replace `localhost` with that database server's hostname or IP.

Never commit real credentials to GitHub.

### 3️⃣ Install the dependencies

You need the following components before running eLibrary:

| Dependency | Required? | Purpose |
|---|---|---|
| **JDK 8+** | ✅ Yes | Compile and run the Java application |
| **Apache Tomcat 9+** | ✅ Yes | Runs the Servlet web application |
| **Oracle Database / Oracle XE** | ✅ Yes | Stores books, librarians and issue records |
| **Oracle JDBC Driver** | ✅ Yes | Connects Java to Oracle |
| **Eclipse Enterprise Java / IntelliJ IDEA** | ⚪ Optional | Recommended for easier development |

#### ☕ 3.1 Install Java JDK

Install **JDK 8 or newer** and verify it:

```bash
java -version
javac -version
```

Both commands should return a Java version.

#### 🐱 3.2 Install Apache Tomcat

Install **Apache Tomcat 9 or newer**.

After installation, verify that Tomcat can start successfully. The default web port is usually:

```text
8080
```

You can change this later in Tomcat's `server.xml`.

#### 🗄️ 3.3 Install Oracle Database

Install **Oracle XE** for a simple local setup, or use another compatible Oracle Database instance.

After Oracle is running:

1. Create the `elibrary` database user from the previous step.
2. Run `WebContent/tablesoracle.sql` using that user.
3. Make sure the database host, port and service name match `ELIBRARY_DB_URL`.

#### 🔌 3.4 Install the Oracle JDBC driver

The application requires an Oracle JDBC driver because Java connects to Oracle through JDBC.

The old `ojdbc14.jar` is intentionally **not included** in this repository.

Download a current Oracle JDBC driver compatible with your JDK, such as:

- `ojdbc8.jar` for Java 8-compatible environments
- `ojdbc11.jar` for Java 11+ environments

Then copy the JAR into:

```text
WebContent/WEB-INF/lib/
```

Your project should look similar to:

```text
Elibrary/
└── WebContent/
    └── WEB-INF/
        └── lib/
            └── ojdbc8.jar
```

> **Important:** Do not commit the JDBC driver to Git if your organization's or Oracle's distribution terms do not permit it. For local development, place the JAR in the project as described above.

#### 🧰 3.5 IDE (optional)

You can use **Eclipse Enterprise Java** or **IntelliJ IDEA** to work with the project.

The IDE is not the application runtime. The important runtime pieces are:

```text
JDK + Tomcat + Oracle + Oracle JDBC Driver
```

### 4️⃣ Deploy with Tomcat

1. Import the repository as an Existing Dynamic Web Project if supported by your IDE.
2. Otherwise create a Dynamic Web Project using `src` as the Java source folder and `WebContent` as the web content folder.
3. Add the Oracle JDBC driver.
4. Configure Apache Tomcat.
5. Add Elibrary to the Tomcat server.
6. Start Tomcat.

## 🌐 5️⃣ Open the application

After Tomcat is running, open:

```text
http://localhost:8080/Elibrary/
```

### What does this URL mean?

| Part | Meaning |
|---|---|
| `http://` | Browser protocol |
| `localhost` | Your own computer |
| `8080` | Typical Tomcat HTTP port |
| `/Elibrary/` | Deployed application context |

### ⚠️ When should you use this link?

**Only when you are running eLibrary locally.**

Example:

```text
Your computer
   ├── Tomcat :8080
   ├── Elibrary deployed
   └── Oracle
          │
          ▼
http://localhost:8080/Elibrary/
```

`localhost` is **not a live/public URL**. Another person cannot open your local application using that address.

> **Public demo:** There is currently no public/live demo deployed for this repository.

If you deploy the application to a server later, the address will use that server's domain, for example `https://your-domain.com/Elibrary/`.

### 🔁 If Tomcat uses another port

If Tomcat is configured for port `9090`, use:

```text
http://localhost:9090/Elibrary/
```

## 🧪 Try the application

### 👑 Admin flow
1. Sign in using `ELIBRARY_ADMIN_EMAIL` and `ELIBRARY_ADMIN_PASSWORD`.
2. Open **Add Librarian**.
3. Create a librarian account.

### 📚 Librarian flow
1. Sign in with the librarian account.
2. Add a book.
3. Search the inventory.
4. Issue a book.
5. View issued books.
6. Return the book.

## 🧩 How it works

```text
Browser
   │
   ▼
HTML / CSS / Bootstrap
   │
   ▼
Java Servlets
   │
   ├── Authentication
   ├── Authorization
   └── Validation
   │
   ▼
DAO Layer
   │
   ▼
JDBC
   │
   ▼
Oracle Database
```

The project intentionally uses a traditional Servlet + JDBC architecture rather than Spring Boot, making the request-to-database flow easy to follow.

## 🏗️ Project structure

```text
Elibrary/
├── WebContent/       → UI, forms, CSS, images and SQL
├── src/
│   └── com/javatpoint/
│       ├── beans/    → Data models
│       ├── dao/      → Database operations
│       ├── servlets/ → HTTP workflows
│       └── util/     → Security and helpers
├── .gitignore
└── README.md
```

## 🔐 Security architecture

New librarian passwords are stored as PBKDF2-HMAC-SHA256 hashes with unique random salts.

```text
Password → PBKDF2 + random salt → Hash → Oracle
```

Legacy plaintext librarian passwords are upgraded after a successful login.

Admin and librarian routes also verify the correct session role before allowing protected actions.

## 📦 Main routes

| Route | Purpose | Access |
|---|---|---|
| `/` | Login page | Public |
| `/AdminLogin` | Admin authentication | Public |
| `/LibrarianLogin` | Librarian authentication | Public |
| `/ViewLibrarian` | Librarian management | Admin |
| `/AddLibrarianForm` | Create librarian | Admin |
| `/ViewBook` | Book inventory | Librarian |
| `/AddBookForm` | Add book | Librarian |
| `/IssueBookForm` | Issue book | Librarian |
| `/ViewIssuedBook` | Circulation history | Librarian |
| `/ReturnBookForm` | Return book | Librarian |

## 🛠️ Troubleshooting

**Oracle JDBC driver not found**  
Add a JDBC driver compatible with your JDK to `WebContent/WEB-INF/lib/` and restart Tomcat.

**Database connection fails**  
Check `ELIBRARY_DB_URL`, `ELIBRARY_DB_USER` and `ELIBRARY_DB_PASSWORD`, and verify that Oracle is running.

**Admin login fails**  
Check `ELIBRARY_ADMIN_EMAIL` and `ELIBRARY_ADMIN_PASSWORD` in the environment used by Tomcat.

**Book cannot be issued**  
Confirm that the book exists and at least one copy is available.

**localhost:8080 does not open**  
Check that Tomcat is running, Elibrary is deployed, the context name is correct, and Tomcat is actually using port 8080.

## 🚀 Production deployment

For a real deployment, the architecture becomes:

```text
Internet → HTTPS / Domain → Reverse Proxy → Tomcat → eLibrary → Oracle
```

For production, use HTTPS, a secret manager, a least-privilege database account, a production JDBC driver, and do not expose Oracle directly to the internet.

## 📌 Current status

| Item | Status |
|---|---|
| Application | Java Servlet + Oracle |
| Runtime | Apache Tomcat |
| Database | Oracle |
| Local demo | ✅ Supported |
| Public/live demo | ❌ Not currently deployed |

## 📄 License

Add the project's preferred license before public distribution.