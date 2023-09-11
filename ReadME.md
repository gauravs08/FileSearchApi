# File search api 

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

- **Caching Mechanism:** Implement a caching mechanism to avoid re-calculating the top K most frequent words every time the API is called with the same text file and K value.

- **Large File Handling:** Handle cases where the input text file is very large and cannot fit into memory.

- **Efficient Algorithm:** Use a more efficient algorithm to find the top K most frequent words, such as a heap or a trie data structure.

- **User Authentication and Authorization:** Implement user authentication and authorization so that only authenticated users can access the app.


----------------Remove this later-------
## Getting Started

To get started with this assignment, you will need to:

1. Set up a Java 11+ Spring Boot project.

2. Create the required endpoints for uploading a text file, processing it, and returning the top K frequent words.

3. Develop the algorithm to analyze the text and find the top K frequent words.

4. Implement unit tests to verify the correctness of your code.

5. Optionally, add bonus features like caching, efficient algorithms, and security measures for bonus points.

## Submission Guidelines

Please submit your solution for this assignment as a well-organized and documented Spring Boot project. Include clear instructions on how to run and test the application.

Ensure that your code is well-structured, follows best practices, and includes comments where necessary to explain complex logic.

## Evaluation Criteria

Your submission will be evaluated based on the following criteria:

- **Functionality:** Does the API correctly find and return the top K most frequent words from the input text file?

- **Code Quality:** Is the code well-structured, readable, and maintainable? Does it follow Java and Spring Boot best practices?

- **Testing:** Are there sufficient unit tests to validate the functionality of the API?

- **Bonus Features:** If applicable, do the bonus features enhance the application's functionality and efficiency?

- **Documentation:** Is the README.md clear and comprehensive, providing instructions for running, testing, and understanding the code?

