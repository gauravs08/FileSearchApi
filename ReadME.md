# File search api

## Tech used
    - Java 17+
    - Spring boot 3.1.0
    - Spring reactive web flux
    - Spring security 
    - Spring actuator health check 
    - Spring cache + Caffeine Cache
    - Lombok
    - JUnit

## Description

The goal of this api is to develop a Java 11+ Spring Boot application that
takes a text file as input and finds the top K most frequent words within it.
Below are the specific requirements and bonus points for this assignment:

### Backend Requirements

- The API should have an endpoint that takes a text file and a value for K as input.

- The API should read the text file and find the K most frequent words.

- The API should return the K most frequent words and their frequency in descending order.

- The API should have tests to ensure its functionality.

### Bonus Points (Optional)

- **Caching Mechanism:** Implement a caching mechanism to avoid re-calculating the top K most frequent words every time
  the API is called with the same text file and K value.

- **Large File Handling:** Handle cases where the input text file is very large and cannot fit into memory.

- **Efficient Algorithm:** Use a more efficient algorithm to find the top K most frequent words, such as a heap or a
  trie data structure.

- **User Authentication and Authorization:** Implement user authentication and authorization so that only authenticated
  users can access the app.
  Two users to check different roles and permissions on endpoints, below is list of apis that are visible to each user.
    - **ROLE: ADMIN**
        - username: admin & password: password
            - ```/api/file/upload```
            - ```/api/users/welcome```
            - ```/actuator/health```
    - **ROLE: USER**
        - username: user & password: password
            - ```/api/users/welcome```
            - ```/actuator/health```
- **Health Check:** Heath check is also added and status can be tested using ``http://localhost:8080/actuator/health``