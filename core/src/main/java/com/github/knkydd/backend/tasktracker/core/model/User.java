package com.github.knkydd.backend.tasktracker.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    private long chatId;

    public User(long chatId) {
        this.chatId = chatId;
    }
}
