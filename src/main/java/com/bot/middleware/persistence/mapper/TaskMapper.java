package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.*;
import com.bot.middleware.persistence.dto.TaskDTO;
import com.bot.middleware.persistence.dto.TaskScheduleDTO;
import com.bot.middleware.persistence.type.BidStatus;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskMapper {

    public static TaskDTO createTaskDTOLazy(Task task, User user) {
        List<UserBid> userBids = (List<UserBid>) task.getUserBids();
        TaskDTO taskDTO = TaskDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(task.getPublicId()))
                .description(task.getDescription())
                .title(task.getTitle())
                .isAssigned(task.getIsAssigned())
                .isCompleted(task.getIsCompleted())
                .isPending(task.getIsPending())
                .poster( (task.getPoster() == null) ? null : UserMapper.createUserMinimalDetailsDTOLazy(task.getPoster()))
                .tasker( (task.getTasker() == null) ? null : UserMapper.createUserMinimalDetailsDTOLazy(task.getTasker()))
                .taskRepeat(task.getTaskRepeat())
                .paymentType(task.getPaymentType())
                .taskTime(task.getTaskTime())
                .hourlyRate(task.getHourlyRate())
                .milestoneRate(task.getMilestoneRate())
                .budget(task.getBudget())
                .mediaFiles(task.getTaskMedia())
                .category(TaskCategoriesMapper.createTaskCategoriesDTOLazy(task.getTaskCategory()))
                .totalBids( (userBids == null) ? 0 : userBids.size())
                .userAlreadyPlacedBid((userBids == null || userBids.size() <= 0) ? false: checkIfCurrentUserAlreadyPlacedBid(userBids, user.getPublicId()))
                .feedbackAlreadyGiven((task.getReview() == null) ? false: checkIfCurrentUserAlreadyReviewedTask(task.getReview(), user.getPublicId()))
                .userAddress( (task.getUserAddress() == null) ? null : UserResidentialAddressMapper.createUserResidentialAddressDTOLazy(task.getUserAddress()))
                .userTransactions( (task.getTaskTransactions() == null) ? null : UserTransactionMapper.createUserTransactionDTOListLazy(task.getTaskTransactions()))
                .startDateTime(task.getStartDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .endDateTime(task.getEndDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .createDateTime(task.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .updatedDateTime(task.getUpdateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(task.getTaskStatus())
                .isRemoteTask(task.getRemoteTask())
                .build();

        return taskDTO;
    }

    public static List<TaskDTO> createTaskDTOListLazy(Collection<Task> tasks, User user) {
        List<TaskDTO> taskDTOS = new ArrayList<>();
        if(tasks == null ) return taskDTOS;

        tasks.forEach(task -> taskDTOS.add(createTaskDTOLazy(task, user)));
        return taskDTOS;
    }

    public static boolean checkIfCurrentUserAlreadyPlacedBid(List<UserBid> userBids, Long userPublicId){

        for (int index = 0; index < userBids.size(); index++){
            if(userBids.get(index).getUser().getPublicId().equals(userPublicId)){
                if(userBids.get(index).getStatus().equals(BidStatus.REJECTED.toString())) continue;// check is false for rejected bids
                return true;
            }
        }

        return false;
    }


    private static boolean checkIfCurrentUserAlreadyReviewedTask(Review review, Long publicId) {
        if(review.getTasker().getPublicId().equals(publicId) || review.getPoster().getPublicId().equals(publicId)){
            return true;
        }
        return false;
    }

    public static TaskScheduleDTO createTaskScheduleDTOLazy(Task task, Task originalTask) {
        TaskScheduleDTO taskScheduleDTO = TaskScheduleDTO.builder()
                .startDateTime(task.getStartDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .endDateTime(task.getEndDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .taskTime(task.getTaskTime())
                .isCurrent((task.getPublicId().equals(originalTask.getPublicId())) ? true : false)
                .build();

        return taskScheduleDTO;
    }

    public static List<TaskScheduleDTO> createTaskScheduleDTOListLazy(Collection<Task> tasks, Task originalTask) {
        List<TaskScheduleDTO> taskScheduleDTOS = new ArrayList<>();
        if(tasks == null ) return taskScheduleDTOS;

        tasks.forEach(task -> taskScheduleDTOS.add(createTaskScheduleDTOLazy(task, originalTask)));
        return taskScheduleDTOS;
    }

    public static TaskDTO createTaskTimelineDTOLazy(Task task, Double additionalCharges) {
        TaskDTO taskDTO = TaskDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(task.getPublicId()))
                .description(task.getDescription())
                .title(task.getTitle())
                .isAssigned(task.getIsAssigned())
                .isCompleted(task.getIsCompleted())
                .isPending(task.getIsPending())
                .poster( (task.getPoster() == null) ? null : UserMapper.createUserMinimalDetailsDTOLazy(task.getPoster()))
                .tasker( (task.getTasker() == null) ? null : UserMapper.createUserMinimalDetailsDTOLazy(task.getTasker()))
                .taskRepeat(task.getTaskRepeat())
                .paymentType(task.getPaymentType())
                .taskTime(task.getTaskTime())
                .hourlyRate(task.getHourlyRate())
                .milestoneRate(task.getMilestoneRate())
                .budget(task.getBudget())
                .mediaFiles(task.getTaskMedia())
                .category(TaskCategoriesMapper.createTaskCategoriesDTOLazy(task.getTaskCategory()))
                .userAddress( (task.getUserAddress() == null) ? null : UserResidentialAddressMapper.createUserResidentialAddressDTOLazy(task.getUserAddress()))
                .userTransactions( (task.getTaskTransactions() == null) ? null : UserTransactionMapper.createUserTransactionDTOListLazy(task.getTaskTransactions()))
                .startDateTime(task.getStartDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .endDateTime(task.getEndDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .createDateTime(task.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .updatedDateTime(task.getUpdateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(task.getTaskStatus())
                .isRemoteTask(task.getRemoteTask())
                .taskTimeline(TaskTimelineMapper.createTaskTimelineDTOListLazy((List<TaskTimeline>) task.getTaskTimelines()))
                .additionalCharges(additionalCharges)
                .build();

        return taskDTO;
    }
}
