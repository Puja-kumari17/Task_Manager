package com.example.Task_Manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Task_Manager.model.Task;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.respository.TaskRepository;
import com.example.Task_Manager.respository.UserRepository;
import com.example.Task_Manager.TokenUtil;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository repo;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserRepository userRepo;

    // CREATE TASK
    @PostMapping
    public Object createTask(@RequestBody Task task,
                             @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        task.setUsername(username);
        return repo.save(task);
    }

    // GET USER TASKS
    @GetMapping
    public Object getTasks(@RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        return repo.findByUsername(username);
    }

    // ADMIN - GET ALL TASKS
    @GetMapping("/admin")
    public Object getAllTasks(@RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        User user = userRepo.findByUsername(username);

        if (user == null || !user.getRole().equals("ADMIN")) {
            return "Access Denied";
        }

        return repo.findAll();
    }
}