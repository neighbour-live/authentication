package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.*;
import com.bot.middleware.persistence.dto.EmailNotificationDto;
import com.bot.middleware.persistence.dto.TaskDTO;
import com.bot.middleware.persistence.dto.TaskScheduleDTO;
import com.bot.middleware.persistence.mapper.TaskMapper;
import com.bot.middleware.persistence.repository.TaskCategoriesRepository;
import com.bot.middleware.persistence.repository.TaskRepository;
import com.bot.middleware.persistence.repository.UserBidRepository;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddTask;
import com.bot.middleware.persistence.request.UserNotificationRequest;
import com.bot.middleware.persistence.type.NotificationAction;
import com.bot.middleware.persistence.type.NotificationEnum;
import com.bot.middleware.persistence.type.TaskStatus;
import com.bot.middleware.resources.services.*;
import com.bot.middleware.utility.Constants;
import com.bot.middleware.utility.ObjectUtils;
import com.bot.middleware.utility.id.PublicIdGenerator;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TaskingServiceImpl implements TaskingService {
    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserBidRepository userBidRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskCategoriesRepository taskCategoriesRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ConversationService conversationService;

    @Autowired
    private TaskTimelineService taskTimelineService;

    @Autowired
    private EmailService emailService;

    @Value("${bot.logoURL}")
    private String BOT_LOGO_URL;

    @Value("${bot.minimum-task-amount}")
    private int MINIMUM_TASK_AMOUNT;

    @Value("${bot.service-fees-factor}")
    private Double SERVICE_FEE;

    @Override
    @Transactional
    public Task addTask(AddTask addTask, User user) throws ResourceNotFoundException, UnauthorizedException, StripeException {
        Task task = new Task();
        UserAddress userAddress = null;

        if(!ObjectUtils.isNull(addTask.getUserAddressPublicId()) && Boolean.FALSE.equals(addTask.getIsRemoteTask())){
            userAddress = userAddressService.getByUserAddressId(addTask.getUserAddressPublicId());
        }

        if(addTask.getBudget() < MINIMUM_TASK_AMOUNT ) {
            System.out.println("\nMinimum task amount would not be less than $30.\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Minimum task amount would not be less than $30.");
        }

        Account account = stripeService.getConnectAccount(user.getConnectId());
        if(!account.getPayoutsEnabled() || !account.getChargesEnabled()){
            System.out.println("\nPlease add relevant banking/card details to proceed\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Payments are not enabled, Please add relevant banking/card details to proceed.");
        }

        TaskCategories taskCategory = taskCategoriesRepository.findByPublicId(PublicIdGenerator.decodePublicId(addTask.getCategoryPublicId()));
        if(taskCategory == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.TASK_CATEGORY_NOT_FOUND_WITH_PUBLIC_ID, addTask.getCategoryPublicId());

        task.setPublicId(PublicIdGenerator.generatePublicId());
        task.setBudget(BigInteger.valueOf(addTask.getBudget()));
        task.setTitle(addTask.getTitle());
        task.setDescription(addTask.getDescription());
        task.setHourlyRate(addTask.getHourlyRate());
        task.setMilestoneRate(addTask.getMilestoneRate());
        task.setPaymentType(addTask.getPaymentType());
        task.setTaskRepeat(addTask.getTaskRepeat());
        task.setTaskTime(addTask.getTaskTime());
        task.setTaskMedia(addTask.getMediaFiles());
        task.setStartDateTime(ZonedDateTime.parse(addTask.getStartDateTime()));
        task.setEndDateTime(task.getStartDateTime().plusMinutes((long) addTask.getTaskTime()*60));
        task.setTaskCategory(taskCategory);

        task.setUserAddress(userAddress);
        task.setPoster(user);
        task.setTasker(null);
        task.setBlockedBy(null);
//        task.setUserTransactions(null);

        task.setIsApproved(true);
        task.setIsPending(false);
        task.setIsAssigned(false);
        task.setIsCompleted(false);
        task.setIsDeleted(false);
        task.setIsRescheduled(false);
        task.setIsRescheduleRequested(false);
        task.setRescheduleTaskTime(null);
        task.setTaskStatus(TaskStatus.APPROVED.toString());
        task.setRemoteTask(Boolean.TRUE.equals(addTask.getIsRemoteTask()));
        task = taskRepository.save(task);
        taskTimelineService.addTaskTimeline(task.getTaskStatus(), task, null, task.getPoster());
        return task;
    }

    @Override
    public Task editTask(User user, AddTask addTask, String taskPublicId) throws Exception {
        Task task = getByTaskPublicId(taskPublicId);
        UserAddress userAddress = userAddressService.getByUserAddressId(addTask.getUserAddressPublicId());

        if(addTask.getBudget() < MINIMUM_TASK_AMOUNT ) {
            System.out.println("\nMinimum task amount would not be less than $30.\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Minimum task amount would not be less than $30.");
        }

        Account account = stripeService.getConnectAccount(user.getConnectId());
        if(!account.getPayoutsEnabled() || !account.getChargesEnabled()){
            System.out.println("\nPlease add relevant banking/card details to proceed\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Payments are not enabled, Please add relevant banking/card details to proceed.");
        }

        if(!task.getPoster().getPublicId().equals(user.getPublicId())){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        }

        if(task.getIsDeleted()){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//            throw new Exception("Cannot edit an already deleted task.");
        }

        if(task.getIsCompleted()){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//            throw new Exception("Cannot edit a completed task.");
        }

        if(task.getIsPending()){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//            throw new Exception("Cannot edit a pending task.");
        }

        if(task.getIsAssigned()){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//            throw new Exception("Cannot edit an assigned task.");
        }

        task.setBudget(BigInteger.valueOf(addTask.getBudget()));
        task.setTitle(addTask.getTitle());
        task.setDescription(addTask.getDescription());
        task.setHourlyRate(addTask.getHourlyRate());
        task.setMilestoneRate(addTask.getMilestoneRate());
        task.setPaymentType(addTask.getPaymentType());
        task.setTaskRepeat(addTask.getTaskRepeat());
        task.setTaskTime(addTask.getTaskTime());
        task.setTaskMedia(addTask.getMediaFiles());
        task.setStartDateTime(ZonedDateTime.parse(addTask.getStartDateTime()));
        task.setEndDateTime(task.getStartDateTime().plusMinutes((long) addTask.getTaskTime()*60));

        task.setUserAddress(userAddress);
        task.setPoster(user);
        task.setTasker(null);
        task.setBlockedBy(null);
//        task.setUserTransactions(null);

        task.setIsApproved(true);
        task.setIsPending(false);
        task.setIsAssigned(false);
        task.setIsCompleted(false);
        task.setIsDeleted(false);
        task.setTaskStatus(TaskStatus.APPROVED.toString());
        task.setRemoteTask(Boolean.TRUE.equals(addTask.getIsRemoteTask()));

        Collection<UserBid> userBids = task.getUserBids();
        userBids.forEach(
                userBid -> {
                    //NOTIFICATION
                    Map<String,String> actionsInfo =  new HashMap<>();
                    actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

                    UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                            .notificationType(NotificationEnum.EDITED_TASK.toString())
                            .target("GROUP")
                            .title("Task Edited")
                            .body("Task from "+user.getFirstName()+" "+user.getLastName()+" with title: "+userBid.getTask().getTitle()+" has been edited!")
                            .imageUrl(BOT_LOGO_URL)
                            .actions(NotificationAction.EDITED_TASK_PAGE.name())
                            .actionsInfo(actionsInfo)
                            .firebaseKey(userBid.getUser().getFirebaseKey())
                            .build();

                    try {
                        UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, userBid.getUser());
                    } catch (ResourceNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );

        return taskRepository.save(task);
    }

    @Override
    public Collection<Task> getAllTasks(User poster, String status) {
        if(status == null) {
            Collection<Task> tasks = taskRepository.findAllByPoster(poster);
            return tasks;
        } else {
            String[] values = status.trim().split(",");
            List<String> statuses  =  Arrays.asList(values);
            Collection<Task> tasks = taskRepository.findAllByPosterAndTaskStatus(poster, statuses);
            return tasks;
        }
    }

    @Override
    public Task getByTaskPublicId(String taskPublicId) throws ResourceNotFoundException {
        Task task = taskRepository.findTaskByTaskPublicId(PublicIdGenerator.decodePublicId(taskPublicId));
        if(task == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.TASK_NOT_FOUND_WITH_PUBLIC_ID, taskPublicId);
        return task;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Task deleteTask(String taskPublicId, User poster) throws Exception {
        Task task =  taskRepository.findUndeletedTaskByTaskPublicId(PublicIdGenerator.decodePublicId(taskPublicId), poster);
        if(task == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.TASK_NOT_FOUND_WITH_PUBLIC_ID, taskPublicId);

        Account account = stripeService.getConnectAccount(poster.getConnectId());
        if(!account.getPayoutsEnabled() || !account.getChargesEnabled()){
            System.out.println("\nPlease add relevant banking/card details to proceed\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Payments are not enabled, Please add relevant banking/card details to proceed.");
        }

        if(task.getIsDeleted()){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//            throw new Exception("Cannot delete an already deleted task.");
        }

        if(task.getIsCompleted()){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//            throw new Exception("Cannot delete a completed task.");
        }

        if(task.getIsPending()){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//            throw new Exception("Cannot delete a pending task.");
        }

        if(task.getIsAssigned()){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//            throw new Exception("Cannot delete an assigned task.");
        }

        task.setTaskStatus(TaskStatus.DELETED.toString());
        task.setIsAssigned(false);
        task.setIsDeleted(true);
        task.setIsPending(false);
        task.setIsCompleted(false);
        task.setTasker(null);
        task.setTaskRepeat(null);

        Collection<UserBid> userBids = task.getUserBids();
        userBids.forEach(
                userBid -> {
                    //NOTIFICATION
                    Map<String,String> actionsInfo =  new HashMap<>();
                    actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

                    UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                            .notificationType(NotificationEnum.EDITED_TASK.toString())
                            .target("GROUP")
                            .title("Task Edited")
                            .body("Task from "+poster.getFirstName()+" "+poster.getLastName()+" with title: "+userBid.getTask().getTitle()+" has been deleted!")
                            .imageUrl(BOT_LOGO_URL)
                            .actions(NotificationAction.DELETED_BID_PAGE.name())
                            .actionsInfo(actionsInfo)
                            .firebaseKey(userBid.getUser().getFirebaseKey())
                            .build();

                    try {
                        UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, userBid.getUser());
                    } catch (ResourceNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );

        return taskRepository.save(task);
    }

    @Override
    public Page<User> getFeaturedTaskers(String city, Pageable pageable) {
        Page<User> users = userService.getFeaturedTaskers(city, pageable);
        return users;
    }

    @Override
    public List<UserBid> findAllTaskBids(String taskPublicId) throws ResourceNotFoundException {
        Task task = getByTaskPublicId(taskPublicId);
        List<UserBid> userBids =  userBidRepository.findAllTaskBids(PublicIdGenerator.decodePublicId(taskPublicId));
        return userBids;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Task editTaskStatus(String taskPublicId, User user, String status) throws ResourceNotFoundException, UnauthorizedException, StripeException {

        Task task = getByTaskPublicId(taskPublicId);
        if(!task.getTaskStatus().equals(TaskStatus.COMPLETE) && task.getPoster().equals(user)) {

            if(status.equalsIgnoreCase(TaskStatus.PENDING.toString()) || status.equalsIgnoreCase(TaskStatus.IN_PROGRESS.toString())){

                task.setTaskStatus(status.toUpperCase());
                if(status.equalsIgnoreCase(TaskStatus.IN_PROGRESS.toString())){
                    stripeService.chargeMoneyFromTaskPoster(task);
//                    UserTransactions userTransaction = stripeService.chargeMoneyFromTaskPoster(task);
//                    task.setUserTransactions(userTransaction);

                    //Task Funded notification
                    Map<String,String> actionsInfo =  new HashMap<>();
                    actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

                    UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                            .notificationType(NotificationEnum.FUNDED_TASK.toString())
                            .target("INDIVIDUAL")
                            .title("Task Funded")
                            .body("Task from "+user.getFirstName()+" "+user.getLastName()+" with title: "+task.getTitle()+" has been Funded!")
                            .imageUrl(BOT_LOGO_URL)
                            .actions(NotificationAction.FUNDED_TASK_PAGE.name())
                            .actionsInfo(actionsInfo)
                            .firebaseKey(task.getTasker().getFirebaseKey())
                            .build();

                    try {
                        UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, task.getTasker());
                    } catch (ResourceNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                //In Progress Notification
                Map<String,String> actionsInfo =  new HashMap<>();
                actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

                UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                        .notificationType(NotificationEnum.IN_PROGRESS_TASK.toString())
                        .target("INDIVIDUAL")
                        .title("Task In-Progress")
                        .body("Task from "+user.getFirstName()+" "+user.getLastName()+" with title: "+task.getTitle()+" has been started!")
                        .imageUrl(BOT_LOGO_URL)
                        .actions(NotificationAction.IN_PROGRESS_TASK_PAGE.name())
                        .actionsInfo(actionsInfo)
                        .firebaseKey(task.getTasker().getFirebaseKey())
                        .build();

                try {
                    UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, task.getTasker());
                } catch (ResourceNotFoundException e) {
                    e.printStackTrace();
                }

                task = taskRepository.save(task);

                taskTimelineService.addTaskTimeline(status.toUpperCase(), task, null, user);

            } else{
                throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
            }

        } else if(!task.getTaskStatus().equals(TaskStatus.COMPLETE) && task.getTasker().equals(user)){

            if(status.equalsIgnoreCase(TaskStatus.PENDING.toString()) || status.equalsIgnoreCase(TaskStatus.COMPLETE.toString())){

                task.setTaskStatus(status.toUpperCase());
                if(status.equalsIgnoreCase(TaskStatus.COMPLETE.toString())){
                    UserTransactions userTransaction = stripeService.transferMoneyFromPlatformToTasker(task);
                    //Money Transferred for clearance
                    Map<String,String> actionsInfo =  new HashMap<>();
                    actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

                    User poster = task.getPoster();
                    poster.setTasksCompletedAsPoster(poster.getTasksCompletedAsPoster() + 1);
                    userRepository.save(poster);

                    User tasker = task.getTasker();
                    tasker.setTasksCompletedAsTasker(tasker.getTasksCompletedAsTasker() + 1);
                    userRepository.save(tasker);

                    UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                            .notificationType(NotificationEnum.PAID_TASK.toString())
                            .target("INDIVIDUAL")
                            .title("Task paid")
                            .body("Task with title: "+task.getTitle()+" has been paid, your amount is under clearance process.")
                            .imageUrl(BOT_LOGO_URL)
                            .actions(NotificationAction.PAID_TASK_PAGE.name())
                            .actionsInfo(actionsInfo)
                            .firebaseKey(task.getTasker().getFirebaseKey())
                            .build();

                    Conversation conversation = conversationService.deleteConversationByTask(task);

                    // Email Notification Task Completed
                    Map<String, String> placeHolders = new HashMap<String, String>();
                    placeHolders.put("tasker_name", tasker.getFirstName());
                    placeHolders.put("category_name", task.getTaskCategory().getName());

                    EmailNotificationDto emailNotificationDto = EmailNotificationDto.builder()
                            .to(poster.getEmail())
                            .template(Constants.EmailTemplate.TASK_COMPLETED_TEMPLATE.value())
                            .placeHolders(placeHolders).build();

                    // Email Notification Funds Received
                    placeHolders = new HashMap<String, String>();
                    placeHolders.put("poster_name", poster.getFirstName());
                    placeHolders.put("category_name", task.getTaskCategory().getName());

                    emailNotificationDto = EmailNotificationDto.builder()
                            .to(tasker.getEmail())
                            .template(Constants.EmailTemplate.FUNDS_RECEIVED_TEMPLATE.value())
                            .placeHolders(placeHolders).build();

                    // Email Notification Funds Issued
                    placeHolders = new HashMap<String, String>();
                    placeHolders.put("tasker_name", tasker.getFirstName());
                    placeHolders.put("category_name", task.getTaskCategory().getName());

                    emailNotificationDto = EmailNotificationDto.builder()
                            .to(poster.getEmail())
                            .template(Constants.EmailTemplate.FUNDS_ISSUED_TEMPLATE.value())
                            .placeHolders(placeHolders).build();

                    try {
                        UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, task.getTasker());
                        emailService.sendEmailFromExternalApi(emailNotificationDto);
                    } catch (ResourceNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }


                //In Progress Notification
                Map<String,String> actionsInfo =  new HashMap<>();
                actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

                UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                        .notificationType(NotificationEnum.COMPLETED_TASK.toString())
                        .target("INDIVIDUAL")
                        .title("Task Completed.")
                        .body("Task with title: "+task.getTitle()+" has been completed!")
                        .imageUrl(BOT_LOGO_URL)
                        .actions(NotificationAction.COMPLETED_TASK_PAGE.name())
                        .actionsInfo(actionsInfo)
                        .firebaseKey(task.getPoster().getFirebaseKey())
                        .build();

                try {
                    UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, task.getPoster());
                } catch (ResourceNotFoundException e) {
                    e.printStackTrace();
                }

                task = taskRepository.save(task);

                taskTimelineService.addTaskTimeline(status.toUpperCase(), task, user, null);

            } else{
                throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
            }

        } else {
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        }

        return task;
    }

    @Override
    public Page<Task> searchTasks(List<String> publicIds, Float longitude, Float latitude, Long lowBudget, Long highBudget, Boolean remoteTask, Long radius,  User user, Pageable page) {
        List<Long> categoryPublicIds = null;
        Float lng = null, lat = null;
        if(!ObjectUtils.isNull(publicIds)){
            List<Long> tempList = new ArrayList<>();
            publicIds.forEach(publicId -> tempList.add(PublicIdGenerator.decodePublicId(publicId)));
            categoryPublicIds = tempList;
        }
        if(ObjectUtils.isNull(longitude) && ObjectUtils.isNull(latitude)){
            List<UserAddress> userAddresses = (List<UserAddress>) user.getUserAddresses();
            if(!ObjectUtils.isNull(userAddresses) && !userAddresses.isEmpty()){
                lng = userAddresses.get(0).getLng();
                lat = userAddresses.get(0).getLat();
            }
        } else{
            lng = longitude;
            lat = latitude;
        }

        Page<Task> tasks = taskRepository.searchTasks(categoryPublicIds,
                ObjectUtils.isNull(lowBudget) ? null : BigInteger.valueOf(lowBudget),
                ObjectUtils.isNull(highBudget) ? null : BigInteger.valueOf(highBudget),
                remoteTask,
                ObjectUtils.isNull(lng) ? 0.0f : lng,
                ObjectUtils.isNull(lat) ? 0.0f : lat,
                ObjectUtils.isNull(radius) ? 5L : radius,
                !ObjectUtils.isNull(remoteTask) && remoteTask,
                user,
                page);
        return tasks;
    }

    @Override
    public Collection<Task> getAllBidderTasks(User user) {
        Collection<Task> tasks = taskRepository.getAllBidderTasks(user);
        return tasks;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Task rescheduleTaskRequest(String taskPublicId, User user, String rescheduleDateTime) throws Exception {
        Task task = getByTaskPublicId(taskPublicId);
        if(task.getPoster().equals(user) && task.getTaskStatus().equals(TaskStatus.PENDING.name())){

            task.setRescheduleTaskTime(ZonedDateTime.parse(rescheduleDateTime));
            task.setIsRescheduled(false);
            task.setIsRescheduleRequested(true);


            Map<String,String> actionsInfo =  new HashMap<>();
            actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

            UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                    .notificationType(NotificationEnum.RESCHEDULE_TASK_REQUEST.toString())
                    .target("INDIVIDUAL")
                    .title("Task reschedule request")
                    .body(task.getPoster().getFirstName() + " " + task.getPoster().getLastName() + " has requested to re-schedule the task: "+ task.getTitle())
                    .imageUrl(BOT_LOGO_URL)
                    .actions(NotificationAction.TASK_RESCHEDULE_REQUEST_PAGE.name())
                    .actionsInfo(actionsInfo)
                    .firebaseKey(task.getTasker().getFirebaseKey())
                    .build();

            try {
                UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, task.getTasker());
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
            }

            taskRepository.save(task);
            return task;

        } else {
            throw new Exception("Only pending tasks can be rescheduled by task owner.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean rescheduleTaskResponse(String taskPublicId, User user, boolean response) throws Exception {
        Task task = getByTaskPublicId(taskPublicId);
        if(task.getTasker().equals(user) && task.getTaskStatus().equals(TaskStatus.PENDING.name())){

            Map<String,String> actionsInfo =  new HashMap<>();
            actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

            if(response){

                task.setIsRescheduled(true);
                task.setIsRescheduleRequested(true);
                taskRepository.save(task);

                UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                        .notificationType(NotificationEnum.RESCHEDULE_TASK_ACCEPTED.toString())
                        .target("INDIVIDUAL")
                        .title("Task Rescheduled")
                        .body(task.getTitle()+ " has been rescheduled.")
                        .imageUrl(BOT_LOGO_URL)
                        .actions(NotificationAction.TASK_RESCHEDULE_ACCEPTED_PAGE.name())
                        .actionsInfo(actionsInfo)
                        .firebaseKey(task.getTasker().getFirebaseKey())
                        .build();

                try {
                    UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, task.getPoster());
                } catch (ResourceNotFoundException e) {
                    e.printStackTrace();
                }

                return true;

            } else {

                task.setIsRescheduled(false);
                task.setIsRescheduleRequested(true);
                taskRepository.save(task);

                UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                        .notificationType(NotificationEnum.RESCHEDULE_TASK_REJECTED.toString())
                        .target("INDIVIDUAL")
                        .title("Task Reschedule Cancelled")
                        .body(task.getTitle()+ " reschedule request has been canceled.")
                        .imageUrl(BOT_LOGO_URL)
                        .actions(NotificationAction.TASK_RESCHEDULE_REJECTED_PAGE.name())
                        .actionsInfo(actionsInfo)
                        .firebaseKey(task.getTasker().getFirebaseKey())
                        .build();

                try {
                    UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, task.getPoster());
                } catch (ResourceNotFoundException e) {
                    e.printStackTrace();
                }

                return false;

            }

        } else {
            throw new Exception("Only tasker can accept/reject task reschedule request.");
        }
    }

    @Override
    public Integer getTasksCompletedByUserAsPoster(User user) {
        return taskRepository.findCountOfTasksCompletedByUserAsTasker(user);
    }

    @Override
    public Integer getTasksCompletedByUserAsTasker(User user) {
        return taskRepository.findCountOfTasksCompletedByUserAsPoster(user);
    }

    @Override
    public List<TaskScheduleDTO> getTaskerBookedTimesForRescheduling(String taskPublicId, User user) throws Exception {
        Task task = getByTaskPublicId(taskPublicId);
        if(task.getPoster().equals(user) && task.getTaskStatus().equals(TaskStatus.PENDING.name())){

            ZonedDateTime startDateTime = task.getStartDateTime().truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime dayStart = startDateTime.toLocalDate().atStartOfDay(startDateTime.getZone());
            ZonedDateTime dayEnd = dayStart.plusDays(1).minusSeconds(1);

            List<UserBid> bidderBookingsForDay =  userBidRepository.checkBidderBookingsForDay(task.getTasker().getPublicId(), dayStart, dayEnd);
            List<Task> bidderTasksForDay = new ArrayList<>();

            bidderBookingsForDay.forEach(userBid -> {
                bidderTasksForDay.add(userBid.getTask());
            });

            return TaskMapper.createTaskScheduleDTOListLazy(bidderTasksForDay, task);

        } else {
            throw new Exception("Only pending tasks can be rescheduled by task owner.");
        }
    }

    @Override
    public TaskDTO getTaskTimeline(String taskPublicId) throws Exception {
        Task task = getByTaskPublicId(taskPublicId);
        Double serviceFee = task.getBudget().doubleValue() * SERVICE_FEE;
        return TaskMapper.createTaskTimelineDTOLazy(task, serviceFee);
    }
}
