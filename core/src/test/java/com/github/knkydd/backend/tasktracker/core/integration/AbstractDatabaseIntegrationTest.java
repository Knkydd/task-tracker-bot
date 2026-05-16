package com.github.knkydd.backend.tasktracker.core.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@SpringBootTest
public class AbstractDatabaseIntegrationTest {

    @ServiceConnection
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18.1")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("init-schema.sql"),
                    "/docker-entrypoint-initdb.d/init-schema.sql"
            );

    static {
        postgres.start();
    }

}
