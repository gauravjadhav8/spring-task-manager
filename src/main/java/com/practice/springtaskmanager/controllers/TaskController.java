package com.practice.springtaskmanager.controllers;

import com.practice.springtaskmanager.entities.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TaskController {

    public final List<Task> taskList;
    AtomicInteger taskCount = new AtomicInteger(0);

    public TaskController() {
        this.taskList = new ArrayList<>();
        this.taskList.add(new Task(taskCount.incrementAndGet(), "Task 1", " Simple Task 1", new Date()));
        this.taskList.add(new Task(taskCount.incrementAndGet(), "Task 2", " Simple Task 2", new Date()));
        this.taskList.add(new Task(taskCount.incrementAndGet(), "Task 3", " Simple Task 3", new Date()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasks")
    List<Task> getTasks() {
        return taskList;
    }

    @PostMapping(value = "/tasks")
    List<Task> createTask(@RequestBody Task task){
        taskList.add(new Task(taskCount.incrementAndGet(), task.getTitle(), task.getDescription(), task.getDueDate()));
        return getTasks();
    }

    @GetMapping("/tasks/{id}")
    Task getTask(@PathVariable("id") Integer id) {
        return taskList.stream().filter(task -> task.getId().equals(id))
                .findFirst().orElse(null);
    }

    @DeleteMapping("/tasks/{id}")
    ResponseEntity deleteTask(@PathVariable("id") Integer id) {
        Task t = getTask(id);
        if(t == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        taskList.remove(t);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/tasks/{id}")
    ResponseEntity updateTask(@PathVariable("id") Integer id, @RequestBody Task task) {
        Task t = getTask(id);
        if(t == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        if(task.getDescription()!=null)  t.setDescription(task.getDescription());
        if(task.getTitle()!=null)  t.setTitle(task.getTitle());
        if(task.getDueDate()!=null)  t.setDueDate(task.getDueDate());
        return ResponseEntity.ok(t);
    }

}
