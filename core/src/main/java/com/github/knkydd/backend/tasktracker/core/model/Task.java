package com.github.knkydd.backend.tasktracker.core.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskId;

    @ManyToOne
    @JoinColumn(name = "chatId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private TaskCategory category;

    private String description;

    public Task(User user, TaskCategory category, String description) {
        this.user = user;
        this.category = category;
        this.description = description;
    }
}
