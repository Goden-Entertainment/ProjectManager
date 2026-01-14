# Project Management System - Endpoint Flowcharts

> Sequence diagrams for every endpoint showing the flow through layers: Browser → Controller → Service → Repository → Database

---

## Table of Contents

1. [Authentication Endpoints](#authentication-endpoints)
2. [User Management Endpoints](#user-management-endpoints)
3. [Project Management Endpoints](#project-management-endpoints)
4. [SubProject Management Endpoints](#subproject-management-endpoints)
5. [Task Management Endpoints](#task-management-endpoints)
6. [Subtask Management Endpoints](#subtask-management-endpoints)
7. [Team Management Endpoints](#team-management-endpoints)

---

## Authentication Endpoints

### GET /user/login
**Show Login Form**

```
Browser              Controller              Template
   │                      │                      │
   │  GET /user/login     │                      │
   ├─────────────────────>│                      │
   │                      │  UserController      │
   │                      │  .login()            │
   │                      │                      │
   │                      │  Check: session      │
   │                      │  has 'user'?         │
   │                      │                      │
   │                      │  If yes: redirect    │
   │                      │  to /user/profile    │
   │                      │                      │
   │                      │  If no: return       │
   │                      │  "login"             │
   │                      ├─────────────────────>│
   │                      │                      │  Render login.html
   │<──────────────────────────────────────────────┤
   │  Display login form  │                      │
```

---

### POST /user/login
**Authenticate User**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /user/login     │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  username="admin"     │                      │                     │                    │
   │  password="admin123"  │                      │                     │                    │
   │                       │  UserController      │                     │                    │
   │                       │  .authenticateUser() │                     │                    │
   │                       │                      │                     │                    │
   │                       │  login(username,     │                     │                    │
   │                       │  password)           │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  UserService        │                    │
   │                       │                      │  .login()           │                    │
   │                       │                      │                     │                    │
   │                       │                      │  findUser(username) │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  UserRepository    │
   │                       │                      │                     │  .findUser()       │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  USERS WHERE       │
   │                       │                      │                     │  userName=?        │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (user row data)  │
   │                       │                      │                     │                    │
   │                       │                      │                     │  RowMapper:        │
   │                       │                      │                     │  ResultSet→User    │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   User object       │                    │
   │                       │                      │                     │                    │
   │                       │                      │  Check: password    │                    │
   │                       │                      │  matches?           │                    │
   │                       │                      │                     │                    │
   │                       │                      │  If no: throw       │                    │
   │                       │                      │  ProfileNotFound    │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   User object        │                     │                    │
   │                       │                      │                     │                    │
   │                       │  session.setAttribute│                     │                    │
   │                       │  ("user", user)      │                     │                    │
   │                       │                      │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /user/profile        │                      │                     │                    │
```

---

### GET /user/logout
**Clear Session**

```
Browser              Controller              Session
   │                      │                      │
   │  GET /user/logout    │                      │
   ├─────────────────────>│                      │
   │                      │  UserController      │
   │                      │  .logout()           │
   │                      │                      │
   │                      │  session.invalidate()│
   │                      ├─────────────────────>│
   │                      │                      │  Clear all data
   │                      │                      │  Delete "user"
   │<──────────────────────┤                      │
   │  redirect:           │                      │
   │  /user/login         │                      │
```

---

## User Management Endpoints

### GET /user/profile
**View Profile / User List (Role-based routing)**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /user/profile    │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  UserController      │                     │                    │
   │                       │  .profile()          │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session      │                     │                    │
   │                       │  has 'user'?         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  If null: redirect   │                     │                    │
   │                       │  to /user/login      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: userType?    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  If ADMIN:           │                     │                    │
   │                       │  getUsers()          │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  UserService        │                    │
   │                       │                      │  .getUsers()        │                    │
   │                       │                      │                     │                    │
   │                       │                      │  getUsers()         │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  UserRepository    │
   │                       │                      │                     │  .getUsers()       │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  USERS             │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (all user rows)  │
   │                       │                      │                     │                    │
   │                       │                      │                     │  RowMapper: each   │
   │                       │                      │                     │  row → User object │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<User>        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<User>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("userList", list)  │                     │                    │
   │                       │                      │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  adminProfile.html    │                      │                     │                    │
   │                       │                      │                     │                    │
   │  If PROJECTMANAGER:   │                      │                     │                    │
   │  Render pmProfile.html│                      │                     │                    │
```

---

### GET /user/addNewUser
**Show Add User Form**

```
Browser              Controller              Model
   │                      │                      │
   │  GET /user/          │                      │
   │  addNewUser          │                      │
   ├─────────────────────>│                      │
   │                      │  UserController      │
   │                      │  .addNewUser()       │
   │                      │                      │
   │                      │  Create new User()   │
   │                      ├─────────────────────>│
   │                      │                      │  Empty User object
   │                      │                      │
   │                      │  model.addAttribute  │
   │                      │  ("newUser", user)   │
   │                      │                      │
   │                      │  model.addAttribute  │
   │                      │  ("userTypeEnums",   │
   │                      │   [ADMIN, PM, DEV])  │
   │                      │                      │
   │                      │  model.addAttribute  │
   │                      │  ("devTypeEnums",    │
   │                      │   [FRONTEND, etc])   │
   │<──────────────────────┤                      │
   │  Render:             │                      │
   │  addNewUserForm.html │                      │
   │  with enum dropdowns │                      │
```

---

### POST /user/addNewUser
**Create New User**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /user/          │                      │                     │                    │
   │  addNewUser           │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  Form data:           │                      │                     │                    │
   │  username, email,     │                      │                     │                    │
   │  password, userType,  │                      │                     │                    │
   │  devType, etc.        │                      │                     │                    │
   │                       │  UserController      │                     │                    │
   │                       │  .createUser()       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds form → User   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  createUser(user)    │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  UserService        │                    │
   │                       │                      │  .createUser()      │                    │
   │                       │                      │                     │                    │
   │                       │                      │  createUser(user)   │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  UserRepository    │
   │                       │                      │                     │  .createUser()     │
   │                       │                      │                     │                    │
   │                       │                      │                     │  INSERT INTO USERS │
   │                       │                      │                     │  (userName, email, │
   │                       │                      │                     │   password, ...)   │
   │                       │                      │                     │  VALUES (?, ?, ?)  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row inserted     │
   │                       │                      │                     │   user_id = 42     │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT            │
   │                       │                      │                     │  LAST_INSERT_ID()  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Returns 42       │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   user_id = 42      │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   user_id = 42       │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /user/profile        │                      │                     │                    │
```

---

### GET /user/editUser/{userId}
**Show Edit User Form**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /user/edit/42    │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  UserController      │                     │                    │
   │                       │  .editUser()         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  userId = 42         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findUser(42)        │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  UserService        │                    │
   │                       │                      │  .findUser()        │                    │
   │                       │                      │                     │                    │
   │                       │                      │  findUser(42)       │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  UserRepository    │
   │                       │                      │                     │  .findUser()       │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  USERS WHERE       │
   │                       │                      │                     │  user_id = 42      │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (user row)       │
   │                       │                      │                     │                    │
   │                       │                      │                     │  RowMapper:        │
   │                       │                      │                     │  ResultSet→User    │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   User object       │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   User object        │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("user", user)      │                     │                    │
   │                       │  + enum lists        │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  editUserForm.html    │                      │                     │                    │
   │  (pre-filled)         │                      │                     │                    │
```

---

### POST /user/editUser
**Update User**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /user/editUser  │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  Form data + hidden   │                      │                     │                    │
   │  userId = 42          │                      │                     │                    │
   │                       │  UserController      │                     │                    │
   │                       │  .updateUser()       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to User       │                     │                    │
   │                       │  (includes userId)   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  editUser(user)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  UserService        │                    │
   │                       │                      │  .editUser()        │                    │
   │                       │                      │                     │                    │
   │                       │                      │  editUser(user)     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  UserRepository    │
   │                       │                      │                     │  .editUser()       │
   │                       │                      │                     │                    │
   │                       │                      │                     │  UPDATE USERS SET  │
   │                       │                      │                     │  userName=?,       │
   │                       │                      │                     │  userEmail=?, ...  │
   │                       │                      │                     │  WHERE user_id=42  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row updated      │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /user/profile        │                      │                     │                    │
```

---

### GET /user/deleteUser/{userId}
**Delete User**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /user/delete/42  │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  UserController      │                     │                    │
   │                       │  .deleteUser()       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  userId = 42         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  deleteUser(42)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  UserService        │                    │
   │                       │                      │  .deleteUser()      │                    │
   │                       │                      │                     │                    │
   │                       │                      │  deleteUser(42)     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  UserRepository    │
   │                       │                      │                     │  .deleteUser()     │
   │                       │                      │                     │                    │
   │                       │                      │                     │  DELETE FROM USERS │
   │                       │                      │                     │  WHERE user_id=42  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row deleted      │
   │                       │                      │                     │                    │
   │                       │                      │                     │   CASCADE DELETE:  │
   │                       │                      │                     │   USERS_PROJECT    │
   │                       │                      │                     │   USERS_SUBTASK    │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   Rows affected: 1  │                    │
   │                       │<─────────────────────┤                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /user/profile        │                      │                     │                    │
```

---

## Project Management Endpoints

### GET /project/myprojects
**View PM's Projects (with JOIN)**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /project/        │                      │                     │                    │
   │  myprojects           │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  ProjectController   │                     │                    │
   │                       │  .myProjects()       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session      │                     │                    │
   │                       │  & PROJECTMANAGER    │                     │                    │
   │                       │  role                │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getProjectsByUserId │                     │                    │
   │                       │  (user.getUserId())  │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  ProjectService     │                    │
   │                       │                      │  .getProjects...()  │                    │
   │                       │                      │                     │                    │
   │                       │                      │  getProjects...     │                    │
   │                       │                      │  ByUserId(userId)   │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  ProjectRepository │
   │                       │                      │                     │  .getProjects...() │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  PROJECT p         │
   │                       │                      │                     │  JOIN USERS_PROJECT│
   │                       │                      │                     │  up ON             │
   │                       │                      │                     │  p.project_id =    │
   │                       │                      │                     │  up.project_id     │
   │                       │                      │                     │  WHERE             │
   │                       │                      │                     │  up.user_id = ?    │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (project rows    │
   │                       │                      │                     │    for this PM)    │
   │                       │                      │                     │                    │
   │                       │                      │                     │  RowMapper: each   │
   │                       │                      │                     │  row → Project     │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<Project>     │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Project>      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("projects", list)  │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  projects.html        │                      │                     │                    │
```

---

### GET /project/add
**Show Add Project Form**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /project/add     │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  ProjectController   │                     │                    │
   │                       │  .addProject()       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getTeams()          │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      │  .getTeams()        │                    │
   │                       │                      │                     │                    │
   │                       │                      │  getTeams()         │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  .getTeams()       │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  TEAM              │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (all teams)      │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<Team>        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Team>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Create new Project()│                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("newProject")      │                     │                    │
   │                       │  ("availableTeams")  │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  addProjectForm.html  │                      │                     │                    │
   │  with team checkboxes │                      │                     │                    │
```

---

### POST /project/add
**Create New Project + Assign PM + Teams**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /project/add    │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  Project data +       │                      │                     │                    │
   │  selectedTeamIds[]    │                      │                     │                    │
   │                       │  ProjectController   │                     │                    │
   │                       │  .createProject()    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to Project    │                     │                    │
   │                       │  @RequestParam gets  │                     │                    │
   │                       │  selectedTeamIds     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  createProject(proj) │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  ProjectService     │                    │
   │                       │                      │  .createProject()   │                    │
   │                       │                      │                     │                    │
   │                       │                      │  createProject()    │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  ProjectRepository │
   │                       │                      │                     │  .createProject()  │
   │                       │                      │                     │                    │
   │                       │                      │                     │  INSERT INTO       │
   │                       │                      │                     │  PROJECT(name,     │
   │                       │                      │                     │  description, ...) │
   │                       │                      │                     │  VALUES (?,?,...)  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row inserted     │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT            │
   │                       │                      │                     │  LAST_INSERT_ID()  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   project_id = 5   │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   project_id = 5    │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   project_id = 5     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  assignProjectToUser │                     │                    │
   │                       │  (projectId, userId) │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  ProjectService     │                    │
   │                       │                      │  .assignProject...()│                    │
   │                       │                      │                     │                    │
   │                       │                      │  assignProject...() │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  ProjectRepository │
   │                       │                      │                     │  .assignProject..()│
   │                       │                      │                     │                    │
   │                       │                      │                     │  INSERT INTO       │
   │                       │                      │                     │  USERS_PROJECT     │
   │                       │                      │                     │  (user_id,         │
   │                       │                      │                     │   project_id)      │
   │                       │                      │                     │  VALUES (?,?)      │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   PM linked to     │
   │                       │                      │                     │   project          │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each teamId in  │                     │                    │
   │                       │  selectedTeamIds:    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findTeam(teamId)    │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  SELECT team       │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   Team object        │                     │                    │
   │                       │                      │                     │                    │
   │                       │  team.setProjectId(5)│                     │                    │
   │                       │  editTeam(team)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  UPDATE TEAM       │
   │                       │                      │                     │  SET project_id=5  │
   │                       │                      │                     │  WHERE team_id=?   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │  (repeat for each    │                     │                    │
   │                       │   selected team)     │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /project/myprojects  │                      │                     │                    │
```

---

### GET /project/edit/{projectId}
**Show Edit Project Form**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /project/edit/5  │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  ProjectController   │                     │                    │
   │                       │  .editProject()      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  projectId = 5       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findProject(5)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  ProjectService     │                    │
   │                       │                      │  .findProject()     │                    │
   │                       │                      │                     │                    │
   │                       │                      │  findProject(5)     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  ProjectRepository │
   │                       │                      │                     │  .findProject()    │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  PROJECT WHERE     │
   │                       │                      │                     │  project_id = 5    │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (project row)    │
   │                       │                      │                     │                    │
   │                       │                      │                     │  RowMapper:        │
   │                       │                      │                     │  ResultSet→Project │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   Project object    │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   Project object     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getTeams()          │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  (Get all teams)    │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Team>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("project")         │                     │                    │
   │                       │  ("availableTeams")  │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  editProjectForm.html │                      │                     │                    │
   │  (pre-filled)         │                      │                     │                    │
```

---

### POST /project/edit
**Update Project + Reassign Teams**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /project/edit   │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  Project data +       │                      │                     │                    │
   │  hidden projectId +   │                      │                     │                    │
   │  selectedTeamIds[]    │                      │                     │                    │
   │                       │  ProjectController   │                     │                    │
   │                       │  .updateProject()    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to Project    │                     │                    │
   │                       │  (includes projectId)│                     │                    │
   │                       │                      │                     │                    │
   │                       │  editProject(proj)   │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  ProjectService     │                    │
   │                       │                      │  .editProject()     │                    │
   │                       │                      │                     │                    │
   │                       │                      │  editProject()      │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  ProjectRepository │
   │                       │                      │                     │  .editProject()    │
   │                       │                      │                     │                    │
   │                       │                      │                     │  UPDATE PROJECT    │
   │                       │                      │                     │  SET name=?,       │
   │                       │                      │                     │  description=?, ...│
   │                       │                      │                     │  WHERE             │
   │                       │                      │                     │  project_id = 5    │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row updated      │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │                      │                     │                    │
   │                       │  Get old teams:      │                     │                    │
   │                       │  getTeams(projectId) │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  (Query teams with  │                    │
   │                       │                      │   project_id = 5)   │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Team>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each old team   │                     │                    │
   │                       │  NOT in new          │                     │                    │
   │                       │  selection:          │                     │                    │
   │                       │                      │                     │                    │
   │                       │  team.setProjectId   │                     │                    │
   │                       │  (null)              │                     │                    │
   │                       │  editTeam(team)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  UPDATE TEAM       │
   │                       │                      │                     │  SET project_id=   │
   │                       │                      │                     │  NULL WHERE        │
   │                       │                      │                     │  team_id = ?       │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each teamId in  │                     │                    │
   │                       │  selectedTeamIds:    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findTeam(teamId)    │                     │                    │
   │                       │  team.setProjectId(5)│                     │                    │
   │                       │  editTeam(team)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  UPDATE TEAM       │
   │                       │                      │                     │  SET project_id=5  │
   │                       │                      │                     │  WHERE team_id=?   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /project/myprojects  │                      │                     │                    │
```

---

### GET /project/delete/{projectId}
**Delete Project (with CASCADE)**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /project/        │                      │                     │                    │
   │  delete/5             │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  ProjectController   │                     │                    │
   │                       │  .deleteProject()    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  projectId = 5       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  deleteProject(5)    │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  ProjectService     │                    │
   │                       │                      │  .deleteProject()   │                    │
   │                       │                      │                     │                    │
   │                       │                      │  deleteProject(5)   │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  ProjectRepository │
   │                       │                      │                     │  .deleteProject()  │
   │                       │                      │                     │                    │
   │                       │                      │                     │  DELETE FROM       │
   │                       │                      │                     │  PROJECT WHERE     │
   │                       │                      │                     │  project_id = 5    │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row deleted      │
   │                       │                      │                     │                    │
   │                       │                      │                     │   CASCADE DELETE:  │
   │                       │                      │                     │   ↓                │
   │                       │                      │                     │   DELETE SUBPROJECT│
   │                       │                      │                     │   WHERE            │
   │                       │                      │                     │   project_id = 5   │
   │                       │                      │                     │   ↓                │
   │                       │                      │                     │   DELETE TASK      │
   │                       │                      │                     │   WHERE            │
   │                       │                      │                     │   sub_project_id   │
   │                       │                      │                     │   matches          │
   │                       │                      │                     │   ↓                │
   │                       │                      │                     │   DELETE SUBTASK   │
   │                       │                      │                     │   WHERE task_id    │
   │                       │                      │                     │   matches          │
   │                       │                      │                     │   ↓                │
   │                       │                      │                     │   DELETE           │
   │                       │                      │                     │   USERS_PROJECT    │
   │                       │                      │                     │   WHERE            │
   │                       │                      │                     │   project_id = 5   │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   Rows affected     │                    │
   │                       │<─────────────────────┤                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /project/myprojects  │                      │                     │                    │
```

---

## SubProject Management Endpoints

### GET /subproject/list/{projectId}
**View SubProjects for Project**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /subproject/     │                      │                     │                    │
   │  list/5               │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  SubProjectController│                     │                    │
   │                       │  .listSubProjects()  │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  projectId = 5       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findProject(5)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  (Get Project obj)  │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │                      │                     │                    │
   │                       │  getSubProjects      │                     │                    │
   │                       │  ByProjectId(5)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubProjectService  │                    │
   │                       │                      │  .getSubProjects... │                    │
   │                       │                      │                     │                    │
   │                       │                      │  getSubProjects...  │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubProjectRepo    │
   │                       │                      │                     │  .getSubProjects..│
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  SUBPROJECT WHERE  │
   │                       │                      │                     │  project_id = 5    │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (subproject rows)│
   │                       │                      │                     │                    │
   │                       │                      │                     │  RowMapper: each   │
   │                       │                      │                     │  row → SubProject  │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<SubProject>  │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<SubProject>   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getTotalActualTime  │                     │                    │
   │                       │  (projectId)         │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  (Calculate sum of  │                    │
   │                       │                      │   all subproject    │                    │
   │                       │                      │   actualTime)       │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   totalActualTime    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("subProjects")     │                     │                    │
   │                       │  ("project")         │                     │                    │
   │                       │  ("totalActualTime") │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  subprojects.html     │                      │                     │                    │
```

---

### GET /subproject/add/{projectId}
**Show Add SubProject Form**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /subproject/     │                      │                     │                    │
   │  add/5                │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  SubProjectController│                     │                    │
   │                       │  .addSubProject()    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  projectId = 5       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  allAvailableTeams   │                     │                    │
   │                       │  For_SubProject(5)   │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      │  (Get teams for     │                    │
   │                       │                      │   this project or   │                    │
   │                       │                      │   unassigned)       │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Team>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Create new          │                     │                    │
   │                       │  SubProject()        │                     │                    │
   │                       │  .setProjectId(5)    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("newSubProject")   │                     │                    │
   │                       │  ("availableTeams")  │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  addSubProjectForm    │                      │                     │                    │
   │  .html                │                      │                     │                    │
```

---

### POST /subproject/add
**Create New SubProject + Assign Teams**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /subproject/add │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  SubProject data +    │                      │                     │                    │
   │  hidden projectId +   │                      │                     │                    │
   │  selectedTeamIds[]    │                      │                     │                    │
   │                       │  SubProjectController│                     │                    │
   │                       │  .createSubProject() │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to SubProject │                     │                    │
   │                       │  (projectId from     │                     │                    │
   │                       │   hidden field)      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  createSubProject    │                     │                    │
   │                       │  (subProject)        │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubProjectService  │                    │
   │                       │                      │  .createSubProject()│                    │
   │                       │                      │                     │                    │
   │                       │                      │  createSubProject() │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubProjectRepo    │
   │                       │                      │                     │  .createSubProject│
   │                       │                      │                     │                    │
   │                       │                      │                     │  INSERT INTO       │
   │                       │                      │                     │  SUBPROJECT        │
   │                       │                      │                     │  (name, desc, ..., │
   │                       │                      │                     │   project_id)      │
   │                       │                      │                     │  VALUES (?,?,..,5) │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row inserted     │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT            │
   │                       │                      │                     │  LAST_INSERT_ID()  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   sub_project_id=12│
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   subProjectId = 12 │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   subProjectId = 12  │                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each teamId in  │                     │                    │
   │                       │  selectedTeamIds:    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findTeam(teamId)    │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   Team object        │                     │                    │
   │                       │                      │                     │                    │
   │                       │  team.setSubProject  │                     │                    │
   │                       │  Id(12)              │                     │                    │
   │                       │  editTeam(team)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  UPDATE TEAM SET   │
   │                       │                      │                     │  sub_project_id=12 │
   │                       │                      │                     │  WHERE team_id=?   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │  (repeat for each    │                     │                    │
   │                       │   selected team)     │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /subproject/list/5   │                      │                     │                    │
```

---

### GET /subproject/edit/{subProjectId}
**Show Edit SubProject Form**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /subproject/     │                      │                     │                    │
   │  edit/12              │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  SubProjectController│                     │                    │
   │                       │  .editSubProject()   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  subProjectId = 12   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findSubProject(12)  │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubProjectService  │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubProjectRepo    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  SUBPROJECT WHERE  │
   │                       │                      │                     │  sub_project_id=12 │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (subproject row) │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   SubProject object │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   SubProject object  │                     │                    │
   │                       │                      │                     │                    │
   │                       │  allAvailableTeams   │                     │                    │
   │                       │  For_SubProject      │                     │                    │
   │                       │  (projectId, 12)     │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Team>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("subProject")      │                     │                    │
   │                       │  ("availableTeams")  │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  editSubProjectForm   │                      │                     │                    │
   │  .html (pre-filled)   │                      │                     │                    │
```

---

### POST /subproject/edit
**Update SubProject + Reassign Teams**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /subproject/    │                      │                     │                    │
   │  edit                 │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  SubProject data +    │                      │                     │                    │
   │  selectedTeamIds[]    │                      │                     │                    │
   │                       │  SubProjectController│                     │                    │
   │                       │  .updateSubProject() │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to SubProject │                     │                    │
   │                       │                      │                     │                    │
   │                       │  editSubProject      │                     │                    │
   │                       │  (subProject)        │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubProjectService  │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubProjectRepo    │
   │                       │                      │                     │  UPDATE SUBPROJECT │
   │                       │                      │                     │  SET name=?, ...   │
   │                       │                      │                     │  WHERE             │
   │                       │                      │                     │  sub_project_id=12 │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │                      │                     │                    │
   │                       │  Get old teams with  │                     │                    │
   │                       │  sub_project_id=12   │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Team>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each old team   │                     │                    │
   │                       │  NOT in new          │                     │                    │
   │                       │  selection:          │                     │                    │
   │                       │                      │                     │                    │
   │                       │  team.setSubProject  │                     │                    │
   │                       │  Id(null)            │                     │                    │
   │                       │  team.setTaskId(null)│                     │                    │
   │                       │  editTeam(team)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  UPDATE TEAM SET   │
   │                       │                      │                     │  sub_project_id=   │
   │                       │                      │                     │  NULL,             │
   │                       │                      │                     │  task_id = NULL    │
   │                       │                      │                     │  WHERE team_id=?   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each teamId in  │                     │                    │
   │                       │  selectedTeamIds:    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  team.setSubProject  │                     │                    │
   │                       │  Id(12)              │                     │                    │
   │                       │  editTeam(team)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  UPDATE TEAM SET   │
   │                       │                      │                     │  sub_project_id=12 │
   │                       │                      │                     │  WHERE team_id=?   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /subproject/list/    │                      │                     │                    │
   │  (projectId)          │                      │                     │                    │
```

---

### GET /subproject/delete/{subProjectId}
**Delete SubProject (with CASCADE)**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /subproject/     │                      │                     │                    │
   │  delete/12            │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  SubProjectController│                     │                    │
   │                       │  .deleteSubProject() │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  subProjectId = 12   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getProjectIdBy      │                     │                    │
   │                       │  SubProjectId(12)    │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  (Get parent        │                    │
   │                       │                      │   project_id for    │                    │
   │                       │                      │   redirect)         │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   projectId = 5      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  deleteSubProject(12)│                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubProjectService  │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubProjectRepo    │
   │                       │                      │                     │  DELETE FROM       │
   │                       │                      │                     │  SUBPROJECT WHERE  │
   │                       │                      │                     │  sub_project_id=12 │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row deleted      │
   │                       │                      │                     │                    │
   │                       │                      │                     │   CASCADE DELETE:  │
   │                       │                      │                     │   ↓                │
   │                       │                      │                     │   DELETE TASK      │
   │                       │                      │                     │   WHERE            │
   │                       │                      │                     │   sub_project_id=12│
   │                       │                      │                     │   ↓                │
   │                       │                      │                     │   DELETE SUBTASK   │
   │                       │                      │                     │   WHERE task_id    │
   │                       │                      │                     │   matches          │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /subproject/list/5   │                      │                     │                    │
```

---

## Task Management Endpoints

### GET /task/list/{subProjectId}
**View Tasks for SubProject**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /task/list/12    │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  TaskController      │                     │                    │
   │                       │  .listTasks()        │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  subProjectId = 12   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findSubProject(12)  │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  (Get SubProject    │                    │
   │                       │                      │   object)           │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   SubProject object  │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getTasksBySubProject│                     │                    │
   │                       │  Id(12)              │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TaskService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TaskRepository    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  TASK WHERE        │
   │                       │                      │                     │  sub_project_id=12 │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (task rows)      │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<Task>        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Task>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getTotalActualTime  │                     │                    │
   │                       │  (subProjectId)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  (Calculate sum)    │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   totalActualTime    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("tasks")           │                     │                    │
   │                       │  ("subProject")      │                     │                    │
   │                       │  ("totalActualTime") │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render: tasks.html   │                      │                     │                    │
```

---

### GET /task/add/{subProjectId}
**Show Add Task Form**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /task/add/12     │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  TaskController      │                     │                    │
   │                       │  .addTask()          │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  subProjectId = 12   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  allAvailableTeams   │                     │                    │
   │                       │  For_Task(12)        │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Team>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Create new Task()   │                     │                    │
   │                       │  .setSubProjectId(12)│                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("newTask")         │                     │                    │
   │                       │  ("availableTeams")  │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  addTaskForm.html     │                      │                     │                    │
```

---

### POST /task/add
**Create New Task + Assign Teams**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /task/add       │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  Task data +          │                      │                     │                    │
   │  selectedTeamIds[]    │                      │                     │                    │
   │                       │  TaskController      │                     │                    │
   │                       │  .createTask()       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to Task       │                     │                    │
   │                       │  (subProjectId from  │                     │                    │
   │                       │   hidden field)      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  createTask(task)    │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TaskService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TaskRepository    │
   │                       │                      │                     │  INSERT INTO TASK  │
   │                       │                      │                     │  (name, ...,       │
   │                       │                      │                     │   sub_project_id)  │
   │                       │                      │                     │  VALUES (?,?,12)   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row inserted     │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT            │
   │                       │                      │                     │  LAST_INSERT_ID()  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   task_id = 25     │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   task_id = 25       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each teamId in  │                     │                    │
   │                       │  selectedTeamIds:    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findTeam(teamId)    │                     │                    │
   │                       │  team.setTaskId(25)  │                     │                    │
   │                       │  editTeam(team)      │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  UPDATE TEAM SET   │
   │                       │                      │                     │  task_id = 25      │
   │                       │                      │                     │  WHERE team_id=?   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /task/list/12        │                      │                     │                    │
```

---

*Note: Edit and Delete flows for Task follow the same patterns as SubProject.*

---

## Subtask Management Endpoints

### GET /subtask/list/{taskId}
**View Subtasks with Assigned Developers**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /subtask/        │                      │                     │                    │
   │  list/25              │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  SubtaskController   │                     │                    │
   │                       │  .listSubtasks()     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  taskId = 25         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findTask(25)        │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TaskService        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   Task object        │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getSubtasksByTaskId │                     │                    │
   │                       │  (25)                │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubtaskService     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubtaskRepository │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  SUBTASK WHERE     │
   │                       │                      │                     │  task_id = 25      │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (subtask rows)   │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<Subtask>     │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Subtask>      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each subtask:   │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getDevsBySubtaskId  │                     │                    │
   │                       │  (subtaskId)         │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubtaskService     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubtaskRepository │
   │                       │                      │                     │  SELECT u.* FROM   │
   │                       │                      │                     │  USERS u JOIN      │
   │                       │                      │                     │  USERS_SUBTASK us  │
   │                       │                      │                     │  ON u.user_id =    │
   │                       │                      │                     │  us.user_id WHERE  │
   │                       │                      │                     │  us.sub_task_id=?  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (assigned devs)  │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<User>        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<User>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Add to              │                     │                    │
   │                       │  assignedDevsMap     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  (repeat for each    │                     │                    │
   │                       │   subtask)           │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("subtasks")        │                     │                    │
   │                       │  ("assignedDevsMap") │                     │                    │
   │                       │  ("task")            │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  subtasks.html        │                      │                     │                    │
```

---

### GET /subtask/add/{taskId}
**Show Add Subtask Form**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /subtask/add/25  │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  SubtaskController   │                     │                    │
   │                       │  .addSubtask()       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @PathVariable       │                     │                    │
   │                       │  taskId = 25         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getAvailableDevs    │                     │                    │
   │                       │  (taskId)            │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubtaskService     │                    │
   │                       │                      │  (Get devs from     │                    │
   │                       │                      │   assigned teams,   │                    │
   │                       │                      │   filter out those  │                    │
   │                       │                      │   already assigned  │                    │
   │                       │                      │   to other subtasks)│                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<User>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Create new Subtask()│                     │                    │
   │                       │  .setTaskId(25)      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("newSubtask")      │                     │                    │
   │                       │  ("availableDevs")   │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render:              │                      │                     │                    │
   │  addSubtaskForm.html  │                      │                     │                    │
```

---

### POST /subtask/add
**Create Subtask + Assign Devs (Junction Table)**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /subtask/add    │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  Subtask data +       │                      │                     │                    │
   │  selectedDevIds[]     │                      │                     │                    │
   │                       │  SubtaskController   │                     │                    │
   │                       │  .createSubtask()    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to Subtask    │                     │                    │
   │                       │  (taskId from hidden)│                     │                    │
   │                       │                      │                     │                    │
   │                       │  createSubtask       │                     │                    │
   │                       │  (subtask)           │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubtaskService     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubtaskRepository │
   │                       │                      │                     │  INSERT INTO       │
   │                       │                      │                     │  SUBTASK (name,    │
   │                       │                      │                     │  ..., task_id)     │
   │                       │                      │                     │  VALUES (?,?,25)   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row inserted     │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT            │
   │                       │                      │                     │  LAST_INSERT_ID()  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   sub_task_id = 78 │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   subtaskId = 78     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each devId in   │                     │                    │
   │                       │  selectedDevIds:     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  assignDevToSubtask  │                     │                    │
   │                       │  (devId, 78)         │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubtaskService     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubtaskRepository │
   │                       │                      │                     │  INSERT INTO       │
   │                       │                      │                     │  USERS_SUBTASK     │
   │                       │                      │                     │  (user_id,         │
   │                       │                      │                     │   sub_task_id)     │
   │                       │                      │                     │  VALUES (devId, 78)│
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Junction row     │
   │                       │                      │                     │   created          │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │  (repeat for each    │                     │                    │
   │                       │   selected dev)      │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /subtask/list/25     │                      │                     │                    │
```

---

### POST /subtask/edit
**Update Subtask + Reassign Devs (Delete All → Insert New)**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /subtask/edit   │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  Subtask data +       │                      │                     │                    │
   │  selectedDevIds[]     │                      │                     │                    │
   │                       │  SubtaskController   │                     │                    │
   │                       │  .updateSubtask()    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to Subtask    │                     │                    │
   │                       │                      │                     │                    │
   │                       │  editSubtask(subtask)│                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubtaskService     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubtaskRepository │
   │                       │                      │                     │  UPDATE SUBTASK    │
   │                       │                      │                     │  SET name=?, ...   │
   │                       │                      │                     │  WHERE             │
   │                       │                      │                     │  sub_task_id = 78  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │                      │                     │                    │
   │                       │  removeAllDevsFrom   │                     │                    │
   │                       │  Subtask(78)         │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubtaskService     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubtaskRepository │
   │                       │                      │                     │  DELETE FROM       │
   │                       │                      │                     │  USERS_SUBTASK     │
   │                       │                      │                     │  WHERE             │
   │                       │                      │                     │  sub_task_id = 78  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   All old          │
   │                       │                      │                     │   assignments      │
   │                       │                      │                     │   deleted          │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each devId in   │                     │                    │
   │                       │  selectedDevIds:     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  assignDevToSubtask  │                     │                    │
   │                       │  (devId, 78)         │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  SubtaskService     │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  SubtaskRepository │
   │                       │                      │                     │  INSERT INTO       │
   │                       │                      │                     │  USERS_SUBTASK     │
   │                       │                      │                     │  (user_id,         │
   │                       │                      │                     │   sub_task_id)     │
   │                       │                      │                     │  VALUES (devId, 78)│
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   New junction row │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │  (repeat for each    │                     │                    │
   │                       │   selected dev)      │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /subtask/list/       │                      │                     │                    │
   │  (taskId)             │                      │                     │                    │
```

---

## Team Management Endpoints

### GET /team/list
**View All Teams with Members**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  GET /team/list       │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │                       │  TeamController      │                     │                    │
   │                       │  .listTeams()        │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getTeams()          │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  TEAM              │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (all team rows)  │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<Team>        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<Team>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each team:      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  getTeamDevs(teamId) │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  SELECT * FROM     │
   │                       │                      │                     │  USERS WHERE       │
   │                       │                      │                     │  team_id = ?       │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   (member rows)    │
   │                       │                      │<────────────────────┤                    │
   │                       │                      │   List<User>        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   List<User>         │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Add to              │                     │                    │
   │                       │  teamMembersMap      │                     │                    │
   │                       │                      │                     │                    │
   │                       │  (repeat for each    │                     │                    │
   │                       │   team)              │                     │                    │
   │                       │                      │                     │                    │
   │                       │  model.addAttribute  │                     │                    │
   │                       │  ("teams")           │                     │                    │
   │                       │  ("teamMembersMap")  │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  Render: teams.html   │                      │                     │                    │
```

---

### POST /team/add
**Create Team + Assign Devs (via USERS.team_id FK)**

```
Browser                Controller              Service              Repository           Database
   │                       │                      │                     │                    │
   │  POST /team/add       │                      │                     │                    │
   ├──────────────────────>│                      │                     │                    │
   │  Team data +          │                      │                     │                    │
   │  selectedDevIds[]     │                      │                     │                    │
   │                       │  TeamController      │                     │                    │
   │                       │  .createTeam()       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  Check: session &    │                     │                    │
   │                       │  PM role             │                     │                    │
   │                       │                      │                     │                    │
   │                       │  @ModelAttribute     │                     │                    │
   │                       │  binds to Team       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  createTeam(team)    │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  TeamService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  TeamRepository    │
   │                       │                      │                     │  INSERT INTO TEAM  │
   │                       │                      │                     │  (teamName, ...)   │
   │                       │                      │                     │  VALUES (?, ...)   │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   Row inserted     │
   │                       │                      │                     │                    │
   │                       │                      │                     │  SELECT            │
   │                       │                      │                     │  LAST_INSERT_ID()  │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   team_id = 10     │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   team_id = 10       │                     │                    │
   │                       │                      │                     │                    │
   │                       │  For each devId in   │                     │                    │
   │                       │  selectedDevIds:     │                     │                    │
   │                       │                      │                     │                    │
   │                       │  findUser(devId)     │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  UserService        │                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │   User object        │                     │                    │
   │                       │                      │                     │                    │
   │                       │  dev.setTeamId(10)   │                     │                    │
   │                       │  editUser(dev)       │                     │                    │
   │                       ├─────────────────────>│                     │                    │
   │                       │                      │  UserService        │                    │
   │                       │                      ├────────────────────>│                    │
   │                       │                      │                     │  UserRepository    │
   │                       │                      │                     │  UPDATE USERS SET  │
   │                       │                      │                     │  team_id = 10      │
   │                       │                      │                     │  WHERE user_id = ? │
   │                       │                      │                     ├───────────────────>│
   │                       │                      │                     │<───────────────────┤
   │                       │                      │                     │   User updated     │
   │                       │                      │<────────────────────┤                    │
   │                       │<─────────────────────┤                     │                    │
   │                       │  (repeat for each    │                     │                    │
   │                       │   selected dev)      │                     │                    │
   │<──────────────────────┤                      │                     │                    │
   │  redirect:            │                      │                     │                    │
   │  /team/list           │                      │                     │                    │
```

---

## Summary

### Common Flow Pattern

All endpoints follow this structure:

1. **Browser** → HTTP request
2. **Controller** → Session & role check
3. **Controller** → Call Service method
4. **Service** → Pass through to Repository
5. **Repository** → Execute SQL query via JdbcTemplate
6. **Database** → Return data
7. **Repository** → RowMapper converts ResultSet → Java objects
8. **Service** → Return data to Controller
9. **Controller** → Add to Model or redirect
10. **Browser** → Display result

### Key Differences by Pattern

**One-to-Many (FK):**
- SubProject → Project (via `project_id` FK)
- Task → SubProject (via `sub_project_id` FK)
- Subtask → Task (via `task_id` FK)
- Team Members → Team (via `USERS.team_id` FK)

**Many-to-Many (Junction Table):**
- PM ↔ Project (via `USERS_PROJECT`)
- Developer ↔ Subtask (via `USERS_SUBTASK`)

**Assignment Patterns:**
- **FK assignment:** Set FK field → UPDATE entity
- **Junction assignment:** INSERT into junction table
- **Reassignment:** Delete old → Insert new (for junction tables)

---

**Total Endpoints Documented:** 34 complete request flows showing method names, layer transitions, SQL queries, and database operations.