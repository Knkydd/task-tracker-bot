package com.github.knkydd.backend.tasktracker.bot.repository.jpa;

import com.github.knkydd.backend.tasktracker.bot.model.User;
import com.github.knkydd.backend.tasktracker.bot.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
}
