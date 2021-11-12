package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.Conversation;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.mapper.ConversationMapper;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.persistence.response.PageableResponseEntity;
import com.app.middleware.resources.services.ConversationService;
import com.app.middleware.security.UserPrincipal;
import com.app.middleware.utility.StatusCode;
import com.app.middleware.utility.id.PublicIdGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offline-conversation")
public class ConversationController {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ConversationService conversationService;
//
//    @GetMapping
//    @PreAuthorize("hasRole('USER')")
//    @ApiOperation(value = "This operation is used to fetch conversation.")
//    public ResponseEntity getConversation(@RequestParam("userPublicId") String userPublicId,
//                                          @RequestParam("taskPublicId") String taskPublicId) throws Exception {
//        try {
//            User user = isCurrentUser(userPublicId);
//            Conversation conversation = conversationService.getConversation(taskPublicId, user);
//            return GenericResponseEntity.create(StatusCode.SUCCESS, ConversationMapper.createConversationDTOLazy(conversation, user), HttpStatus.OK);
//        } catch (Exception e) {
//            return ExceptionUtil.handleException(e);
//        }
//    }
//
//
//    @GetMapping("/page")
//    @PreAuthorize("hasRole('USER')")
//    @ApiOperation(value = "This operation is used to fetch conversation by pagination.")
//    public PageableResponseEntity<Object> getConversationPage(@RequestParam("userPublicId") String userPublicId,
//                                                              @RequestParam(value = "pageNo") String pageNo,
//                                                              @RequestParam(value = "pageSize") String pageSize
//                                                              ) throws Exception {
//        try {
//            User user = isCurrentUser(userPublicId);
//            if(Integer.valueOf(pageNo) < 0 || Integer.valueOf(pageSize) < 0){
//                throw new Exception("PageNo and PageSize must be positive numbers.");
//            }
//
//            PageableResponseEntity<Object> pageableResponseEntity = conversationService.getConversationPage(Integer.valueOf(pageNo), Integer.valueOf(pageSize), user);
//            return pageableResponseEntity;
//
//        } catch (Exception e) {
//            return ExceptionUtil.handlePaginatedException(e);
//        }
//    }
//
//    public User isCurrentUser(String userPublicId) throws ResourceNotFoundException, UnauthorizedException {
//        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(userPublicId));
//        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, userPublicId);
//
//        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if(!userPrincipal.getEmail().equals(user.getEmail())) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
//
//        return user;
//    }
}
