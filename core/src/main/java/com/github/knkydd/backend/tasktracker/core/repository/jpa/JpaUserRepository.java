package com.github.knkydd.backend.tasktracker.core.repository.jpa;

import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
}
