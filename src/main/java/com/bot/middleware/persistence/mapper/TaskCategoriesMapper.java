package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.TaskCategories;
import com.bot.middleware.persistence.dto.TaskCategoriesDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskCategoriesMapper {

    public static TaskCategoriesDTO createTaskCategoriesDTOLazy(TaskCategories taskCategories) {
        TaskCategoriesDTO taskCategoriesDTO = TaskCategoriesDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(taskCategories.getPublicId()))
                .name(taskCategories.getName())
                .iconUrl(taskCategories.getIconUrl())
                .build();

        return taskCategoriesDTO;
    }

    public static List<TaskCategoriesDTO> createTaskCategoriesDTOListLazy(Collection<TaskCategories> taskCategoriesCollection) {
        List<TaskCategoriesDTO> taskCategoriesDTOS = new ArrayList<>();
        taskCategoriesCollection.forEach(taskCategory -> taskCategoriesDTOS.add(createTaskCategoriesDTOLazy(taskCategory)));
        return taskCategoriesDTOS;
    }
}
