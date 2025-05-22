# Securing a Web Application

> This guide from Spring.io demonstrates how to secure a web application using **Spring Security** with a Spring MVC application. Below is a concise summary of the key steps and concepts covered:

## Overview

The guide walks through building a Spring MVC application that secures a page with a login form backed by a fixed list of users. It starts with creating an unsecured web application and then adds security using Spring Security.

## Prerequisites

- A Java IDE (e.g., Eclipse, IntelliJ IDEA, or VSCode).
- JDK (Java Development Kit) installed.
- Build tools like Gradle or Maven.
- Access to [Spring Initializr](https://start.spring.io) to set up the project.

## Key Steps

1. **Create an Unsecured Web Application**:
   
   - Use Spring Initializr to generate a project with **Spring Web** and **Thymeleaf** dependencies.
   - Build a simple web application with:
     - A homepage (`home.html`) with a link to a secured greeting page (`/hello`).
     - A controller (`HomeController.java`) to handle requests for the homepage and greeting page.
     - A Thymeleaf template (`hello.html`) to display a greeting.

2. **Add Spring Security**:
   
   - Add Spring Security to the project by including the following dependencies:
     - For Gradle: Add `spring-boot-starter-security`, `thymeleaf-extras-springsecurity6`, and `spring-security-test` in `build.gradle`.
     - For Maven: Add equivalent dependencies in `pom.xml`.
   - Spring Boot automatically secures all HTTP endpoints with basic authentication when Spring Security is on the classpath.

3. **Configure Spring Security**:
   
   - Create a `WebSecurityConfig.java` class annotated with `@EnableWebSecurity`.
   - Configure security to:
     - Protect the `/hello` endpoint, requiring authentication.
     - Allow unauthenticated access to the homepage (`/`).
     - Enable a login form and logout functionality.
     - Define a fixed list of users (e.g., username: `user`, password: `password`) using an in-memory authentication provider.

4. **Integrate Thymeleaf with Spring Security**:
   
   - Use Thymeleaf’s Spring Security integration to display the authenticated username in the greeting page.
   - Implement a logout form that submits a POST request to `/logout`, redirecting to `/login?logout` upon success.

5. **Run and Test the Application**:
   
   - Start the application and access `http://localhost:8080`.
   - Clicking the link to `/hello` redirects to a login page.
   - Log in with the test credentials (`user`/`password`) to access the secured greeting page.
   - Use the logout button to revoke authentication and return to the login page.

## Security Features

- **Authentication**: Ensures only authorized users can access protected resources (e.g., `/hello`).
- **Authorization**: Restricts access based on user roles or credentials.
- **Default Security Headers**: Spring Security adds headers like `Cache-Control`, `X-Content-Type-Options`, `X-Frame-Options`, and `Strict-Transport-Security` (for HTTPS) to enhance security.
- **Customization**: Developers can customize security settings, such as disabling default headers or adding custom ones.

## Outcome

By following the guide, you create a web application where:

- The homepage is publicly accessible.
- The greeting page (`/hello`) is secured, requiring login.
- Users can log in, view their username, and log out securely.

## Additional Notes

- The guide uses Spring Boot’s auto-configuration to simplify setup.
- Thymeleaf 3.1 requires specific handling for Spring Security integration, avoiding direct access to `HttpServletRequest`.
- For production, replace the in-memory user store with a database or external authentication provider.

For the complete code, refer to the [GitHub repository](https://github.com/spring-guides/gs-securing-web) under `gs-securing-web/complete`.

**Source**: [Spring.io - Securing a Web Application](https://spring.io/guides/gs/securing-web)[](https://github.com/spring-guides/gs-securing-web)