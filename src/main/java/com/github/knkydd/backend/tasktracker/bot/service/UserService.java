package com.github.knkydd.backend.tasktracker.bot.service;

import com.github.knkydd.backend.tasktracker.bot.model.User;
import com.github.knkydd.backend.tasktracker.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOrCreateByChatId(long chatId) {
        Optional<User> user = userRepository.findById(chatId);
        if (user.isEmpty()) {
            return user.get();
        }
        try {
            User newUser = new User();
            newUser.setChatId(chatId);
            return userRepository.saveAndFlush(newUser);
        } catch (DataIntegrityViolationException e) {
            return userRepository.findById(chatId).orElseThrow(() -> e);
        }
    }
}
