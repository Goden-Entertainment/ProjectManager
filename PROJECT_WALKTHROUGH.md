# Project Management System - Complete Code Walkthrough

> **For Beginners to Spring Framework**
> This guide explains how the code flows from one end to the other, with method-level detail and code examples.

---

## Table of Contents

1. [Introduction](#introduction)
2. [Spring Framework Primer](#spring-framework-primer)
3. [Database Architecture](#database-architecture)
4. [Feature Walkthroughs](#feature-walkthroughs)
   - [Authentication Flow](#authentication-flow)
   - [User Management](#user-management)
   - [Project Management](#project-management)
   - [SubProject Management](#subproject-management)
   - [Task Management](#task-management)
   - [Subtask Management](#subtask-management)
   - [Team Management](#team-management)
5. [Key Architecture Patterns](#key-architecture-patterns)
6. [Common Patterns Reference](#common-patterns-reference)
7. [Navigation Guide](#navigation-guide)
8. [Extension Examples](#extension-examples)
9. [Troubleshooting](#troubleshooting)
10. [Glossary](#glossary)

---

## Introduction

### Project Overview

This is a **Scrum/Project Management system** built for Alpha Solutions using:
- **Java 21**
- **Spring Boot 3.5.7** (Spring Web + JDBC + Thymeleaf)
- **MySQL database**
- **Maven** build system

### Quick Start

**Build the project:**
```bash
mvn clean install
```

**Run the application:**
```bash
mvn spring-boot:run
```

**Run tests:**
```bash
mvn test
```

### Architecture Overview

This project follows a **3-layer Spring MVC architecture**:

```
┌─────────────────────────────────────────┐
│         Controller Layer                │  ← Handles HTTP requests
│  (@Controller, @GetMapping, @PostMapping) │
└──────────────┬──────────────────────────┘
               ↓
┌──────────────────────────────────────────┐
│          Service Layer                   │  ← Business logic
│            (@Service)                    │
└──────────────┬───────────────────────────┘
               ↓
┌──────────────────────────────────────────┐
│        Repository Layer                  │  ← Database access
│   (@Repository, JdbcTemplate)            │
└──────────────┬───────────────────────────┘
               ↓
┌──────────────────────────────────────────┐
│          MySQL Database                  │
└──────────────────────────────────────────┘
```

**Request Flow Example:**
```
User clicks "Login" button
  → Browser sends POST /user/login
    → UserController.authenticateUser() receives request
      → UserService.login() validates credentials
        → UserRepository.findUser() queries database
          → Database returns user data
        → Repository converts ResultSet to User object
      → Service returns User object
    → Controller creates session
  → Browser redirects to /user/profile
```

### File Structure

```
ProjectManager/
├── src/main/java/org/example/projectmanager/
│   ├── controller/          ← HTTP request handlers
│   ├── service/             ← Business logic layer
│   ├── repository/          ← Database queries (JdbcTemplate)
│   ├── model/               ← POJOs (Plain Old Java Objects)
│   └── exceptions/          ← Custom exceptions
├── src/main/resources/
│   ├── templates/           ← Thymeleaf HTML views
│   ├── SQL-scripts/         ← Database schema & initial data
│   ├── static/              ← CSS, JS, images
│   └── application.properties  ← Spring configuration
└── pom.xml                  ← Maven dependencies
```

---

## Spring Framework Primer

If you're new to Spring, here are the key concepts used in this project:

### 1. Dependency Injection (DI)

**What it is:** Spring automatically creates and connects objects for you.

**Example from UserController.java:**
```java
@Controller
public class UserController {
    private UserService userService;  // We need this

    // Spring calls this constructor and provides UserService automatically
    public UserController(UserService userService) {
        this.userService = userService;  // Spring "injects" the dependency
    }
}
```

**Why it's useful:** You don't need to write `new UserService()`. Spring manages object creation and lifecycle.

---

### 2. Annotations Explained

**`@Controller` - Marks a class as a web request handler**
```java
@Controller              // Spring: "This handles web requests"
@RequestMapping("user")  // All endpoints start with /user
public class UserController {
    // Methods handle specific URLs
}
```

**`@Service` - Marks a class as business logic layer**
```java
@Service  // Spring: "This contains business logic"
public class UserService {
    // Methods contain business rules
}
```

**`@Repository` - Marks a class as database access layer**
```java
@Repository  // Spring: "This talks to the database"
public class UserRepository {
    JdbcTemplate jdbcTemplate;  // Spring provides this for SQL queries
}
```

**`@GetMapping` / `@PostMapping` - Maps URLs to methods**
```java
@GetMapping("/login")  // Handles: GET http://localhost:8080/user/login
public String login() {
    return "login";  // Returns the name of a Thymeleaf template (login.html)
}

@PostMapping("/login")  // Handles: POST http://localhost:8080/user/login
public String authenticateUser(@RequestParam String username, @RequestParam String password) {
    // Processes login form submission
}
```

**`@PathVariable` - Extracts values from URL**
```java
@GetMapping("/edit/{userId}")  // URL: /user/edit/42
public String editUser(@PathVariable int userId) {
    // userId = 42 (extracted from URL)
}
```

**`@ModelAttribute` - Binds form data to Java object**
```java
@PostMapping("/add")
public String createUser(@ModelAttribute User user) {
    // Spring automatically fills user object from form fields
    // If form has <input name="username">, it calls user.setUsername(value)
}
```

---

### 3. JdbcTemplate - Database Access

**What it is:** A Spring class that simplifies database operations without needing an ORM (Object-Relational Mapping) framework.

**Example - Inserting data:**
```java
String sql = "INSERT INTO USERS (userName, userPassword) VALUES (?, ?)";
jdbcTemplate.update(sql, "john_doe", "password123");
```

**Example - Querying data:**
```java
String sql = "SELECT * FROM USERS WHERE user_id = ?";
User user = jdbcTemplate.queryForObject(sql, new Object[]{userId},
    (rs, rowNum) -> {
        // This lambda is called a "RowMapper"
        // It converts database row (ResultSet) into a Java object
        return new User(
            rs.getInt("user_id"),
            rs.getString("userName"),
            rs.getString("userPassword"),
            // ... other fields
        );
    });
```

**The `?` placeholders** prevent SQL injection attacks. Spring safely replaces them with your values.

---

### 4. HttpSession - User Login State

**What it is:** A way to remember information about a user across multiple requests.

**Setting session data (on login):**
```java
@PostMapping("/login")
public String authenticateUser(HttpSession session) {
    User user = userService.login(username, password);
    session.setAttribute("user", user);  // Save user in session
    return "redirect:/user/profile";
}
```

**Reading session data (in other endpoints):**
```java
@GetMapping("/profile")
public String profile(HttpSession session) {
    User user = (User) session.getAttribute("user");  // Get user from session
    if (user == null) {
        return "redirect:/user/login";  // Not logged in
    }
    // User is logged in, show profile
}
```

**Clearing session (on logout):**
```java
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();  // Clear all session data
    return "redirect:/user/login";
}
```

---

### 5. Thymeleaf - HTML Templates

**What it is:** A template engine that generates dynamic HTML using Java objects.

**Passing data from Controller to Template:**
```java
@GetMapping("/profile")
public String profile(Model model) {
    List<User> userList = userService.getUsers();
    model.addAttribute("userList", userList);  // Make userList available in template
    return "adminProfile";  // Renders: templates/adminProfile.html
}
```

**Using data in Template (adminProfile.html):**
```html
<table>
    <tr th:each="user : ${userList}">  <!-- Loop through userList -->
        <td th:text="${user.username}">Username</td>  <!-- Display user.username -->
        <td th:text="${user.email}">Email</td>
    </tr>
</table>
```

**Form binding with Thymeleaf:**
```html
<form th:action="@{/user/addNewUser}" th:object="${newUser}" method="post">
    <!-- th:field="*{username}" binds to newUser.getUsername()/setUsername() -->
    <input type="text" th:field="*{username}" required>
    <button type="submit">Create User</button>
</form>
```

When form is submitted, Spring automatically calls `newUser.setUsername(value)` for each field.

---

## Database Architecture

### Schema Overview

The database has **6 core tables** and **2 junction tables** for relationships.

```
                    ┌─────────────┐
                    │   USERS     │
                    │ user_id (PK)│
                    │ userType    │───┐
                    │ team_id (FK)│   │
                    └──────┬──────┘   │
                           │          │
                           ↓          │
                    ┌─────────────┐   │
                    │    TEAM     │←──┘
                    │ team_id(PK) │
                    │ project_id  │───────┐
                    │ sub_project_id│──┐  │
                    │ task_id     │─┐ │  │
                    └─────────────┘ │ │  │
                                    │ │  │
         ┌──────────────────────────┼─┼──┘
         │                          │ │
         ↓                          │ │
┌──────────────────┐                │ │
│     PROJECT      │                │ │
│  project_id (PK) │                │ │
└────────┬─────────┘                │ │
         │ (1:M)                    │ │
         ↓                          │ │
┌──────────────────┐                │ │
│   SUBPROJECT     │                │ │
│sub_project_id(PK)│                │ │
│  project_id (FK) │                │ │
└────────┬─────────┘                │ │
         │ (1:M)                    │ │
         ↓                          │ │
┌──────────────────┐                │ │
│      TASK        │←───────────────┘ │
│   task_id (PK)   │                  │
│sub_project_id(FK)│                  │
└────────┬─────────┘                  │
         │ (1:M)                      │
         ↓                            │
┌──────────────────┐                  │
│    SUBTASK       │←─────────────────┘
│ sub_task_id (PK) │
│   task_id (FK)   │
└──────────────────┘
```

**Junction Tables (Many-to-Many):**

```
USERS_PROJECT              USERS_SUBTASK
┌─────────────────┐       ┌──────────────────┐
│ user_id (FK)    │       │ user_id (FK)     │
│ project_id (FK) │       │ sub_task_id (FK) │
└─────────────────┘       └──────────────────┘
     │       │                 │         │
     ↓       ↓                 ↓         ↓
  USERS   PROJECT           USERS    SUBTASK
```

---

### Primary Key Naming Convention

**Important:** All primary keys use `snake_case` (with underscores):

- `user_id` (not userId)
- `project_id` (not projectId)
- `sub_project_id` (not subProjectId)
- `task_id` (not taskId)
- `sub_task_id` (not subTaskId)
- `team_id` (not teamId)

**Other columns** use `camelCase`:
- `userName`, `userEmail`, `userPassword`
- `projectName`, `projectDescription`
- `estimatedTime`, `actualTime`

---

### Foreign Key Relationships & Cascade Behavior

**One-to-Many (with CASCADE DELETE):**

```sql
-- SubProject belongs to one Project
SUBPROJECT (
    sub_project_id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
)
```

**What `ON DELETE CASCADE` means:**
When you delete a Project, all its SubProjects are automatically deleted too.

**Cascade Chain:**
```
DELETE PROJECT (id=5)
  → Automatically DELETE SUBPROJECT (project_id=5)
    → Automatically DELETE TASK (sub_project_id matches)
      → Automatically DELETE SUBTASK (task_id matches)
```

**Many-to-Many (with Junction Table):**

```sql
USERS_PROJECT (
    user_id INT NOT NULL,
    project_id INT NOT NULL,
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
)
```

**Why junction tables exist:**
- One project manager can manage multiple projects
- One project can be managed by multiple project managers
- Junction table stores all (PM, Project) pairs

---

### Complete Table Definitions

```sql
-- Core entity tables
CREATE TABLE PROJECT (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    projectName VARCHAR(255) NOT NULL,
    projectDescription TEXT,
    status VARCHAR(255),
    priority VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    startDate DATE,
    endDate DATE
);

CREATE TABLE SUBPROJECT (
    sub_project_id INT AUTO_INCREMENT PRIMARY KEY,
    subProjectName VARCHAR(255) NOT NULL,
    subProjectDescription TEXT,
    team VARCHAR(255),
    status VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    startDate DATE,
    endDate DATE,
    project_id INT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
);

CREATE TABLE TASK (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    taskName VARCHAR(255) NOT NULL,
    taskDescription TEXT,
    status VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    priority VARCHAR(255),
    startDate DATE,
    endDate DATE,
    sub_project_id INT NOT NULL,
    FOREIGN KEY (sub_project_id) REFERENCES SUBPROJECT(sub_project_id) ON DELETE CASCADE
);

CREATE TABLE SUBTASK (
    sub_task_id INT AUTO_INCREMENT PRIMARY KEY,
    subTaskName VARCHAR(255),
    subTaskDescription TEXT,
    status VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    priority VARCHAR(255),
    startDate DATE,
    endDate DATE,
    task_id INT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES TASK(task_id) ON DELETE CASCADE
);

CREATE TABLE TEAM (
    team_id INT AUTO_INCREMENT PRIMARY KEY,
    teamName VARCHAR(255) NOT NULL,
    teamDescription TEXT,
    project_id INT,
    sub_project_id INT,
    task_id INT,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE SET NULL,
    FOREIGN KEY (sub_project_id) REFERENCES SUBPROJECT(sub_project_id) ON DELETE SET NULL,
    FOREIGN KEY (task_id) REFERENCES TASK(task_id) ON DELETE SET NULL
);

CREATE TABLE USERS (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(255) UNIQUE NOT NULL,
    userEmail VARCHAR(255),
    userPassword VARCHAR(255),
    userType ENUM('ADMIN', 'PROJECTMANAGER', 'DEV') NOT NULL DEFAULT 'DEV',
    devType ENUM('FRONTEND', 'BACKEND', 'FULLSTACK'),
    workTime INT,
    team_id INT,
    FOREIGN KEY (team_id) REFERENCES TEAM(team_id) ON DELETE SET NULL
);

-- Junction tables for many-to-many relationships
CREATE TABLE USERS_PROJECT (
    user_id INT NOT NULL,
    project_id INT NOT NULL,
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
);

CREATE TABLE USERS_SUBTASK (
    user_id INT NOT NULL,
    sub_task_id INT NOT NULL,
    PRIMARY KEY (user_id, sub_task_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (sub_task_id) REFERENCES SUBTASK(sub_task_id) ON DELETE CASCADE
);
```

---

## Feature Walkthroughs

Now let's walk through each feature, following the request flow from the user's browser all the way to the database.

---

### Authentication Flow

**User Story:** A user wants to log in to the system.

#### Step 1: User Visits Login Page

**URL:** `GET /user/login`

**Controller Method (UserController.java:84-91):**
```java
@GetMapping("/login")
public String login(HttpSession session) {
    if (session.getAttribute("user") != null) {
        return "redirect:/user/profile";  // Already logged in
    }

    return "login";  // Show login form
}
```

**What happens:**
1. User navigates to `http://localhost:8080/user/login`
2. Spring calls `UserController.login()`
3. Checks if user is already logged in (session has "user" attribute)
4. If yes → redirect to profile
5. If no → return "login" template name
6. Spring renders `src/main/resources/templates/login.html`

---

#### Step 2: User Submits Login Form

**URL:** `POST /user/login`

**Controller Method (UserController.java:93-110):**
```java
@PostMapping("/login")
public String authenticateUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session) {
    try {
        // Step 1: Validate credentials
        User user = userService.login(username, password);

        // Step 2: If valid, create session
        if (user != null) {
            session.setAttribute("user", user);  // Save user in session
            return "redirect:/user/profile";
        }

    } catch (DataAccessException e) {
        throw new DatabaseOperationException("Database fejl ved autentificering", e);
    }

    return "redirect:/user/login";  // Login failed
}
```

**Flow:**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /user/login     │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  username="admin"     │                      │                     │                    │
   │  password="admin123"  │                      │                     │                    │
   │                       │   login(username,    │                     │                    │
   │                       │   password)          │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  findUser(username) │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  USERS WHERE       │
   │                       │                      │                     │  userName=?        │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (user row data)  │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   User object       │                    │
   │                       │                      │  Check password     │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   User object        │                     │                    │
   │                       │  session.setAttribute│                     │                    │
   │<──────────────────────┤  ("user", user)      │                     │                    │
   │  redirect:/user/profile                     │                     │                    │
```

**Service Layer (UserService.java):**
```java
public User login(String username, String password) {
    User user = userRepository.findUser(username);  // Get user from database

    if (user != null && user.getPassword().equals(password)) {
        return user;  // Password matches
    }

    throw new ProfileNotFoundException("Invalid username or password");
}
```

**Repository Layer (UserRepository.java):**
```java
public User findUser(String username) {
    String sql = "SELECT * FROM USERS WHERE userName = ?";

    return jdbcTemplate.queryForObject(sql, new Object[]{username},
        (rs, rowNum) -> new User(
            rs.getInt("user_id"),
            rs.getString("userName"),
            rs.getString("userPassword"),
            rs.getString("userEmail"),
            userType.valueOf(rs.getString("userType")),  // String → Enum
            rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
            rs.getInt("workTime"),
            (Integer) rs.getObject("team_id")
        ));
}
```

**Key Points:**
- `@RequestParam` extracts form fields (username, password)
- Service validates password (currently plaintext comparison)
- `session.setAttribute("user", user)` saves user for future requests
- All other endpoints check `session.getAttribute("user")` to verify login

---

#### Step 3: Access Control in Other Endpoints

**Every protected endpoint checks session:**

```java
@GetMapping("/myprojects")
public String myProjects(HttpSession session, Model model) {
    // STEP 1: Check if user is logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        return "redirect:/user/login";  // Not logged in → force login
    }

    // STEP 2: Check if user has correct role
    if (user.getUserType() != userType.PROJECTMANAGER) {
        return "redirect:/user/profile";  // Wrong role → redirect
    }

    // STEP 3: User is authorized, proceed with logic
    List<Project> projects = projectService.getProjectsByUserId(user.getUserId());
    model.addAttribute("projects", projects);
    return "projects";
}
```

**This pattern appears in EVERY controller method** that requires authentication.

---

#### Step 4: Logout

**URL:** `GET /user/logout`

**Controller Method (UserController.java:112-116):**
```java
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();  // Clear ALL session data
    return "redirect:/user/login";
}
```

**What `session.invalidate()` does:**
- Deletes all session attributes (including "user")
- User must log in again to access protected pages

---

### User Management

**Who can use this:** ADMIN users only

**User Story:** An admin wants to create, view, edit, and delete users.

---

#### View All Users

**URL:** `GET /user/profile` (as ADMIN)

**Controller Method (UserController.java:28-45):**
```java
@GetMapping("/profile")
public String profile(HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }

    // Route based on user type
    if (user.getUserType().equals(userType.valueOf("ADMIN"))) {
        // ADMIN: Show all users
        List<User> userList = userService.getUsers();
        model.addAttribute("userList", userList);
        return "adminProfile";  // adminProfile.html template

    } else if (user.getUserType().equals(userType.valueOf("PROJECTMANAGER"))) {
        // PM: Show PM dashboard
        return "pmProfile";
    }

    return "redirect:/user/login";
}
```

**Repository Method (UserRepository.java):**
```java
public List<User> getUsers() {
    String sql = "SELECT * FROM USERS";

    // jdbcTemplate.query() returns a List
    return jdbcTemplate.query(sql, (rs, rowNum) ->
        new User(
            rs.getInt("user_id"),
            rs.getString("userName"),
            rs.getString("userPassword"),
            rs.getString("userEmail"),
            userType.valueOf(rs.getString("userType")),
            rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
            rs.getInt("workTime"),
            (Integer) rs.getObject("team_id")  // Can be NULL
        ));
}
```

**Understanding the RowMapper:**
```java
(rs, rowNum) -> new User(...)
```

This is a **lambda expression** (anonymous function) that:
1. Takes a `ResultSet` (database row) and row number
2. Extracts each column value
3. Creates a `User` object
4. Returns it

Spring calls this lambda for **each row** in the result set.

---

#### Create New User

**Step 1: Show Form**

**URL:** `GET /user/addNewUser`

**Controller Method (UserController.java:47-55):**
```java
@GetMapping("/addNewUser")
public String addNewUser(HttpSession session, Model model) {
    User user = new User();  // Empty user object for form binding

    // Make enum values available for dropdown menus
    model.addAttribute("userTypeEnums", userType.values());  // [ADMIN, PROJECTMANAGER, DEV]
    model.addAttribute("devTypeEnums", devType.values());    // [FRONTEND, BACKEND, FULLSTACK]
    model.addAttribute("newUser", user);

    return "addNewUserForm";
}
```

**Template (addNewUserForm.html):**
```html
<form th:action="@{/user/addNewUser}" th:object="${newUser}" method="post">
    <input type="text" th:field="*{username}" required>
    <input type="email" th:field="*{email}">
    <input type="password" th:field="*{password}" required>

    <!-- Dropdown for userType -->
    <select th:field="*{userType}">
        <option th:each="type : ${userTypeEnums}"
                th:value="${type}"
                th:text="${type}">
        </option>
    </select>

    <button type="submit">Create User</button>
</form>
```

**How `th:field` works:**
- `th:field="*{username}"` creates: `<input name="username" ...>`
- When form submits, Spring calls: `newUser.setUsername(value)`
- Automatically binds form data to Java object

---

**Step 2: Process Form Submission**

**URL:** `POST /user/addNewUser`

**Controller Method (UserController.java:57-61):**
```java
@PostMapping("/addNewUser")
public String createUser(@ModelAttribute User user) {
    // @ModelAttribute: Spring automatically filled user object from form
    userService.createUser(user);
    return "redirect:/user/profile";  // Redirect to user list
}
```

**Service Layer (UserService.java):**
```java
public int createUser(User user) {
    return userRepository.createUser(user);  // Pass through to repository
}
```

**Repository Layer (UserRepository.java):**
```java
public int createUser(User user) {
    // INSERT query with ? placeholders
    String sqlInsert = "INSERT INTO USERS (userName, userEmail, userPassword, userType, devType, workTime, team_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

    // Execute INSERT
    jdbcTemplate.update(sqlInsert,
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        user.getUserType().name(),  // Enum → String ("ADMIN", "DEV", etc.)
        user.getDevType() != null ? user.getDevType().name() : null,
        user.getWorkTime(),
        user.getTeamId()
    );

    // Get the auto-generated user_id
    String sqlGetId = "SELECT LAST_INSERT_ID()";
    return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
}
```

**Why `LAST_INSERT_ID()`?**
- MySQL auto-generates `user_id` (AUTO_INCREMENT)
- After INSERT, we need that ID for future operations
- `LAST_INSERT_ID()` returns the most recently generated ID in this connection

**Example:**
```
INSERT INTO USERS (...) VALUES (...)
→ MySQL creates row with user_id = 42

SELECT LAST_INSERT_ID()
→ Returns 42
```

---

#### Edit User

**Step 1: Show Edit Form**

**URL:** `GET /user/editUser/{userId}`

**Controller Method (UserController.java:63-70):**
```java
@GetMapping("/editUser/{userId}")
public String editUser(@PathVariable int userId, Model model) {
    // @PathVariable extracts userId from URL
    // URL: /user/editUser/42 → userId = 42

    User user = userService.findUser(userId);  // Load existing user
    model.addAttribute("user", user);
    model.addAttribute("userTypeEnums", userType.values());
    model.addAttribute("devTypeEnums", devType.values());
    return "editUserForm";
}
```

**Template (editUserForm.html):**
```html
<form th:action="@{/user/editUser}" th:object="${user}" method="post">
    <!-- Hidden field to preserve user ID -->
    <input type="hidden" th:field="*{userId}">

    <input type="text" th:field="*{username}" required>
    <input type="email" th:field="*{email}">

    <!-- Password field (optional on edit) -->
    <input type="password" th:field="*{password}">

    <button type="submit">Update User</button>
</form>
```

**Why hidden userId field?**
The form doesn't show userId to the user, but Spring needs it to know which user to update.

---

**Step 2: Process Edit Form**

**URL:** `POST /user/editUser`

**Controller Method (UserController.java:72-76):**
```java
@PostMapping("/editUser")
public String updateUser(@ModelAttribute User user) {
    // user object now has:
    // - userId from hidden field
    // - All other fields from form inputs

    userService.editUser(user);
    return "redirect:/user/profile";
}
```

**Repository Method (UserRepository.java):**
```java
public void editUser(User user) {
    String sql = "UPDATE USERS SET userName = ?, userEmail = ?, userPassword = ?, " +
                 "userType = ?, devType = ?, workTime = ?, team_id = ? " +
                 "WHERE user_id = ?";

    jdbcTemplate.update(sql,
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        user.getUserType().name(),
        user.getDevType() != null ? user.getDevType().name() : null,
        user.getWorkTime(),
        user.getTeamId(),
        user.getUserId()  // WHERE clause value (which user to update)
    );
}
```

---

#### Delete User

**URL:** `GET /user/deleteUser/{userId}`

**Controller Method (UserController.java:78-82):**
```java
@GetMapping("/deleteUser/{userId}")
public String deleteUser(@PathVariable int userId) {
    userService.deleteUser(userId);
    return "redirect:/user/profile";
}
```

**Repository Method (UserRepository.java):**
```java
public int deleteUser(int userId) {
    String sql = "DELETE FROM USERS WHERE user_id = ?";
    return jdbcTemplate.update(sql, userId);  // Returns number of rows affected
}
```

**What happens in database:**
- User row deleted
- If user was in `USERS_PROJECT` → those rows deleted (CASCADE)
- If user was in `USERS_SUBTASK` → those rows deleted (CASCADE)
- If user had `team_id` → just the FK is removed, team remains

---

### Project Management

**Who can use this:** PROJECTMANAGER users only

**User Story:** A project manager wants to manage their projects.

---

#### View My Projects

**URL:** `GET /project/myprojects`

**Controller Method (ProjectController.java):**
```java
@GetMapping("/myprojects")
public String myProjects(HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");

    // Security checks
    if (user == null) {
        return "redirect:/user/login";
    }
    if (user.getUserType() != userType.PROJECTMANAGER) {
        return "redirect:/user/profile";
    }

    // Get only THIS PM's projects
    List<Project> projects = projectService.getProjectsByUserId(user.getUserId());
    model.addAttribute("projects", projects);
    return "projects";  // projects.html
}
```

**Repository Method (ProjectRepository.java:89-105):**
```java
public List<Project> getProjectsByUserId(int userId) {
    // JOIN with junction table to get user's projects
    String sql = "SELECT * FROM PROJECT p " +
                 "JOIN USERS_PROJECT up ON p.project_id = up.project_id " +
                 "WHERE up.user_id = ?";

    return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
        new Project(
            rs.getInt("project_id"),
            rs.getString("projectName"),
            rs.getString("projectDescription"),
            rs.getString("status"),
            rs.getString("priority"),
            rs.getInt("estimatedTime"),
            rs.getInt("actualTime"),
            rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
            rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
        ));
}
```

**Understanding the JOIN:**
```sql
SELECT * FROM PROJECT p
JOIN USERS_PROJECT up ON p.project_id = up.project_id
WHERE up.user_id = 5
```

This query:
1. Joins PROJECT table with USERS_PROJECT junction table
2. Filters for rows where `up.user_id = 5` (the PM's ID)
3. Returns all projects assigned to that PM

**Example data:**
```
USERS_PROJECT table:
| user_id | project_id |
|---------|------------|
| 5       | 10         |  ← PM #5 manages Project #10
| 5       | 12         |  ← PM #5 manages Project #12
| 7       | 11         |  ← PM #7 manages Project #11

Query for user_id=5 returns: Projects #10 and #12
```

---

#### Create New Project

**Step 1: Show Form**

**URL:** `GET /project/add`

**Controller Method (ProjectController.java):**
```java
@GetMapping("/add")
public String addProject(HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }
    if (user.getUserType() != userType.PROJECTMANAGER) {
        return "redirect:/user/profile";
    }

    // Get available teams to assign
    List<Team> availableTeams = teamService.getTeams();

    model.addAttribute("newProject", new Project());
    model.addAttribute("availableTeams", availableTeams);
    return "addProjectForm";
}
```

**Template Example (addProjectForm.html:30-44):**
```html
<form th:action="@{/project/add}" th:object="${newProject}" method="post">
    <!-- Project Name -->
    <label>Project Name</label>
    <input type="text" th:field="*{name}" required>

    <!-- Description -->
    <label>Description</label>
    <textarea th:field="*{description}" rows="4"></textarea>

    <!-- Team Selection (checkboxes) -->
    <label>Select Teams:</label>
    <div th:each="team : ${availableTeams}">
        <input type="checkbox"
               name="selectedTeamIds"
               th:value="${team.teamId}">
        <span th:text="${team.teamName}">Team Name</span>
    </div>

    <!-- Other fields... -->
    <input type="date" th:field="*{startDate}">
    <input type="date" th:field="*{endDate}">

    <button type="submit">Save</button>
</form>
```

**Key points:**
- `th:field="*{name}"` binds to `newProject.getName()/setName()`
- `name="selectedTeamIds"` sends array of selected team IDs
- Multiple checkboxes with same `name` create an array

---

**Step 2: Process Form**

**URL:** `POST /project/add`

**Controller Method (ProjectController.java):**
```java
@PostMapping("/add")
public String createProject(@ModelAttribute Project project,
                           @RequestParam(required = false) List<Integer> selectedTeamIds,
                           HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }

    // Step 1: Create the project
    int projectId = projectService.createProject(project);

    // Step 2: Assign project to this PM
    projectService.assignProjectToUser(projectId, user.getUserId());

    // Step 3: Assign selected teams to project
    if (selectedTeamIds != null && !selectedTeamIds.isEmpty()) {
        for (Integer teamId : selectedTeamIds) {
            Team team = teamService.findTeam(teamId);
            team.setProjectId(projectId);  // Set FK
            teamService.editTeam(team);     // UPDATE team
        }
    }

    return "redirect:/project/myprojects";
}
```

**Repository Method (ProjectRepository.java:18-34):**
```java
public int createProject(Project project) {
    String sqlInsert = "INSERT INTO PROJECT (projectName, projectDescription, status, priority, " +
                       "estimatedTime, actualTime, startDate, endDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    // Execute INSERT
    jdbcTemplate.update(sqlInsert,
        project.getName(),
        project.getDescription(),
        project.getStatus(),
        project.getPriority(),
        project.getEstimatedTime(),
        project.getActualTime(),
        project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null,  // LocalDate → java.sql.Date
        project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null
    );

    // Get auto-generated project_id
    String sqlGetId = "SELECT LAST_INSERT_ID()";
    return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
}
```

**Date Handling:**
```java
project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null
```

- `project.getStartDate()` returns `java.time.LocalDate` (Java 8+)
- `Date.valueOf()` converts to `java.sql.Date` (for JDBC)
- If `null`, we insert `NULL` into database

**Assigning Project to PM (ProjectRepository.java:107-110):**
```java
public void assignProjectToUser(int projectId, int userId) {
    String sql = "INSERT INTO USERS_PROJECT (user_id, project_id) VALUES (?, ?)";
    jdbcTemplate.update(sql, userId, projectId);
}
```

This creates a row in the junction table linking the PM to the project.

---

#### Edit Project

**URL:** `POST /project/edit`

**Repository Method (ProjectRepository.java:68-82):**
```java
public void editProject(Project project) {
    String sqlEdit = "UPDATE PROJECT SET projectName = ?, projectDescription = ?, status = ?, " +
                     "priority = ?, estimatedTime = ?, actualTime = ?, startDate = ?, endDate = ? " +
                     "WHERE project_id = ?";

    jdbcTemplate.update(sqlEdit,
        project.getName(),
        project.getDescription(),
        project.getStatus(),
        project.getPriority(),
        project.getEstimatedTime(),
        project.getActualTime(),
        project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null,
        project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null,
        project.getProjectId()  // WHERE clause
    );
}
```

---

#### Delete Project

**URL:** `GET /project/delete/{projectId}`

**Repository Method (ProjectRepository.java:84-87):**
```java
public int deleteProject(int projectId) {
    String sql = "DELETE FROM PROJECT WHERE project_id = ?";
    return jdbcTemplate.update(sql, projectId);
}
```

**What gets deleted (CASCADE):**
```
DELETE PROJECT (project_id = 5)
  ↓
CASCADE DELETE SUBPROJECT (project_id = 5)
  ↓
CASCADE DELETE TASK (for those subprojects)
  ↓
CASCADE DELETE SUBTASK (for those tasks)
  ↓
CASCADE DELETE USERS_PROJECT (project_id = 5)
```

All related data is automatically removed.

---

### SubProject Management

SubProjects have a **one-to-many** relationship with Projects:
- One Project can have many SubProjects
- Each SubProject belongs to exactly one Project (via `project_id` foreign key)

---

#### View SubProjects for a Project

**URL:** `GET /subproject/list/{projectId}`

**Controller Method (SubProjectController.java):**
```java
@GetMapping("/list/{projectId}")
public String listSubProjects(@PathVariable int projectId, HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }

    // Get all subprojects for this project
    List<SubProject> subProjects = subProjectService.getSubProjectsByProjectId(projectId);

    model.addAttribute("subProjects", subProjects);
    model.addAttribute("projectId", projectId);  // For "back" navigation
    return "subprojects";
}
```

**Repository Method (SubProjectRepository.java):**
```java
public List<SubProject> getSubProjectsByProjectId(int projectId) {
    // Simple WHERE query (no JOIN needed - direct FK)
    String sql = "SELECT * FROM SUBPROJECT WHERE project_id = ?";

    return jdbcTemplate.query(sql, new Object[]{projectId}, (rs, rowNum) ->
        new SubProject(
            rs.getInt("sub_project_id"),
            rs.getString("subProjectName"),
            rs.getString("subProjectDescription"),
            rs.getString("status"),
            rs.getInt("estimatedTime"),
            rs.getInt("actualTime"),
            rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
            rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
            rs.getInt("project_id")  // FK to parent project
        ));
}
```

---

#### Create SubProject

**URL:** `POST /subproject/add`

**Controller Method (SubProjectController.java):**
```java
@PostMapping("/add")
public String createSubProject(@ModelAttribute SubProject subProject,
                              @RequestParam(required = false) List<Integer> selectedTeamIds,
                              @RequestParam int projectId,
                              HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }

    // Set the foreign key
    subProject.setProjectId(projectId);

    // Create subproject
    int subProjectId = subProjectService.createSubproject(subProject);

    // Assign teams if selected
    if (selectedTeamIds != null && !selectedTeamIds.isEmpty()) {
        for (Integer teamId : selectedTeamIds) {
            Team team = teamService.findTeam(teamId);
            team.setSubProjectId(subProjectId);
            teamService.editTeam(team);
        }
    }

    return "redirect:/subproject/list/" + projectId;  // Back to subproject list
}
```

**Repository Method (SubProjectRepository.java):**
```java
public int createSubproject(SubProject subProject) {
    String sqlInsert = "INSERT INTO SUBPROJECT (subProjectName, subProjectDescription, status, " +
                       "estimatedTime, actualTime, startDate, endDate, project_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(sqlInsert,
        subProject.getName(),
        subProject.getDescription(),
        subProject.getStatus(),
        subProject.getEstimatedTime(),
        subProject.getActualTime(),
        subProject.getStartDate() != null ? Date.valueOf(subProject.getStartDate()) : null,
        subProject.getEndDate() != null ? Date.valueOf(subProject.getEndDate()) : null,
        subProject.getProjectId()  // FK value
    );

    String sqlGetId = "SELECT LAST_INSERT_ID()";
    return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
}
```

**Key Point:** The `project_id` foreign key is set directly in the SubProject object, then included in the INSERT. No junction table needed because it's a one-to-many relationship.

---

### Task Management

Tasks follow the same pattern as SubProjects:
- One SubProject can have many Tasks
- Each Task belongs to exactly one SubProject (via `sub_project_id` foreign key)

**URL:** `GET /task/list/{subProjectId}`

**Repository Method (TaskRepository.java):**
```java
public List<Task> getTasksBySubProjectId(int subProjectId) {
    String sql = "SELECT * FROM TASK WHERE sub_project_id = ?";

    return jdbcTemplate.query(sql, new Object[]{subProjectId}, (rs, rowNum) ->
        new Task(
            rs.getInt("task_id"),
            rs.getString("taskName"),
            rs.getString("taskDescription"),
            rs.getString("status"),
            rs.getInt("estimatedTime"),
            rs.getInt("actualTime"),
            rs.getString("priority"),
            rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
            rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
            rs.getInt("sub_project_id")  // FK
        ));
}
```

**Create Task:**
```java
task.setSubProjectId(subProjectId);  // Set FK
taskService.createTask(task);        // INSERT with sub_project_id
```

---

### Subtask Management

Subtasks are unique because they use a **many-to-many** relationship with developers.

- One Subtask can have multiple developers assigned
- One developer can be assigned to multiple Subtasks
- Junction table: `USERS_SUBTASK`

---

#### View Subtasks with Assigned Developers

**URL:** `GET /subtask/list/{taskId}`

**Controller Method (SubtaskController.java):**
```java
@GetMapping("/list/{taskId}")
public String listSubtasks(@PathVariable int taskId, HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }

    // Get all subtasks for this task
    List<Subtask> subtasks = subtaskService.getSubtasksByTaskId(taskId);

    // For each subtask, get assigned developers
    Map<Integer, List<User>> assignedDevsMap = new HashMap<>();
    for (Subtask subtask : subtasks) {
        List<User> devs = subtaskService.getDevsBySubtaskId(subtask.getSubTaskId());
        assignedDevsMap.put(subtask.getSubTaskId(), devs);
    }

    model.addAttribute("subtasks", subtasks);
    model.addAttribute("assignedDevsMap", assignedDevsMap);
    model.addAttribute("taskId", taskId);
    return "subtasks";
}
```

**Repository Method - Get Developers for Subtask:**
```java
public List<User> getDevsBySubtaskId(int subtaskId) {
    // JOIN with junction table to get assigned developers
    String sql = "SELECT u.* FROM USERS u " +
                 "JOIN USERS_SUBTASK us ON u.user_id = us.user_id " +
                 "WHERE us.sub_task_id = ?";

    return jdbcTemplate.query(sql, new Object[]{subtaskId}, (rs, rowNum) ->
        new User(
            rs.getInt("user_id"),
            rs.getString("userName"),
            rs.getString("userPassword"),
            rs.getString("userEmail"),
            userType.valueOf(rs.getString("userType")),
            rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
            rs.getInt("workTime"),
            (Integer) rs.getObject("team_id")
        ));
}
```

**Understanding the JOIN:**
```sql
SELECT u.* FROM USERS u
JOIN USERS_SUBTASK us ON u.user_id = us.user_id
WHERE us.sub_task_id = 78
```

This gets all users who have a row in `USERS_SUBTASK` with `sub_task_id = 78`.

---

#### Create Subtask with Developer Assignment

**URL:** `POST /subtask/add`

**Controller Method (SubtaskController.java):**
```java
@PostMapping("/add")
public String createSubtask(@ModelAttribute Subtask subtask,
                           @RequestParam(required = false) List<Integer> selectedDevIds,
                           @RequestParam int taskId,
                           HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }

    // Set FK to task
    subtask.setTaskId(taskId);

    // Create subtask
    int subtaskId = subtaskService.createSubtask(subtask);

    // Assign developers via junction table
    if (selectedDevIds != null && !selectedDevIds.isEmpty()) {
        for (Integer devId : selectedDevIds) {
            subtaskService.assignDevToSubtask(devId, subtaskId);
        }
    }

    return "redirect:/subtask/list/" + taskId;
}
```

**Repository Method - Assign Developer:**
```java
public void assignDevToSubtask(int userId, int subtaskId) {
    String sql = "INSERT INTO USERS_SUBTASK (user_id, sub_task_id) VALUES (?, ?)";
    jdbcTemplate.update(sql, userId, subtaskId);
}
```

This creates a row in the junction table linking the developer to the subtask.

---

#### Edit Subtask - Reassign Developers

**URL:** `POST /subtask/edit`

**Controller Method (SubtaskController.java):**
```java
@PostMapping("/edit")
public String updateSubtask(@ModelAttribute Subtask subtask,
                           @RequestParam(required = false) List<Integer> selectedDevIds,
                           HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }

    // Update subtask details
    subtaskService.editSubtask(subtask);

    // Reassign developers:
    // Step 1: Remove all current assignments
    subtaskService.removeAllDevsFromSubtask(subtask.getSubTaskId());

    // Step 2: Add new assignments
    if (selectedDevIds != null && !selectedDevIds.isEmpty()) {
        for (Integer devId : selectedDevIds) {
            subtaskService.assignDevToSubtask(devId, subtask.getSubTaskId());
        }
    }

    return "redirect:/subtask/list/" + subtask.getTaskId();
}
```

**Repository Method - Remove All Developers:**
```java
public void removeAllDevsFromSubtask(int subtaskId) {
    String sql = "DELETE FROM USERS_SUBTASK WHERE sub_task_id = ?";
    jdbcTemplate.update(sql, subtaskId);
}
```

**Why delete all and re-add?**
Simpler than comparing old vs new assignments. We clear the slate and insert fresh.

---

### Team Management

Teams are assigned to Projects/SubProjects/Tasks and contain developers as members.

---

#### View All Teams with Members

**URL:** `GET /team/list`

**Controller Method (TeamController.java):**
```java
@GetMapping("/list")
public String listTeams(HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }
    if (user.getUserType() != userType.PROJECTMANAGER) {
        return "redirect:/user/profile";
    }

    // Get all teams
    List<Team> teams = teamService.getTeams();

    // Get members for each team
    Map<Integer, List<User>> teamMembersMap = new HashMap<>();
    for (Team team : teams) {
        List<User> members = teamService.getTeamDevs(team.getTeamId());
        teamMembersMap.put(team.getTeamId(), members);
    }

    model.addAttribute("teams", teams);
    model.addAttribute("teamMembersMap", teamMembersMap);
    return "teams";
}
```

**Repository Method - Get Team Members:**
```java
public List<User> getTeamDevs(int teamId) {
    String sql = "SELECT * FROM USERS WHERE team_id = ?";

    return jdbcTemplate.query(sql, new Object[]{teamId}, (rs, rowNum) ->
        new User(
            rs.getInt("user_id"),
            rs.getString("userName"),
            rs.getString("userPassword"),
            rs.getString("userEmail"),
            userType.valueOf(rs.getString("userType")),
            rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
            rs.getInt("workTime"),
            (Integer) rs.getObject("team_id")
        ));
}
```

**Key Point:** Team membership is stored in `USERS.team_id` (FK), not a junction table.

---

#### Create Team and Assign Developers

**URL:** `POST /team/add`

**Controller Method (TeamController.java):**
```java
@PostMapping("/add")
public String createTeam(@ModelAttribute Team team,
                        @RequestParam(required = false) List<Integer> selectedDevIds,
                        HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";
    }

    // Create team
    int teamId = teamService.createTeam(team);

    // Assign selected developers to team
    if (selectedDevIds != null && !selectedDevIds.isEmpty()) {
        for (Integer devId : selectedDevIds) {
            User dev = userService.findUser(devId);
            dev.setTeamId(teamId);  // Set FK
            userService.editUser(dev);  // UPDATE USERS SET team_id = ?
        }
    }

    return "redirect:/team/list";
}
```

**Repository Method - Create Team:**
```java
public int createTeam(Team team) {
    String sqlInsert = "INSERT INTO TEAM (teamName, teamDescription, project_id, sub_project_id, task_id) " +
                       "VALUES (?, ?, ?, ?, ?)";

    jdbcTemplate.update(sqlInsert,
        team.getTeamName(),
        team.getTeamDescription(),
        team.getProjectId(),     // Can be NULL
        team.getSubProjectId(),  // Can be NULL
        team.getTaskId()         // Can be NULL
    );

    String sqlGetId = "SELECT LAST_INSERT_ID()";
    return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
}
```

**Why multiple FKs in TEAM table?**
A team can be assigned to work on a specific Project, SubProject, or Task. All three FKs are nullable because a team might not be assigned yet.

---

## Key Architecture Patterns

Now let's look at the common code patterns used throughout the project.

---

### Pattern 1: Standard CRUD in Repository

**Full Example - CREATE with auto-generated ID:**

```java
public int createProject(Project project) {
    // Step 1: Define INSERT query with ? placeholders
    String sqlInsert = "INSERT INTO PROJECT (projectName, projectDescription, status, priority, " +
                       "estimatedTime, actualTime, startDate, endDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    // Step 2: Execute INSERT with actual values
    jdbcTemplate.update(sqlInsert,
        project.getName(),                 // ? → projectName
        project.getDescription(),          // ? → projectDescription
        project.getStatus(),               // ? → status
        project.getPriority(),             // ? → priority
        project.getEstimatedTime(),        // ? → estimatedTime
        project.getActualTime(),           // ? → actualTime
        // Date conversion: LocalDate → java.sql.Date
        project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null,
        project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null
    );

    // Step 3: Get auto-generated primary key
    String sqlGetId = "SELECT LAST_INSERT_ID()";
    return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
}
```

**Why `?` placeholders?**
Security! They prevent SQL injection:
```java
// BAD (vulnerable to SQL injection):
String sql = "INSERT INTO USERS (userName) VALUES ('" + username + "')";

// GOOD (safe):
String sql = "INSERT INTO USERS (userName) VALUES (?)";
jdbcTemplate.update(sql, username);
```

If `username = "admin'; DROP TABLE USERS; --"`, the placeholder treats it as a literal string, not SQL code.

---

### Pattern 2: RowMapper for Object Mapping

**Full Example with Explanation:**

```java
public List<Project> getProjects() {
    String sql = "SELECT * FROM PROJECT";

    // jdbcTemplate.query() executes SELECT and returns List
    return jdbcTemplate.query(sql, (rs, rowNum) -> {
        // Lambda function called for EACH row in result set
        // rs = ResultSet (database row)
        // rowNum = row number (0, 1, 2, ...)

        return new Project(
            // Extract columns by name
            rs.getInt("project_id"),      // Get project_id column as int
            rs.getString("projectName"),   // Get projectName as String
            rs.getString("projectDescription"),
            rs.getString("status"),
            rs.getString("priority"),
            rs.getInt("estimatedTime"),
            rs.getInt("actualTime"),

            // Date handling with NULL check
            rs.getDate("startDate") != null
                ? rs.getDate("startDate").toLocalDate()  // java.sql.Date → LocalDate
                : null,                                   // If NULL in DB → null in Java

            rs.getDate("endDate") != null
                ? rs.getDate("endDate").toLocalDate()
                : null
        );
    });
}
```

**Short form (same thing):**
```java
return jdbcTemplate.query(sql, (rs, rowNum) ->
    new Project(
        rs.getInt("project_id"),
        rs.getString("projectName"),
        // ... etc
    ));
```

**For single row (queryForObject):**
```java
public Project findProject(int projectId) {
    String sql = "SELECT * FROM PROJECT WHERE project_id = ?";

    // queryForObject returns ONE object (not a List)
    return jdbcTemplate.queryForObject(sql, new Object[]{projectId}, (rs, rowNum) ->
        new Project(
            rs.getInt("project_id"),
            rs.getString("projectName"),
            // ... etc
        ));
}
```

---

### Pattern 3: Session-Based Access Control

**Every protected endpoint uses this pattern:**

```java
@GetMapping("/myprojects")
public String myProjects(HttpSession session, Model model) {
    // ===== STEP 1: Check if user is logged in =====
    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/user/login";  // Not logged in → force login
    }

    // ===== STEP 2: Check user role (if needed) =====
    if (user.getUserType() != userType.PROJECTMANAGER) {
        return "redirect:/user/profile";  // Wrong role → redirect to profile
    }

    // ===== STEP 3: User is authorized, proceed with business logic =====
    List<Project> projects = projectService.getProjectsByUserId(user.getUserId());
    model.addAttribute("projects", projects);
    return "projects";
}
```

**How session works:**

1. **On login:**
   ```java
   session.setAttribute("user", userObject);
   ```
   Spring creates a cookie in the browser with a session ID. Server stores the user object linked to that ID.

2. **On subsequent requests:**
   ```java
   User user = (User) session.getAttribute("user");
   ```
   Browser sends cookie → Spring finds session → Returns stored user object.

3. **On logout:**
   ```java
   session.invalidate();
   ```
   Deletes all session data. Browser still has cookie, but it's now invalid.

---

### Pattern 4: Form Binding with Thymeleaf

**Controller Side:**

```java
// Show form
@GetMapping("/add")
public String addProject(Model model) {
    // Create empty object for form binding
    model.addAttribute("newProject", new Project());
    return "addProjectForm";
}

// Process form
@PostMapping("/add")
public String createProject(@ModelAttribute Project project) {
    // Spring automatically filled project object from form
    // If form has <input name="projectName">, Spring called project.setProjectName(value)

    projectService.createProject(project);
    return "redirect:/project/myprojects";
}
```

**Template Side (addProjectForm.html):**

```html
<form th:action="@{/project/add}" th:object="${newProject}" method="post">
    <!-- th:field creates both "name" and "value" attributes -->
    <label>Project Name</label>
    <input type="text" th:field="*{name}" required>
    <!-- Becomes: <input type="text" name="name" value="" required> -->

    <label>Description</label>
    <textarea th:field="*{description}" rows="4"></textarea>
    <!-- Becomes: <textarea name="description" rows="4"></textarea> -->

    <label>Start Date</label>
    <input type="date" th:field="*{startDate}">
    <!-- Becomes: <input type="date" name="startDate"> -->

    <button type="submit">Save</button>
</form>
```

**How `th:field="*{name}"` works:**

1. `*{name}` means: "Get `name` from the object bound to `th:object` (which is `newProject`)"
2. Thymeleaf calls `newProject.getName()` to get current value
3. Creates `<input name="name" value="[current value]">`
4. When form submits, Spring calls `newProject.setName(submittedValue)`

**For edit forms:**
```html
<form th:action="@{/project/edit}" th:object="${project}" method="post">
    <!-- Hidden field to preserve ID -->
    <input type="hidden" th:field="*{projectId}">
    <!-- Becomes: <input type="hidden" name="projectId" value="42"> -->

    <input type="text" th:field="*{name}">
    <!-- If project.name = "Alpha Project", becomes:
         <input type="text" name="name" value="Alpha Project"> -->
</form>
```

---

### Pattern 5: Junction Table Management (Many-to-Many)

**Example: Developers assigned to Subtasks**

**Create relationship:**
```java
public void assignDevToSubtask(int userId, int subtaskId) {
    String sql = "INSERT INTO USERS_SUBTASK (user_id, sub_task_id) VALUES (?, ?)";
    jdbcTemplate.update(sql, userId, subtaskId);
}
```

**Delete relationship:**
```java
public void removeDevFromSubtask(int userId, int subtaskId) {
    String sql = "DELETE FROM USERS_SUBTASK WHERE user_id = ? AND sub_task_id = ?";
    jdbcTemplate.update(sql, userId, subtaskId);
}
```

**Delete all relationships for a subtask:**
```java
public void removeAllDevsFromSubtask(int subtaskId) {
    String sql = "DELETE FROM USERS_SUBTASK WHERE sub_task_id = ?";
    jdbcTemplate.update(sql, subtaskId);
}
```

**Get all developers for a subtask:**
```java
public List<User> getDevsBySubtaskId(int subtaskId) {
    String sql = "SELECT u.* FROM USERS u " +
                 "JOIN USERS_SUBTASK us ON u.user_id = us.user_id " +
                 "WHERE us.sub_task_id = ?";

    return jdbcTemplate.query(sql, new Object[]{subtaskId}, (rs, rowNum) ->
        new User(/* map columns to User object */)
    );
}
```

**Get all subtasks for a developer:**
```java
public List<Subtask> getSubtasksByUserId(int userId) {
    String sql = "SELECT st.* FROM SUBTASK st " +
                 "JOIN USERS_SUBTASK us ON st.sub_task_id = us.sub_task_id " +
                 "WHERE us.user_id = ?";

    return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
        new Subtask(/* map columns to Subtask object */)
    );
}
```

---

### Pattern 6: Foreign Key Navigation (One-to-Many)

**Example: Tasks belong to SubProjects**

**Create with FK:**
```java
public int createTask(Task task) {
    // task.getSubProjectId() already set by controller
    String sql = "INSERT INTO TASK (taskName, taskDescription, ..., sub_project_id) " +
                 "VALUES (?, ?, ..., ?)";

    jdbcTemplate.update(sql,
        task.getName(),
        task.getDescription(),
        // ... other fields
        task.getSubProjectId()  // FK value
    );

    return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
}
```

**Get all children by parent FK:**
```java
public List<Task> getTasksBySubProjectId(int subProjectId) {
    // Simple WHERE query - no JOIN needed
    String sql = "SELECT * FROM TASK WHERE sub_project_id = ?";

    return jdbcTemplate.query(sql, new Object[]{subProjectId}, (rs, rowNum) ->
        new Task(
            rs.getInt("task_id"),
            rs.getString("taskName"),
            // ... other fields
            rs.getInt("sub_project_id")  // FK value
        ));
}
```

**Get parent ID from child:**
```java
public int getSubProjectIdByTaskId(int taskId) {
    String sql = "SELECT sub_project_id FROM TASK WHERE task_id = ?";
    return jdbcTemplate.queryForObject(sql, new Object[]{taskId}, Integer.class);
}
```

**Why no junction table?**
Because each Task belongs to exactly ONE SubProject. Junction tables are only for many-to-many relationships.

---

## Common Patterns Reference

Quick reference table for common operations across all layers.

### CRUD Operations

| Operation | Controller Layer | Service Layer | Repository Layer |
|-----------|-----------------|---------------|------------------|
| **Get all entities** | Call `service.getEntities()` | Call `repository.getEntities()` | `SELECT * FROM ENTITY` + RowMapper → `List<Entity>` |
| **Get one entity** | `@PathVariable int id` → `service.findEntity(id)` | Pass through | `SELECT * WHERE id = ?` + RowMapper → `Entity` |
| **Create entity** | `@ModelAttribute Entity entity` → `service.createEntity(entity)` | Pass through | `INSERT INTO ENTITY (...) VALUES (...)` + `LAST_INSERT_ID()` → `int` |
| **Update entity** | `@ModelAttribute Entity entity` (hidden ID field) → `service.editEntity(entity)` | Pass through | `UPDATE ENTITY SET ... WHERE id = ?` |
| **Delete entity** | `@PathVariable int id` → `service.deleteEntity(id)` | Pass through | `DELETE FROM ENTITY WHERE id = ?` |

### Session Management

| Task | Code Pattern |
|------|-------------|
| **Login - Set session** | `session.setAttribute("user", userObject)` |
| **Check if logged in** | `User user = (User) session.getAttribute("user");`<br>`if (user == null) return "redirect:/user/login";` |
| **Check user role** | `if (user.getUserType() != userType.PROJECTMANAGER) return "redirect:/user/profile";` |
| **Logout - Clear session** | `session.invalidate()` |

### Database Operations

| Task | Code Pattern |
|------|-------------|
| **Execute INSERT/UPDATE/DELETE** | `jdbcTemplate.update(sql, param1, param2, ...)` |
| **Get multiple rows** | `jdbcTemplate.query(sql, (rs, rowNum) -> new Entity(...))` |
| **Get single row** | `jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Entity(...))` |
| **Get simple value (int, String)** | `jdbcTemplate.queryForObject(sql, Integer.class)` or `String.class` |
| **Get auto-generated ID after INSERT** | `jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class)` |

### Date Handling

| Conversion | Code |
|------------|------|
| **LocalDate → java.sql.Date (for INSERT/UPDATE)** | `Date.valueOf(localDate)` |
| **java.sql.Date → LocalDate (from SELECT)** | `rs.getDate("column").toLocalDate()` |
| **Handle NULL dates** | `rs.getDate("col") != null ? rs.getDate("col").toLocalDate() : null` |

### Enum Handling

| Conversion | Code |
|------------|------|
| **String → Enum (from SELECT)** | `userType.valueOf(rs.getString("userType"))` |
| **Enum → String (for INSERT/UPDATE)** | `user.getUserType().name()` |
| **Get all enum values** | `userType.values()` → `[ADMIN, PROJECTMANAGER, DEV]` |

---

## Navigation Guide

**"Where do I find...?"**

### Finding Code by Feature

| I want to... | Look in... |
|-------------|-----------|
| Add a new page/endpoint | `controller/` - Create `@GetMapping` method |
| Change button behavior | Find the button's link in `templates/`, trace to controller method |
| Add a database query | `repository/` - Add method with SQL query |
| Change business logic | `service/` - Add/modify method |
| Add a field to an entity | `model/` → Update constructor, getters, setters → Update repository RowMapper → Update SQL schema |
| Change what's displayed on a page | `templates/` - Modify Thymeleaf template |
| Add a new table | `SQL-scripts/USER-table.sql` - Add `CREATE TABLE` statement |

### Following a Request Flow

**Example: User clicks "Add Project" button**

1. **Find the button in HTML:**
   ```html
   <!-- In projects.html -->
   <a href="/project/add"><button>Add Project</button></a>
   ```

2. **Find the controller endpoint:**
   ```java
   // In ProjectController.java
   @GetMapping("/add")  // Matches /project/add
   public String addProject(...) {
       return "addProjectForm";  // Name of template
   }
   ```

3. **Find the template:**
   ```
   templates/addProjectForm.html
   ```

4. **Find the form submission endpoint:**
   ```html
   <!-- In addProjectForm.html -->
   <form th:action="@{/project/add}" method="post">
   ```

5. **Find the POST handler:**
   ```java
   // In ProjectController.java
   @PostMapping("/add")
   public String createProject(@ModelAttribute Project project, ...) {
       projectService.createProject(project);  // Service method
       ...
   }
   ```

6. **Find the service method:**
   ```java
   // In ProjectService.java
   public int createProject(Project project) {
       return projectRepository.createProject(project);  // Repository method
   }
   ```

7. **Find the repository method:**
   ```java
   // In ProjectRepository.java
   public int createProject(Project project) {
       String sql = "INSERT INTO PROJECT ...";
       jdbcTemplate.update(sql, ...);
       ...
   }
   ```

---

## Extension Examples

### Example 1: Add a New Field to Project

**Requirement:** Add a "budget" field to projects.

**Step-by-step:**

1. **Update database schema** (`SQL-scripts/USER-table.sql`):
   ```sql
   CREATE TABLE PROJECT (
       project_id INT AUTO_INCREMENT PRIMARY KEY,
       projectName VARCHAR(255) NOT NULL,
       projectDescription TEXT,
       budget DECIMAL(10, 2),  -- NEW FIELD
       -- ... other fields
   );
   ```

2. **Update Model** (`model/Project.java`):
   ```java
   public class Project {
       private int projectId;
       private String name;
       private String description;
       private double budget;  // NEW FIELD

       // Update constructor
       public Project(int projectId, String name, String description, double budget, ...) {
           this.projectId = projectId;
           this.name = name;
           this.description = description;
           this.budget = budget;  // NEW FIELD
           // ...
       }

       // Add getter and setter
       public double getBudget() {
           return budget;
       }

       public void setBudget(double budget) {
           this.budget = budget;
       }
   }
   ```

3. **Update Repository** (`repository/ProjectRepository.java`):
   ```java
   public int createProject(Project project) {
       String sqlInsert = "INSERT INTO PROJECT (projectName, projectDescription, budget, ...) " +
                          "VALUES (?, ?, ?, ...)";  // Add budget

       jdbcTemplate.update(sqlInsert,
           project.getName(),
           project.getDescription(),
           project.getBudget(),  // NEW FIELD
           // ...
       );
       // ...
   }

   public List<Project> getProjects() {
       String sql = "SELECT * FROM PROJECT";
       return jdbcTemplate.query(sql, (rs, rowNum) ->
           new Project(
               rs.getInt("project_id"),
               rs.getString("projectName"),
               rs.getString("projectDescription"),
               rs.getDouble("budget"),  // NEW FIELD
               // ...
           ));
   }

   public void editProject(Project project) {
       String sqlEdit = "UPDATE PROJECT SET projectName = ?, projectDescription = ?, budget = ?, ... " +
                        "WHERE project_id = ?";

       jdbcTemplate.update(sqlEdit,
           project.getName(),
           project.getDescription(),
           project.getBudget(),  // NEW FIELD
           // ...
       );
   }
   ```

4. **Update Templates**:

   **addProjectForm.html:**
   ```html
   <label>Budget</label>
   <input type="number" step="0.01" th:field="*{budget}">
   ```

   **editProjectForm.html:**
   ```html
   <label>Budget</label>
   <input type="number" step="0.01" th:field="*{budget}">
   ```

   **projects.html:**
   ```html
   <table>
       <thead>
           <tr>
               <th>Name</th>
               <th>Budget</th>  <!-- NEW COLUMN -->
               <!-- ... -->
           </tr>
       </thead>
       <tbody>
           <tr th:each="project : ${projects}">
               <td th:text="${project.name}">Name</td>
               <td th:text="${project.budget}">Budget</td>  <!-- NEW COLUMN -->
               <!-- ... -->
           </tr>
       </tbody>
   </table>
   ```

**Done!** No changes needed in Service layer (it just passes through).

---

### Example 2: Add a New Page/Endpoint

**Requirement:** Add a "Project Details" page showing subprojects and statistics.

1. **Create controller endpoint** (`controller/ProjectController.java`):
   ```java
   @GetMapping("/details/{projectId}")
   public String projectDetails(@PathVariable int projectId, HttpSession session, Model model) {
       User user = (User) session.getAttribute("user");

       if (user == null) {
           return "redirect:/user/login";
       }

       // Get project
       Project project = projectService.findProject(projectId);

       // Get subprojects
       List<SubProject> subProjects = subProjectService.getSubProjectsByProjectId(projectId);

       // Calculate total estimated time
       int totalEstimated = subProjects.stream()
           .mapToInt(SubProject::getEstimatedTime)
           .sum();

       model.addAttribute("project", project);
       model.addAttribute("subProjects", subProjects);
       model.addAttribute("totalEstimated", totalEstimated);

       return "projectDetails";  // Template name
   }
   ```

2. **Create template** (`templates/projectDetails.html`):
   ```html
   <!DOCTYPE html>
   <html xmlns:th="http://www.thymeleaf.org" lang="en">
   <head>
       <meta charset="UTF-8">
       <title>Project Details</title>
   </head>
   <body>
       <h1 th:text="${project.name}">Project Name</h1>
       <p th:text="${project.description}">Description</p>

       <h2>Statistics</h2>
       <p>Total Estimated Time: <span th:text="${totalEstimated}">0</span> hours</p>

       <h2>SubProjects</h2>
       <table>
           <tr th:each="subProject : ${subProjects}">
               <td th:text="${subProject.name}">SubProject Name</td>
               <td th:text="${subProject.estimatedTime}">Time</td>
           </tr>
       </table>

       <a href="/project/myprojects"><button>Back to Projects</button></a>
   </body>
   </html>
   ```

3. **Add link in projects.html**:
   ```html
   <a th:href="@{/project/details/{projectId}(projectId=${project.projectId})}">
       <button>View Details</button>
   </a>
   ```

**Done!**

---

## Troubleshooting

### Common Beginner Errors

#### Error: "Column 'project_id' not found"

**Cause:** Using wrong column name in SQL query.

**Remember:** Database uses `snake_case` for primary keys:
- `project_id` (NOT `projectId`)
- `sub_project_id` (NOT `subProjectId`)
- `user_id` (NOT `userId`)

**Fix:**
```java
// WRONG:
rs.getInt("projectId")

