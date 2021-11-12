package com.app.middleware.security;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.type.RoleType;
import com.app.middleware.utility.ObjectUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    private List<String> openUrls = Arrays.asList(
            "/auth/register",
            "/auth/login",
            "/auth/check-email",
            "/auth/check-phone",
            "/auth/check-username",
            "/auth/send-email-code",
            "/auth/confirm-email",
            "/auth/send-phone-code",
            "/auth/confirm-phone",
            "/auth/confirm-username",
            "/auth/forgot-password",
            "/auth/change-password",
            "/user-uploads/all/([a-zA-Z0-9]+)",
            "/user-uploads/([a-zA-Z0-9]+)/upload/([a-zA-Z0-9]+)",
            "/error",
            "/v2/api-docs",
            "/swagger-ui",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/.*");
    private List<String> userUrls = Arrays.asList(
            "/user/confirm-email",
            "/user/confirm-phone",
            "/auth/refresh-token",
            "/user/confirm-phone-number",
            "/user/edit-profile",
            "/user/logout",
            "/user/me",
            "/user/profile-completion",
            "/user/send-email-verification",
            "/user/send-phone-verification-otp",
            "/user/verification-status",
            "/bucket/file/upload",
            "/bucket/file",
            "/user/upload-identification",
            "/user/file",
            "/user-uploads/all/([a-zA-Z0-9]+)",
            "/user-uploads/([a-zA-Z0-9]+)/upload/([a-zA-Z0-9]+)",
            "/offline-conversation",
            "/offline-conversation/page",
            "/offline-chat",
            "/offline-chat/page",
            "/user-address",
            "/user-address/([a-zA-Z0-9]+)",
            "/user-address/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/awards",
            "/awards/user-award",
            "/awards/user-award/([a-zA-Z0-9]+)",
            "/awards/([a-zA-Z0-9]+)/user-award/([a-zA-Z0-9]+)",
            "/notification",
            "/notification/archive",
            "/notification/archive-all",
            "/notification/email",
            "/notification/page",
            "/notification/read",
            "/notification/read-all");
    private List<String> moderatorUrls = Arrays.asList();


    @Before(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) && args(..)" )
    public void requestAccessLog() throws Exception {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String endpoint = req.getRequestURI();

        boolean isOpen = openUrls.stream().anyMatch(endpoint::matches);
        if(!isOpen){
            String token = TokenAuthenticationFilter.getJwtFromRequest(req);
            String userType = tokenProvider.getUserTypeFromToken(token);
            Long userId = tokenProvider.getUserIdFromToken(token);
            Optional<User> user = userRepository.findById(userId);
            boolean throwException = true , urlMatched = false;

            if(user.isPresent() && !ObjectUtils.isNull(user.get().getRole()) && user.get().getRole().getRoleType().toString().equals(userType)){
                if(userType.equals(RoleType.USER.toString())){
                    urlMatched = userUrls.stream().anyMatch(url -> endpoint.matches(url));
                } else if(userType.equals(RoleType.MODERATOR.toString())){
                    urlMatched = moderatorUrls.stream().anyMatch(url -> endpoint.matches(url));
                } else if(userType.equals(RoleType.ADMIN.toString())){
                    urlMatched = true;
                }
            }

            if(urlMatched){
                throwException = false;
            }

            if(throwException){
                throw new Exception("User un-authorized to perform this action");
            }
        }

    }
}
