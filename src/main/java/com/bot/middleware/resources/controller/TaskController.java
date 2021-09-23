package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.TaskCategories;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserBid;
import com.bot.middleware.persistence.dto.StatusMessageDTO;
import com.bot.middleware.persistence.dto.TaskDTO;
import com.bot.middleware.persistence.dto.UserMinimalDetailsDTO;
import com.bot.middleware.persistence.mapper.TaskCategoriesMapper;
import com.bot.middleware.persistence.mapper.TaskMapper;
import com.bot.middleware.persistence.mapper.UserBidMapper;
import com.bot.middleware.persistence.mapper.UserMapper;
import com.bot.middleware.persistence.repository.TaskCategoriesRepository;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddTask;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.persistence.response.PageableResponseEntity;
import com.bot.middleware.persistence.type.Mediums;
import com.bot.middleware.resources.services.TaskingService;
import com.bot.middleware.security.UserPrincipal;
import com.bot.middleware.utility.AuthConstants;
import com.bot.middleware.utility.Constants;
import com.bot.middleware.utility.StatusCode;
import com.bot.middleware.utility.id.PublicIdGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskCategoriesRepository taskCategoriesRepository;

    @Autowired
    private TaskingService taskingService;

    @GetMapping("/categories")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all task categories.")
    public ResponseEntity<?> getAllTaskCategories() throws Exception {
        try {
            List<TaskCategories> taskCategories = taskCategoriesRepository.findAll();
            return GenericResponseEntity.create(StatusCode.SUCCESS, TaskCategoriesMapper.createTaskCategoriesDTOListLazy(taskCategories), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/bids")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all bids of task.")
    public ResponseEntity<?> getAllTaskBids(@RequestParam("userPublicId") String userPublicId, @RequestParam("taskPublicId") String taskPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            List<UserBid> userBids = taskingService.findAllTaskBids(taskPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserBidMapper.createBidDTOListLazy(userBids), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/bidder-tasks")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all bidder tasks.")
    public ResponseEntity<?> getAllBidderTasks(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            Collection<Task> tasks = taskingService.getAllBidderTasks(user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, TaskMapper.createTaskDTOListLazy(tasks, user), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add task.")
    public ResponseEntity<?> addTask(@Valid @RequestBody AddTask addTask) throws Exception {
        try {
            User user = isCurrentUser(addTask.getPosterPublicId());
            Task task = taskingService.addTask(addTask, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, TaskMapper.createTaskDTOLazy(task, user), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/{taskPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit task.")
    public ResponseEntity<?> editTask(@Valid @RequestBody AddTask addTask,
                                      @PathVariable String taskPublicId) throws Exception {
        try {
            User user = isCurrentUser(addTask.getPosterPublicId());
            taskingService.editTask(user, addTask, taskPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("Task " + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all tasks.")
    public ResponseEntity<?> getAllTasks(@RequestParam("userPublicId") String userPublicId, @Nullable @RequestParam(value = "statuses", required = false) String statuses) throws Exception {
        try {
            //APPROVED, ASSIGNED, PENDING, COMPLETED, DISPUTED
            User user = isCurrentUser(userPublicId);
            Collection<Task> tasks = taskingService.getAllTasks(user, statuses);
            return GenericResponseEntity.create(StatusCode.SUCCESS, TaskMapper.createTaskDTOListLazy(tasks, user), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/featured-taskers")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch featured taskers.")
    public ResponseEntity<?> getFeaturedTaskers(@RequestParam("userPublicId") String userPublicId,
                                                @RequestParam(value = "pageNo", required = false) String pageNo,
                                                @RequestParam(value = "pageSize", required = false) String pageSize,
                                                @Nullable @RequestParam(value = "medium", required = false) String medium,
                                                @Nullable @RequestParam(value = "city", required = false) String city) throws Exception {
        try {

            User user = new User();
            user.setPublicId(Long.valueOf(00000000000));
            //APPROVED, ASSIGNED, PENDING, COMPLETED, DISPUTED
            if(!Mediums.valueOf(medium).equals(Mediums.WEBSITE)){
                user = isCurrentUser(userPublicId);
            }

            if(Integer.valueOf(pageNo) < 0 || Integer.valueOf(pageSize) < 0){
                return GenericResponseEntity.create(StatusCode.SUCCESS, "PageNo and PageSize must be positive numbers", HttpStatus.OK);
            }
            Pageable pageable = (Pageable) PageRequest.of(Integer.valueOf(pageNo), Integer.valueOf(pageSize), Sort.by("taskerRatingAvg"));
            Page<User> userPage = taskingService.getFeaturedTaskers(city, pageable);
            List<UserMinimalDetailsDTO> userMinimalDetailsDTOS = UserMapper.createUserMinimalDetailsDTOListLazy(userPage.getContent());
            PageableResponseEntity<Object> pageableResponseEntity = new PageableResponseEntity<Object>(
                    StatusCode.SUCCESS,
                    "Paginated response",
                    userMinimalDetailsDTOS,
                    userPage.getTotalElements(),
                    userPage.getSize(),
                    userPage.getTotalPages(),
                    0
            );

            return GenericResponseEntity.create(StatusCode.SUCCESS, pageableResponseEntity, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/search-tasks")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to search tasks by pagination.")
    public ResponseEntity<?> searchTasks(@RequestParam("userPublicId") String userPublicId,
                                         @RequestParam(value = "pageNo") String pageNo,
                                         @RequestParam(value = "pageSize") String pageSize,
                                         @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder,
                                         @RequestParam(value = "sortBy") String sortBy,
                                         @RequestParam(value = "medium") String medium,
                                         @RequestParam(value = "categoryPublicIds", required = false) List<String> categoryPublicIds,
                                         @RequestParam(value = "lowBudget", required = false) Long lowBudget,
                                         @RequestParam(value = "highBudget", required = false) Long highBudget,
                                         @RequestParam(value = "isRemoteTask", required = false) Boolean remoteTask,
                                         @RequestParam(value = "longitude", required = false) Float longitude,
                                         @RequestParam(value = "latitude", required = false) Float latitude,
                                         @RequestParam(value = "radius", required = false) Long radius) throws Exception {
        try {
            User user = new User();
            user.setPublicId(Long.valueOf(0000000000000));
            if(!Mediums.valueOf(medium).equals(Mediums.WEBSITE)){
                user = isCurrentUser(userPublicId);
            }

            if(Integer.valueOf(pageNo) < 0 || Integer.valueOf(pageSize) < 0){
                return GenericResponseEntity.create(StatusCode.SUCCESS, "PageNo and PageSize must be positive numbers", HttpStatus.OK);
            }

            Sort.Direction sortDirection = Constants.Sort.DESCENDING.value().equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = (Pageable) PageRequest.of(Integer.valueOf(pageNo), Integer.valueOf(pageSize), Sort.by(sortDirection, sortBy));

            Page<Task> pages = taskingService.searchTasks(categoryPublicIds, longitude, latitude, lowBudget, highBudget, remoteTask, radius, user, pageable);

            List<TaskDTO> taskDTOS = TaskMapper.createTaskDTOListLazy(pages.getContent(), user);

            PageableResponseEntity<Object> pageableResponseEntity = new PageableResponseEntity<Object>(
                    StatusCode.SUCCESS,
                    "Paginated response",
                    taskDTOS,
                    pages.getTotalElements(),
                    pages.getSize(),
                    pages.getTotalPages(),
                    0
            );

            return GenericResponseEntity.create(StatusCode.SUCCESS, pageableResponseEntity, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{taskPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch specific task by task public id.")
    public ResponseEntity<?> getByTaskPublicId(@PathVariable String taskPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            Task task = taskingService.getByTaskPublicId(taskPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, TaskMapper.createTaskDTOLazy(task, user), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{taskPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete task.")
    public ResponseEntity<?> deleteTask(@PathVariable String taskPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            Task task = taskingService.deleteTask(taskPublicId, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, TaskMapper.createTaskDTOLazy(task, user), HttpStatus.OK);

        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{taskPublicId}/user/{userPublicId}/status/{status}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit task status.")
    public ResponseEntity<?> editTaskStatus(@PathVariable String taskPublicId,
                                            @PathVariable String userPublicId,
                                            @PathVariable String status) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            Task task = taskingService.editTaskStatus(taskPublicId, user, status);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("Task " + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{taskPublicId}/user/{userPublicId}/tasker-bookings")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used for rescheduling  tasker booked time.")
    public ResponseEntity<?> getTaskerBookedTimesForRescheduling(@PathVariable String taskPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, taskingService.getTaskerBookedTimesForRescheduling(taskPublicId, user), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{taskPublicId}/user/{userPublicId}/reschedule-response")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to accept/reject reschedule task request.")
    public ResponseEntity<?> acceptOrRejectRescheduleTaskRequest(
            @PathVariable String taskPublicId,
            @PathVariable String userPublicId,
            @RequestParam(value="response", required = false, defaultValue = "false") boolean response) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            if(taskingService.rescheduleTaskResponse(taskPublicId, user, response)){
                return GenericResponseEntity.create(StatusMessageDTO.builder()
                        .message("Task " + AuthConstants.RESCHEDULED_ACCEPTED_SUCCESSFULLY)
                        .status(0)
                        .build(), HttpStatus.CREATED);
            } else {
                return GenericResponseEntity.create(StatusMessageDTO.builder()
                        .message("Task " + AuthConstants.RESCHEDULED_REJECTED_SUCCESSFULLY)
                        .status(0)
                        .build(), HttpStatus.CREATED);
            }

        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{taskPublicId}/user/{userPublicId}/reschedule-request}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to reschedule task request.")
    public ResponseEntity<?> rescheduleTaskRequest(@PathVariable String taskPublicId,
                                                   @PathVariable String userPublicId,
                                                   @RequestParam(value="rescheduleDateTime") String rescheduleDateTime) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            Task task = taskingService.rescheduleTaskRequest(taskPublicId, user, rescheduleDateTime);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("Task " + AuthConstants.RESCHEDULED_REQUESTED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{taskPublicId}/timeline")
    @ApiOperation(value = "This operation is used for fetching task timeline.")
    public ResponseEntity<?> getTaskTimeline(@PathVariable String taskPublicId) throws Exception {
        try {
            TaskDTO taskDTO = taskingService.getTaskTimeline(taskPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, taskDTO, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    public User isCurrentUser(String userPublicId) throws ResourceNotFoundException, UnauthorizedException {
        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(userPublicId));
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, userPublicId);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userPrincipal.getEmail().equals(user.getEmail())) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);

        return user;
    }
}
