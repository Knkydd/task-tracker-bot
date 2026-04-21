package com.github.knkydd.backend.tasktracker.core.service;

import com.github.knkydd.backend.tasktracker.core.exception.ServerException;
import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User getOrCreateUser(long chatId) {
        try {
            log.info("Попытка получить пользователя {} ...", chatId);
            Optional<User> user = userRepository.findById(chatId);
            return user.orElseGet(() -> userRepository.saveAndFlush(new User(chatId)));
        } catch (DataAccessException e) {
            throw new ServerException("Возникла ошибка создания или сохранения пользователя. " + e.getMessage());
        }
    }
}
