package com.example.aws.application;

import com.example.aws.domain.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<String, User> users = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            // Read JSON file into the users list
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/test_users.json");
            var allUsers = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});
            allUsers.forEach(
                    x -> users.put(x.id(), x)
            );
            System.out.println("Users initialized: " + users);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize users", e);
        }
    }

    // Get all users
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    // Get user by ID
    public User getUserById(String id) {
        return users.get(id);
    }

    // Add a new user
    public void addUser(User user) {
        users.put(user.id(), user);
        saveUsersToJson(getAllUsers());
    }

    public void saveUsersToJson(List<User> users) {
        try {
            // Convert List<User> to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String json = writer.writeValueAsString(users);

            // Determine the resource folder path
            Path resourcePath = Path.of("src/main/resources", "/test_users.json");
            File file = resourcePath.toFile();

            // Write JSON to the file
            objectMapper.writeValue(file, users);

            System.out.println("Users successfully saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users to JSON", e);
        }
    }
}
