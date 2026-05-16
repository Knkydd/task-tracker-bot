package com.github.knkydd.backend.tasktracker.core.web.responses;

public record TaskResponseDto(long taskId, String category, String description) {
}
