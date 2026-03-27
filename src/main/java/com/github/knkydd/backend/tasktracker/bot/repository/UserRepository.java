package com.github.knkydd.backend.tasktracker.bot.repository;

import com.github.knkydd.backend.tasktracker.bot.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(long chatId);

    User saveAndFlush(User user);
}