// CORRECT:
rs.getInt("project_id")
```

---

#### Error: "NullPointerException" when accessing `user.getUsername()`

**Cause:** User not logged in, session attribute is null.

**Fix:** Always check session first:
```java
User user = (User) session.getAttribute("user");
if (user == null) {
    return "redirect:/user/login";  // Add this check!
}

// Now safe to use user
String username = user.getUsername();
```

---

#### Error: Form data not binding to object

**Symptom:** After form submission, all fields in object are null.

**Cause:** Missing `th:field` or wrong `name` attribute.

**Fix:**
```html
<!-- WRONG: -->
<input type="text" name="somethingElse">

<!-- CORRECT: -->
<input type="text" th:field="*{name}">
<!-- Or manually: -->
<input type="text" name="name">
```

The `name` attribute must match the Java field name (for `@ModelAttribute` to work).

---

#### Error: "EmptyResultDataAccessException"

**Cause:** `queryForObject()` expects exactly one row, but found zero.

**Fix:** Use try-catch or check if exists first:
```java
// Option 1: Handle exception
try {
    User user = jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> ...);
} catch (EmptyResultDataAccessException e) {
    return null;  // User not found
}

// Option 2: Use query() which returns empty list instead of exception
List<User> users = jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> ...);
return users.isEmpty() ? null : users.get(0);
```

---

#### Error: Auto-increment ID skipping numbers

**Symptom:** User IDs go 1, 2, 3, 5, 6 (skipped 4).

**Cause:** This is normal! If an INSERT fails (validation error, duplicate key, etc.), MySQL still increments the counter.

**This is NOT a bug** - IDs don't need to be sequential, they just need to be unique.

---

#### Error: "Date cannot be null"

**Cause:** Database column is `NOT NULL`, but you're trying to insert null.

**Fix 1:** Make column nullable in database:
```sql
CREATE TABLE PROJECT (
    startDate DATE,  -- Nullable (default)
    ...
);
```

**Fix 2:** Provide a default value:
```java
project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : Date.valueOf(LocalDate.now())
```

---

#### Error: "Incorrect enum value"

**Cause:** Trying to insert enum value that doesn't exist in database ENUM definition.

**Example:**
```sql
-- Database has:
userType ENUM('ADMIN', 'PROJECTMANAGER', 'DEV')

