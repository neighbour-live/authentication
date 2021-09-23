package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.TaskTimeline;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.repository.TaskTimelineRepository;
import com.bot.middleware.resources.services.TaskTimelineService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class TaskTimelineServiceImpl implements TaskTimelineService {

    @Autowired
    private TaskTimelineRepository taskTimelineRepository;

    @Override
    public TaskTimeline addTaskTimeline(String status, Task task, User tasker, User poster) {
        TaskTimeline taskTimeline = new TaskTimeline();
        taskTimeline.setPublicId(PublicIdGenerator.generatePublicId());
        taskTimeline.setTaskStatus(status);
        taskTimeline.setTask(task);
        taskTimeline.setTasker(tasker);
        taskTimeline.setPoster(poster);
        taskTimeline.setStatusChangeTime(ZonedDateTime.now());
        taskTimeline.setIsActive(true);
        taskTimeline.setIsDeleted(false);

        taskTimeline = taskTimelineRepository.save(taskTimeline);
        return taskTimeline;
    }

}
