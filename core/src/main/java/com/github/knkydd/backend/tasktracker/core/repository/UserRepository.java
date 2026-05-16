package com.github.knkydd.backend.tasktracker.core.repository;

import com.github.knkydd.backend.tasktracker.core.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(long chatId);

    User saveAndFlush(User user);
}
