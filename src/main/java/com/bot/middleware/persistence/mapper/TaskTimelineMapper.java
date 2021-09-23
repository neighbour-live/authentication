package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.TaskTimeline;
import com.bot.middleware.persistence.dto.TaskTimelineDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskTimelineMapper {

    public static TaskTimelineDTO createTaskTimelineDTO(TaskTimeline taskTimeline){
        TaskTimelineDTO taskTimelineDTO = TaskTimelineDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(taskTimeline.getId()))
                .statusChangeDatetime(taskTimeline.getStatusChangeTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(taskTimeline.getTaskStatus())
                .poster( (taskTimeline.getPoster() == null) ? null : UserMapper.createUserMinimalDetailsDTOLazy(taskTimeline.getPoster()))
                .tasker( (taskTimeline.getTasker() == null) ? null : UserMapper.createUserMinimalDetailsDTOLazy(taskTimeline.getTasker()))
                .build();

        return taskTimelineDTO;
    }

    public static List<TaskTimelineDTO> createTaskTimelineDTOListLazy(List<TaskTimeline> taskTimelines) {
        List<TaskTimelineDTO> taskTimelineDTOS = new ArrayList<>();
        if(taskTimelines == null ) return taskTimelineDTOS;

        taskTimelines.forEach(taskTimeline -> taskTimelineDTOS.add(createTaskTimelineDTO(taskTimeline)));
        return taskTimelineDTOS;
    }
}
