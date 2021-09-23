package com.bot.middleware.resources.services;

import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.TaskTimeline;
import com.bot.middleware.persistence.domain.User;

public interface TaskTimelineService {

    TaskTimeline addTaskTimeline(String status, Task task, User tasker, User poster);

}
