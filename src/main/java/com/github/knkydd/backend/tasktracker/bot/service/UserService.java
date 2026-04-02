package com.github.knkydd.backend.tasktracker.bot.service;

import com.github.knkydd.backend.tasktracker.bot.exception.GetOrCreateUserException;
import com.github.knkydd.backend.tasktracker.bot.model.User;
import com.github.knkydd.backend.tasktracker.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Optional<User> getOrCreateByChatId(long chatId) {
        try {
            Optional<User> user = userRepository.findById(chatId);
            if (user.isPresent()) {
                return user;
            }
            return Optional.ofNullable(userRepository.saveAndFlush(new User(chatId)));
        } catch (DataAccessException e) {
            throw new GetOrCreateUserException("Возникла ошибка создания или сохранения пользователя. "+ e.getMessage());
        }
    }
}
