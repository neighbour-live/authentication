package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.*;
import com.bot.middleware.persistence.dto.EmailNotificationDto;
import com.bot.middleware.persistence.repository.TaskRepository;
import com.bot.middleware.persistence.repository.UserBankAccountRepository;
import com.bot.middleware.persistence.repository.UserBidRepository;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddConversationRequest;
import com.bot.middleware.persistence.request.AddUserBidRequest;
import com.bot.middleware.persistence.request.UserNotificationRequest;
import com.bot.middleware.persistence.type.BidStatus;
import com.bot.middleware.persistence.type.NotificationAction;
import com.bot.middleware.persistence.type.NotificationEnum;
import com.bot.middleware.persistence.type.TaskStatus;
import com.bot.middleware.resources.services.*;
import com.bot.middleware.utility.Constants;
import com.bot.middleware.utility.id.PublicIdGenerator;
import com.stripe.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
public class BiddingServiceImpl implements BiddingService {

    @Autowired
    UserBidRepository userBidRepository;

    @Autowired
    TaskingService taskingService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    StripeService stripeService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ConversationService conversationService;

    @Autowired
    UserBankAccountRepository userBankAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskTimelineService taskTimelineService;

    @Value("${bot.logoURL}")
    private String BOT_LOGO_URL;

    @Override
    @Transactional
    public UserBid addBid(AddUserBidRequest addUserBidRequest, User user) throws Exception {
        Task task = taskingService.getByTaskPublicId(addUserBidRequest.getTaskPublicId());

        User bidder = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(addUserBidRequest.getUserPublicId()));
        if(bidder == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, addUserBidRequest.getUserPublicId());

        Collection<UserBankAccount> userBankAccounts = userBankAccountRepository.findAllByUser(bidder);
        ArrayList<UserBankAccount> userBankAccountArrayList = (ArrayList<UserBankAccount>) userBankAccounts;

        Account account = stripeService.getConnectAccount(bidder.getConnectId());
        if(!account.getPayoutsEnabled() || userBankAccountArrayList.isEmpty()){
            System.out.println("\nPlease add relevant banking details to proceed\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Payouts are not enabled, Please add relevant banking details to proceed.");
        }

        if(task.getPoster().getPublicId().equals(bidder.getPublicId())){
            System.out.println("\nPoster and Tasker cannot be same users\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Poster and Tasker cannot be same users");
        }

        int bidAlreadyExist = userBidRepository.checkBidAlreadyExist(bidder.getPublicId(), PublicIdGenerator.decodePublicId(addUserBidRequest.getTaskPublicId()));
        if(bidAlreadyExist > 0){
            if(task.getTaskStatus().equals(TaskStatus.IN_PROGRESS.name()) || task.getTaskStatus().equals(TaskStatus.PENDING.name()) || task.getTaskStatus().equals(TaskStatus.COMPLETE.name()) || task.getTaskStatus().equals(TaskStatus.DELETED.name())){
                throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
            }
        }

        UserBid bid = new UserBid();
        bid.setIsDeleted(false);
        bid.setIsActive(true);

        bid.setBudget(addUserBidRequest.getBudget());
        bid.setOtherCosts((long) addUserBidRequest.getOtherCosts());
        bid.setHourlyRate((int) addUserBidRequest.getHourlyRate());
        bid.setOtherCostsExplanation(addUserBidRequest.getOtherCostsExplanation());
        bid.setTimeUtilizationExplanation(addUserBidRequest.getTimeUtilizationExplanation());
        bid.setHours((int) addUserBidRequest.getHours());
        bid.setImageUrl(addUserBidRequest.getImageUrl());
        bid.setPublicId(PublicIdGenerator.generatePublicId());

        //NOTIFICATION
        UserNotificationRequest userNotificationRequest = new UserNotificationRequest();
        bid.setStatus(BidStatus.INITIATED.toString());
        userNotificationRequest.setNotificationType(NotificationEnum.ADDED_BID.toString());
        userNotificationRequest.setImageUrl(BOT_LOGO_URL);

        userNotificationRequest.setActions(NotificationAction.ADDED_BID_PAGE.name());