-- Java tries to insert:
"PROJECT_MANAGER"  -- Wrong! Should be "PROJECTMANAGER"
```

**Fix:** Ensure Java enum names match database ENUM values exactly:
```java
public enum userType {
    ADMIN,           // Matches 'ADMIN' in database
    PROJECTMANAGER,  // Matches 'PROJECTMANAGER' (no underscore!)
    DEV              // Matches 'DEV'
}
```

---

## Glossary

**Model / POJO / Entity**
A simple Java class with fields, constructors, getters, and setters. Represents a database table row. POJO = "Plain Old Java Object" (no special framework requirements).

**Repository / DAO**
A class that handles database operations (CRUD) for a specific entity. DAO = "Data Access Object".

**Service Layer**
A class between Controller and Repository that contains business logic. In this project, most services are simple pass-throughs, but in larger projects they contain validation, calculations, and complex logic.

**Controller / Endpoint**
A class that handles HTTP requests. Methods are mapped to URLs and return view names or redirects.

**RowMapper**
A lambda or function that converts a database row (ResultSet) into a Java object. Used with `jdbcTemplate.query()`.

**Junction Table**
A table used to represent many-to-many relationships. Contains two foreign keys (one to each related table). Example: `USERS_SUBTASK` links users to subtasks.

**Foreign Key (FK)**
A column that references the primary key of another table. Creates a relationship between tables. Example: `TASK.sub_project_id` references `SUBPROJECT.sub_project_id`.

**Primary Key (PK)**
A unique identifier for a row in a table. Usually an auto-incrementing integer (1, 2, 3, ...).

**Cascade Delete**
When a parent row is deleted, all child rows referencing it are automatically deleted. Example: Deleting a Project deletes all its SubProjects.

**Session**
Server-side storage for user data across multiple HTTP requests. Used to remember login state.

**Cookie**
A small piece of data sent by the server and stored in the user's browser. Used to identify the session.

**Dependency Injection (DI)**
A design pattern where objects receive their dependencies from an external source (Spring) instead of creating them. Example: Spring provides `JdbcTemplate` to repositories automatically.

**Enum**
A special Java class that represents a fixed set of constants. Example: `userType` enum has three values: `ADMIN`, `PROJECTMANAGER`, `DEV`.

**JdbcTemplate**
A Spring class that simplifies database operations without needing an ORM (Object-Relational Mapping) framework.

**Thymeleaf**
A template engine that generates dynamic HTML using Java objects passed from controllers.

**`@Controller`**
Annotation that marks a class as a Spring MVC controller (handles HTTP requests).

**`@Service`**
Annotation that marks a class as a Spring service (contains business logic).

**`@Repository`**
Annotation that marks a class as a Spring repository (handles database access).

**`@GetMapping` / `@PostMapping`**
Annotations that map HTTP GET/POST requests to controller methods.

**`@PathVariable`**
Annotation that extracts a value from the URL path. Example: `/user/edit/{userId}` → `@PathVariable int userId`.

**`@RequestParam`**
Annotation that extracts a value from the query string or form data. Example: `?username=admin` → `@RequestParam String username`.

**`@ModelAttribute`**
Annotation that binds form data to a Java object automatically.

---

## Summary

You now have a complete understanding of how this Spring Boot Project Management system works:

1. **Request Flow**: Browser → Controller → Service → Repository → Database
2. **Session Management**: Login sets session, other endpoints check it
3. **CRUD Pattern**: Every feature follows the same pattern (create, read, update, delete)
4. **Relationships**: One-to-many uses foreign keys, many-to-many uses junction tables
5. **Code Location**: Controllers handle requests, repositories handle database, models are data structures

**To extend the system:**
- Follow the existing patterns
- Keep the 3-layer architecture (Controller → Service → Repository)
- Use session checks for authentication
- Use foreign keys for one-to-many, junction tables for many-to-many

**Questions to ask yourself when adding new features:**
1. What entity am I working with? (Create model first)
2. What database operations do I need? (Add repository methods)
3. Do I need business logic? (Add service methods if needed)
4. What URLs should I use? (Add controller endpoints)
5. What should the user see? (Create Thymeleaf templates)

Good luck with your project! 🚀
