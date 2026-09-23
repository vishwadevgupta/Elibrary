# eLibrary — Java Servlet + Oracle Library Management System

A responsive library management application built with Java Servlets, JDBC and Oracle Database. It provides separate Admin and Librarian workspaces for librarian management, book inventory, issue/return workflows and circulation tracking.

## Highlights

- Responsive modern UI with reusable CSS
- Separate Admin and Librarian roles
- Oracle JDBC persistence
- PBKDF2 password hashing with per-password salt
- Automatic migration of legacy plaintext librarian passwords after successful login
- Environment-based database and admin credentials
- Transaction-safe book issue and return operations
- Server-side validation and safe HTML escaping
- POST-only destructive actions
- Book search/filter
- Session timeout and session renewal after login

## Tech stack

| Layer | Technology |
|---|---|
| Backend | Java Servlets, JDBC |
| Database | Oracle XE / Oracle Database |
| Frontend | HTML, CSS, Bootstrap |
| Runtime | Apache Tomcat 9+ |
| Java | JDK 8+ |

## Project structure

- src/com/javatpoint/beans — application data beans
- src/com/javatpoint/dao — JDBC data-access layer
- src/com/javatpoint/servlets — HTTP endpoints
- src/com/javatpoint/util — authentication, password hashing and HTML helpers
- WebContent — static UI, forms, CSS, images and Oracle schema

## Run locally

### 1. Install prerequisites

Install JDK 8+, Apache Tomcat 9+, Oracle XE (or another Oracle Database), and an IDE such as Eclipse Enterprise Java.

Also obtain a compatible Oracle JDBC driver (ojdbc8/ojdbc11 appropriate for your JDK). The old bundled ojdbc14.jar is obsolete for modern JDKs and is not recommended.

### 2. Create a dedicated database user

Do not run the application with the Oracle SYSTEM account.

~~~sql
CREATE USER elibrary IDENTIFIED BY "change-me";
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO elibrary;
~~~

Connect as elibrary and run WebContent/tablesoracle.sql.

### 3. Configure environment variables

Before starting Tomcat, configure:

~~~text
ELIBRARY_DB_URL=jdbc:oracle:thin:@localhost:1521:xe
ELIBRARY_DB_USER=elibrary
ELIBRARY_DB_PASSWORD=change-me
ELIBRARY_ADMIN_EMAIL=admin@example.com
ELIBRARY_ADMIN_PASSWORD=change-this-password
~~~

Never commit real credentials to Git.

### 4. Import and deploy

In Eclipse:

1. Import as an Existing Dynamic Web Project if the project metadata is available.
2. Otherwise create a Dynamic Web Project, use src as the Java source folder and WebContent as the web content folder.
3. Add your compatible Oracle JDBC driver to WebContent/WEB-INF/lib.
4. Add the project to a Tomcat 9 server.
5. Start Tomcat.

### 5. Open the application

Open http://localhost:8080/Elibrary/

Sign in using the admin credentials from ELIBRARY_ADMIN_EMAIL and ELIBRARY_ADMIN_PASSWORD. Create librarian accounts from the Admin workspace.

## Main routes

- / — sign-in page
- /AdminLogin — admin authentication
- /LibrarianLogin — librarian authentication
- /ViewLibrarian — admin librarian management
- /ViewBook — book inventory
- /IssueBookForm — issue workflow
- /ViewIssuedBook — circulation history
- /ReturnBookForm — return workflow

## Security notes

Credentials are loaded from environment variables rather than source code. Librarian passwords are stored as PBKDF2-HMAC-SHA256 hashes with random salts. Existing legacy plaintext passwords are upgraded after a successful login.

For production, use a managed secret store, HTTPS, and a least-privilege database account.

## Troubleshooting

**Oracle driver not found:** add a JDBC driver compatible with your JDK to WEB-INF/lib and restart Tomcat.

**Database credentials error:** verify the three ELIBRARY_DB_* variables are visible to the Tomcat process.

**Admin login does not work:** verify ELIBRARY_ADMIN_EMAIL and ELIBRARY_ADMIN_PASSWORD.

**Book cannot be issued:** the application checks availability inside a transaction; confirm the book exists and has an available copy.

## Deployment note

This is a traditional Servlet/JDBC application, not Spring Boot. GitHub Pages cannot host the Java backend. Deploy it to Tomcat or another Servlet container connected to Oracle.

## License

Add the project's preferred license before public distribution.
