package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserBid;
import com.bot.middleware.persistence.dto.TaskDTO;
import com.bot.middleware.persistence.dto.TaskScheduleDTO;
import com.bot.middleware.persistence.request.AddTask;
import com.stripe.exception.StripeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface TaskingService {
    Task addTask(AddTask addTask, User user) throws ResourceNotFoundException, UnauthorizedException, StripeException;

    Task editTask(User user, AddTask addTask, String taskPublicId) throws Exception;

    Collection<Task> getAllTasks(User poster, String status);

    Task getByTaskPublicId(String taskPublicId) throws ResourceNotFoundException;

    Task deleteTask(String taskPublicId, User poster) throws Exception;

    Page<User> getFeaturedTaskers(String city, Pageable pageable);

    List<UserBid> findAllTaskBids(String taskPublicId) throws ResourceNotFoundException;

    Task editTaskStatus(String taskPublicId, User user, String status) throws ResourceNotFoundException, UnauthorizedException, StripeException;

    Page<Task> searchTasks(List<String> publicIds, Float longitude, Float latitude, Long lowBudget, Long highBudget, Boolean remoteTask, Long radius, User user, Pageable pageable);

    Collection<Task> getAllBidderTasks(User user);

    Task rescheduleTaskRequest(String taskPublicId, User user, String rescheduleTime) throws Exception;

    List<TaskScheduleDTO> getTaskerBookedTimesForRescheduling(String taskPublicId, User user) throws Exception;

    boolean rescheduleTaskResponse(String taskPublicId, User user, boolean response) throws Exception;

    Integer getTasksCompletedByUserAsPoster(User user);

    Integer getTasksCompletedByUserAsTasker(User user);

    TaskDTO getTaskTimeline(String taskPublicId) throws Exception;
}
