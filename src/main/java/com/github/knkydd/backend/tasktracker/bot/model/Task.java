package com.github.knkydd.backend.tasktracker.bot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long taskId;

    @ManyToOne
    @JoinColumn(name="chatId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="categoryId", nullable = false)
    private TaskCategory category;

    private String description;

}
