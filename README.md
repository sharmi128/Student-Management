# Smart College Management System

Minimal Java Swing demo for student/teacher management used in class.

## Build

```bash
mvn package -DskipTests
```

## Run

```bash
java -cp target/classes com.smartcollege.erp.App
```

## Login

- Username: `admin`
- Password: `admin123`# Smart College Management System

Desktop application built with Java 17 and Maven.

## Features

- Student management
- Teacher management
- Attendance management
- Fees tracking
- Notices module

## Tech Stack

- Java 17
- Swing UI
- Maven

## Run Locally

1. Open terminal in project root.
2. Build the project:

	mvn clean package

3. Run the app main class:

	com.smartcollege.erp.App

## GitHub Browser Output

This repository now includes an `index.html` file in the project root.

To publish browser output with GitHub Pages:

1. Push code to the `main` branch.
2. Open repository settings in GitHub.
3. Go to Pages.
4. Source: Deploy from a branch.
5. Branch: `main`, folder: `/(root)`.
6. Save and wait for deployment.

After deployment, your GitHub Pages URL will load `index.html`.

## Important Note

GitHub Pages can host only static content (HTML/CSS/JS).

This project is a Java Swing desktop app, so the full application UI runs locally on your machine, not inside GitHub Pages.
