package com.example.taskapp;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskDAO dao = new TaskDAO();

    @GetMapping
    public List<Task> viewTasks() throws Exception {
        return dao.getAllTasks();
    }

    @PostMapping("/add")
    public String createTask(@RequestParam String title) throws Exception {
        dao.addTask(title);
        return "Task Added";
    }

    @PutMapping("/done/{id}")
    public String completeTask(@PathVariable int id) throws Exception {
        dao.markDone(id);
        return "Task Completed";
    }

    @DeleteMapping("/delete/{id}")
    public String removeTask(@PathVariable int id) throws Exception {
        dao.deleteTask(id);
        return "Task Deleted";
    }
}