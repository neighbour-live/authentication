package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserChat;
import com.bot.middleware.persistence.mapper.ChatMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddChatRequest;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.persistence.response.PageableResponseEntity;
import com.bot.middleware.resources.services.ChatService;
import com.bot.middleware.resources.services.TaskingService;
import com.bot.middleware.security.UserPrincipal;
import com.bot.middleware.utility.StatusCode;
import com.bot.middleware.utility.id.PublicIdGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private TaskingService taskingService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch specific chat.")
    public ResponseEntity getChat(@RequestParam("userPublicId") String userPublicId,
                                  @RequestParam("chatPublicId") String chatPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            UserChat userChat = chatService.getChatByPublicId(chatPublicId, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, ChatMapper.createChatDTOLazy(userChat), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }


    @GetMapping("/page")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to get chat by pagination.")
    public PageableResponseEntity<Object> getChatPage(@RequestParam("userPublicId") String userPublicId,
                                                      @RequestParam("taskPublicId") String taskPublicId,
                                                      @RequestParam(value = "pageNo") String pageNo,
                                                      @RequestParam(value = "pageSize") String pageSize
    ) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            if(Integer.valueOf(pageNo) < 0 || Integer.valueOf(pageSize) < 0){
                throw new Exception("PageNo and PageSize must be positive numbers.");
            }
            Task task = taskingService.getByTaskPublicId(taskPublicId);
            if(!user.getId().equals(task.getPoster().getId()) && !user.getId().equals(task.getTasker().getId())){
                throw new Exception("Task is not associated with current user.");
            }

            PageableResponseEntity<Object> pageableResponseEntity = chatService.getChatPage(Integer.valueOf(pageNo), Integer.valueOf(pageSize), user, task);
            return pageableResponseEntity;

        } catch (Exception e) {
            return ExceptionUtil.handlePaginatedException(e);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add new Chat.")
    public ResponseEntity<?> addChat(@Valid @RequestBody AddChatRequest addChatRequest) throws Exception {
        try {
            User user = isCurrentUser(addChatRequest.getUserPublicId());
            UserChat userChat = chatService.createChat(addChatRequest, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, ChatMapper.createChatDTOLazy(userChat), HttpStatus.OK);
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
