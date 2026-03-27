package com.github.knkydd.backend.tasktracker.bot.service;

import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public boolean deleteByTaskIdAndChatId(long taskId, long chatId){
        Optional<Task> existsTask = taskRepository.findByTaskId(taskId);
        if(existsTask.isEmpty()){
            return false;
        }
        taskRepository.deleteByTaskIdAndUserChatId(taskId, chatId);
        return true;
    }

    public void saveAndFlush(Task task){
        taskRepository.saveAndFlush(task);
    }

}