        Map<String,String> actionsInfo =  new HashMap<>();
        actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));
        actionsInfo.put("bidPublicId", PublicIdGenerator.encodedPublicId(bid.getPublicId()));
        userNotificationRequest.setActionsInfo(actionsInfo);


        if(user.getPublicId().equals(bidder.getPublicId())){
            bid.setStatus(BidStatus.INITIATED.toString());
            //NOTIFICATION
            userNotificationRequest.setTitle("Bid Received");
            userNotificationRequest.setBody("Your "+task.getTaskCategory().getName()+" task has received new bid from "+bidder.getFirstName()+" "+bidder.getLastName());
            userNotificationRequest.setFirebaseKey(task.getPoster().getFirebaseKey());
            UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, task.getPoster());
        } else {
            bid.setStatus(BidStatus.INVITED.toString());
            //NOTIFICATION
            userNotificationRequest.setTitle("Invitation Received");
            userNotificationRequest.setBody("You have been invited to perform "+task.getTaskCategory().getName() + " task");
            userNotificationRequest.setFirebaseKey(bidder.getFirebaseKey());
            UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, bidder);
        }


        bid.setDescription(addUserBidRequest.getDescription());
        bid.setOtherCostsExplanation(addUserBidRequest.getOtherCostsExplanation());
        bid.setTimeUtilizationExplanation(addUserBidRequest.getTimeUtilizationExplanation());
        bid.setTask(task);
        bid.setUser(bidder);
        bid = userBidRepository.save(bid);

        taskTimelineService.addTaskTimeline(bid.getStatus(), task, bidder, null);

        return bid;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserBid editBid(AddUserBidRequest addUserBidRequest, String bidPublicId, User user, String status) throws ResourceNotFoundException, UnauthorizedException {
        UserBid userBid = userBidRepository.findByPublicId(PublicIdGenerator.decodePublicId(bidPublicId));
        if(userBid == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_BID_NOT_FOUND_WITH_PUBLIC_ID, bidPublicId);
        if(!userBid.getUser().getPublicId().equals(user.getPublicId())
                || userBid.getStatus().equalsIgnoreCase(BidStatus.ACCEPTED.toString())
                || userBid.getStatus().equalsIgnoreCase(BidStatus.REJECTED.toString())){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        }

        if(status != null){
            userBid.setStatus(status);
        }

        userBid.setHours((int) addUserBidRequest.getHours());
        userBid.setHourlyRate((int) addUserBidRequest.getHourlyRate());
        userBid.setBudget(addUserBidRequest.getBudget());

        //NOTIFICATION
        Map<String,String> actionsInfo =  new HashMap<>();
        actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(userBid.getTask().getPublicId()));
        actionsInfo.put("bidPublicId", PublicIdGenerator.encodedPublicId(userBid.getPublicId()));

        UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                .notificationType(NotificationEnum.EDITED_BID.toString())
                .target(NotificationAction.EDITED_BID_PAGE.name())
                .title("Bid Edited")
                .body("Bid from "+user.getFirstName()+" "+user.getLastName()+" for task: "+userBid.getTask().getTitle()+" has been edited!")
                .imageUrl(BOT_LOGO_URL)
                .actions(NotificationAction.EDITED_BID_PAGE.name())
                .actionsInfo(actionsInfo)
                .firebaseKey(userBid.getTask().getPoster().getFirebaseKey())
                .build();

        UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, userBid.getTask().getPoster());

        return  userBidRepository.save(userBid);
    }

    @Override
    public List<UserBid> getAllBids(String userPublicId) {
        return userBidRepository.findAllByUserPublicId(PublicIdGenerator.decodePublicId(userPublicId));
    }

    @Override
    public UserBid getByBidId(String bidPublicId) throws ResourceNotFoundException {
        UserBid userBid = userBidRepository.findByPublicId(PublicIdGenerator.decodePublicId(bidPublicId));
        if(userBid == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_BID_NOT_FOUND_WITH_PUBLIC_ID, bidPublicId);
        return userBid;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteBid(String bidPublicId, User user) throws Exception {
        UserBid userBid = userBidRepository.findByPublicId(PublicIdGenerator.decodePublicId(bidPublicId));
        if(userBid == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_BID_NOT_FOUND_WITH_PUBLIC_ID, bidPublicId);
        if(!userBid.getUser().getPublicId().equals(user.getPublicId())
                || userBid.getStatus().equalsIgnoreCase(BidStatus.ACCEPTED.toString())
                || userBid.getStatus().equalsIgnoreCase(BidStatus.REJECTED.toString())){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        }

        userBid.setIsActive(false);
        userBid.setIsDeleted(true);
        userBidRepository.save(userBid);

        Map<String,String> actionsInfo =  new HashMap<>();
        actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(userBid.getTask().getPublicId()));
        actionsInfo.put("bidPublicId", PublicIdGenerator.encodedPublicId(userBid.getPublicId()));

        //NOTIFICATION
        UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
                .notificationType(NotificationEnum.DELETED_BID.toString())
                .target("INDIVIDUAL")
                .title("Bid Deleted")
                .body("Bid from "+user.getFirstName()+" "+user.getLastName()+" for task: "+userBid.getTask().getTitle()+" has been deleted!")
                .imageUrl(BOT_LOGO_URL)
                .actions(NotificationAction.DELETED_BID_PAGE.name())
                .actionsInfo(actionsInfo)
                .firebaseKey(userBid.getTask().getPoster().getFirebaseKey())
                .build();

        UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, userBid.getTask().getPoster());

        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserBid acceptRejectBid(User user, String taskPublicId, String bidPublicId, boolean accept) throws Exception {
        Task task = taskingService.getByTaskPublicId(taskPublicId);
        UserBid bid = getByBidId(bidPublicId);

        if(bid.getStatus().equalsIgnoreCase(BidStatus.INVITED.toString())){
            if(task.getPoster().getPublicId().equals(user.getPublicId())
                    || bid.getStatus().equalsIgnoreCase(BidStatus.ACCEPTED.toString())
                    || bid.getStatus().equalsIgnoreCase(BidStatus.REJECTED.toString())){
                throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
            }
        } else {
            if(!task.getPoster().getPublicId().equals(user.getPublicId())
                    || bid.getStatus().equalsIgnoreCase(BidStatus.ACCEPTED.toString())
                    || bid.getStatus().equalsIgnoreCase(BidStatus.REJECTED.toString())){
                throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
            }
        }


        if(!bid.getTask().getPublicId().equals(task.getPublicId())){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        }

        //NOTIFICATION
        Map<String,String> actionsInfo =  new HashMap<>();
        actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(bid.getTask().getPublicId()));
        actionsInfo.put("bidPublicId", PublicIdGenerator.encodedPublicId(bid.getPublicId()));

        UserNotificationRequest userNotificationRequest = new UserNotificationRequest();
        bid.setStatus(BidStatus.INITIATED.toString());
        userNotificationRequest.setTarget("INDIVIDUAL");
        userNotificationRequest.setImageUrl(BOT_LOGO_URL);
        userNotificationRequest.setActionsInfo(actionsInfo);

        if(accept){
            String status = BidStatus.ACCEPTED.toString();
            List<UserBid> userBids = taskingService.findAllTaskBids(taskPublicId);

            for (int i = 0; i < userBids.size(); i++) {
                if(userBids.get(i).getPublicId().equals(bid.getPublicId())){
                    userBids.get(i).setStatus(status);
                    bid.setStatus(BidStatus.ACCEPTED.toString());

                    //NOTIFICATION
                    userNotificationRequest.setNotificationType(NotificationEnum.ACCEPTED_BID.toString());
                    userNotificationRequest.setTitle("Bid Accepted");
                    userNotificationRequest.setActions(NotificationAction.ACCEPTED_BID_PAGE.name());
                    userNotificationRequest.setBody("Your bid for "+task.getTaskCategory().getName()+" have been accepted!");
                    userNotificationRequest.setFirebaseKey(bid.getUser().getFirebaseKey());
                    UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, bid.getUser());

                } else {
                    status = BidStatus.REJECTED.toString();
                    userBids.get(i).setStatus(status);

                    //NOTIFICATION
                    userNotificationRequest.setNotificationType(NotificationEnum.ACCEPTED_BID.toString());
                    userNotificationRequest.setTitle("Bid Declined");
                    userNotificationRequest.setActions(NotificationAction.REJECTED_BID_PAGE.name());
                    userNotificationRequest.setBody("Your bid for "+task.getTaskCategory().getName()+" have been declined and task assigned to the most effective bid of user '" + bid.getUser().getFirstName() + "'.");
                    userNotificationRequest.setFirebaseKey(userBids.get(i).getUser().getFirebaseKey());
                    UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, userBids.get(i).getUser());
                }
            }
            // Saving bids status
            userBidRepository.saveAll(userBids);
            // Saving task status
            task.setTaskStatus(TaskStatus.PENDING.toString());
            task.setIsAssigned(true);
            task.setIsApproved(true);
            task.setIsPending(true);
            task.setTasker(bid.getUser());
            task.setBudget(BigInteger.valueOf(bid.getBudget()));
            task.setHourlyRate(bid.getHourlyRate());
            task.setTaskTime((float) bid.getHours());
            taskRepository.save(task);

            // create conversation between Poster and Tasker
            User tasker = task.getTasker();
            User poster = task.getPoster();
            AddConversationRequest addConversationRequest = AddConversationRequest.builder()
                    .taskPublicId(taskPublicId)
                    .posterPublicId(PublicIdGenerator.encodedPublicId(poster.getPublicId()))
                    .taskerPublicId(PublicIdGenerator.encodedPublicId(tasker.getPublicId()))
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .build();

            Conversation conversation = conversationService.createConversation(addConversationRequest, task, tasker, poster);

            bid = userBidRepository.save(bid);

            //send Email Notification to Tasker
            Map<String, String> placeHolders = new HashMap<String, String>();
            placeHolders.put("category_name", task.getTaskCategory().getName());
            placeHolders.put("poster_name", poster.getFirstName());

            EmailNotificationDto emailNotificationDto = EmailNotificationDto.builder()
                    .to(tasker.getEmail())
                    .template(Constants.EmailTemplate.TASK_ASSIGNED_TEMPLATE.value())
                    .placeHolders(placeHolders).build();

        } else {
            String status = BidStatus.REJECTED.toString();
            bid.setStatus(status);

            //NOTIFICATION
            userNotificationRequest.setNotificationType(NotificationEnum.REJECTED_BID.toString());
            userNotificationRequest.setTitle("Bid Rejected");
            userNotificationRequest.setActions(NotificationAction.REJECTED_BID_PAGE.name());
            userNotificationRequest.setBody("Your bid for "+task.getTaskCategory().getName()+" have been turned down.");
            userNotificationRequest.setFirebaseKey(bid.getUser().getFirebaseKey());
            UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, bid.getUser());

            bid = userBidRepository.save(bid);
        }
        taskTimelineService.addTaskTimeline(bid.getStatus(), task, null, user);
        return bid;
    }
}
