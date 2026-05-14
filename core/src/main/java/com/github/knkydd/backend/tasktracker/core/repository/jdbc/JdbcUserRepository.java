package com.github.knkydd.backend.tasktracker.core.repository.jdbc;

import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.github.knkydd.backend.tasktracker.core.repository.jdbc.query.UserSqlQueries.SQL_FIND_USER;
import static com.github.knkydd.backend.tasktracker.core.repository.jdbc.query.UserSqlQueries.SQL_SAVE_USER;

@Profile("jdbc")
@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Optional<User> findById(long chatId) {
        return jdbcClient.sql(SQL_FIND_USER)
                .param("chatId", chatId)
                .query(User.class).optional();
    }

    @Override
    public User saveAndFlush(User user) {
        long chatId = user.getChatId();
        jdbcClient.sql(SQL_SAVE_USER)
                .param("chatId", chatId)
                .query();
        return user;
    }
}
