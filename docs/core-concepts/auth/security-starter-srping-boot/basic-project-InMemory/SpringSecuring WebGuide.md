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

## Code




### **MvcConfig.java**

```java
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
	}

}
```

This class sets up the basic web page routing for your application using Spring MVC.

- **What it does**:
  
  - It tells the application which web pages (views) to show when a user visits specific URLs.
  - For example:
    - Visiting `/` or `/home` shows the `home` page.
    - Visiting `/hello` shows the `hello` page.
    - Visiting `/login` shows the `login` page.
  - These pages are likely HTML templates (e.g., `home.html`, `login.html`) stored in your project.

- **Key points**:
  
  - The `@Configuration` annotation tells Spring that this class contains configuration settings.
  - It implements `WebMvcConfigurer` to customize how Spring handles web requests.
  - The `addViewControllers` method maps URLs to view names without needing a controller class.

### **WebSecurityConfig.java**

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout((logout) -> logout.permitAll());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
}
```

This class configures Spring Security to control access to your application and manage user authentication.

- **What it does**:
  
  - It defines **security rules** for the app:
    - **Public access**: Anyone can visit `/` and `/home` without logging in.
    - **Restricted access**: All other URLs (e.g., `/hello`) require the user to be logged in.
  - It sets up a **login page** at `/login`, which everyone can access.
  - It enables **logout** functionality, allowing users to log out.
  - It creates a simple **in-memory user** with:
    - Username: `user`
    - Password: `password`
    - Role: `USER`
  - This user is stored in memory (not a database) for testing purposes.

- **Key points**:
  
  - The `@Configuration` and `@EnableWebSecurity` annotations enable Spring Security.
  - The `securityFilterChain` method defines which URLs are public or protected and sets up the login/logout behavior.
  - The `userDetailsService` method creates a basic user for testing, using an in-memory user store with a default password encoder.

### How they work together:

- **MvcConfig** maps URLs to web pages (e.g., `/login` → `login.html`).
- **WebSecurityConfig** secures the app by:
  - Allowing anyone to access `/` and `/home`.
  - Requiring login for other pages (e.g., `/hello`).
  - Providing a login page at `/login` and a simple user (`user`/`password`) for testing.

### Example flow

1. A user visits `/home` → They see the `home` page (no login required).
2. They visit `/hello` → They’re redirected to `/login` because they’re not logged in.
3. They enter `user` and `password` → They’re authenticated and can access `/hello`.
4. They can log out, which is allowed for everyone.

This setup is a basic starting point for a secure web app using Spring Security!



## Outcome & Additional Notes

By following the guide, you create a web application where:

- The homepage is publicly accessible.
- The greeting page (`/hello`) is secured, requiring login.
- Users can log in, view their username, and log out securely.

The guide uses Spring Boot’s auto-configuration to simplify setup.

- Thymeleaf 3.1 requires specific handling for Spring Security integration, avoiding direct access to `HttpServletRequest`.
- For production, replace the in-memory user store with a database or external authentication provider.

For the complete code, refer to the [GitHub repository](https://github.com/spring-guides/gs-securing-web) under `gs-securing-web/complete`.

**Source**: [Spring.io - Securing a Web Application](https://spring.io/guides/gs/securing-web)[](https://github.com/spring-guides/gs-securing-web)