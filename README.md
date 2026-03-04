# enomy-finances-system

Production-ready Spring Boot 3 (Java 17) financial services web application for **Enomy-Finances**.

## Tech Stack
- Java 17
- Spring Boot 3.3+
- Spring MVC + Thymeleaf
- Spring Security (BCrypt, CSRF, role-based auth)
- Spring Data JPA + MySQL
- SLF4J + Logback
- Maven
- Embedded Tomcat (WAR-compatible)
- JUnit 5 + Mockito

## Prerequisites
- Java 17+
- Maven 3.9+
- MySQL 8+

## MySQL Setup
```sql
CREATE DATABASE enomy_finances;
```

Default app credentials are read from environment variables with fallback values.

## Environment Variables
- `DB_HOST` (default: `localhost`)
- `DB_PORT` (default: `3306`)
- `DB_NAME` (default: `enomy_finances`)
- `DB_USERNAME` (default: `root`)
- `DB_PASSWORD` (default: `root`)
- `EXCHANGE_API_KEY` (required for live rates)

## Run Locally
```bash
mvn clean spring-boot:run
```
Then open: `http://localhost:8080`

## Build WAR
```bash
mvn clean package
```
WAR output:
`target/enomy-finances-system-1.0.0.war`

## Deploy to External Tomcat
1. Build WAR: `mvn clean package`
2. Copy WAR to `${TOMCAT_HOME}/webapps/`
3. Start Tomcat.
4. Access app at: `http://<host>:<port>/enomy-finances-system-1.0.0/`

## Security & Production Considerations
- Replace default DB credentials with secure secrets.
- Set a valid exchange API key.
- Use HTTPS via reverse proxy/load balancer.
- Configure MySQL backups and connection pool tuning.
- Set `spring.thymeleaf.cache=true` in production.
- Centralize logs with ELK/Cloud logging.
- Add rate-limiting and CAPTCHA for auth endpoints in internet-facing deployments.

## Testing
```bash
mvn test
```

## Functional Highlights
- User registration and login with role-based access (`ADMIN`, `CLIENT`)
- Currency conversion with transaction bounds and configurable fee
- Exchange-rate caching with graceful fallback
- Investment projections (1/5/10 years) for 3 investment products
- Global exception handling + DB/file fallback error logging
