package com.app.middleware.security;

import com.app.middleware.exceptions.logging.GenericLog;
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
    private GenericLog log;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    private List<String> openUrls = Arrays.asList(
            "/auth/register",
            "/auth/login",
//            --------------------
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
//            --------------------
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
            "/bucket/file/upload",
            "/bucket/list/files",
//            ---------------------
            "/user/confirm-phone-number",
            "/user/edit-profile",
            "/user/logout",
            "/user/me",
            "/user/profile-completion",
            "/user/send-email-verification",
            "/user/send-phone-verification-otp",
            "/user/verification-status",
            "/bid",
            "/bid/status/([a-zA-Z0-9]+)",
            "/bid/([a-zA-Z0-9]+)",
            "/bid/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/conversation",
            "/conversation/page",
            "/report-user",
            "/review",
            "/review/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/skills",
            "/support-user",
            "/user-address",
            "/user-address/([a-zA-Z0-9]+)",
            "/user-address/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-bank/add-bank",
            "/user-bank/check-bank",
            "/user-bank/verify-bank",
            "/user-bank/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-bank/([a-zA-Z0-9]+)",
            "/user-wallet",
            "/user-wallet/account",
            "/user-wallet/account/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-wallet/balance",
            "user-wallet/frequency",
            "/user-wallet/redeem",
            "/user-wallet/transaction/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-wallet/transactions",
            "/awards",
            "/awards/user-award",
            "/awards/user-award/([a-zA-Z0-9]+)",
            "/awards/([a-zA-Z0-9]+)/user-award/([a-zA-Z0-9]+)",
            "/certifications",
            "/certifications/([a-zA-Z0-9]+)",
            "/certifications/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/chat",
            "/chat/page",
            "/notification",
            "/notification/archive",
            "/notification/archive-all",
            "/notification/email",
            "/notification/page",
            "/notification/read",
            "/notification/read-all",
            "/skills",
            "/skills/user/skill",
            "/skills/user/skill/([a-zA-Z0-9]+)",
            "/skills/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/task",
            "/task/bidder-tasks",
            "/task/bids",
            "/task/categories",
            "/task/featured-taskers",
            "/task/search-tasks",
            "/task/([a-zA-Z0-9]+)",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)/reschedule-request",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)/reschedule-response",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)/status/([a-zA-Z_]+)",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)/tasker-bookings",
            "/user-card/add-card",
            "/user-card/check-card",
            "/user-card/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-card/([a-zA-Z0-9]+)",
            "/task/([a-zA-Z0-9]+)/timeline");
    private List<String> moderatorUrls = Arrays.asList(
            "/auth/change-password",
            "/auth/refresh-token",
            "/user/confirm-phone-number",
            "/user/edit-profile",
            "/user/logout",
            "/user/me",
            "/user/profile-completion",
            "/user/send-email-verification",
            "/user/send-phone-verification-otp",
            "/user/verification-status",
            "/bid",
            "/bid/status/([a-zA-Z0-9]+)",
            "/bid/([a-zA-Z0-9]+)",
            "/bid/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/conversation",
            "/conversation/page",
            "/report-user",
            "/review",
            "/review/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/skills",
            "/support-user",
            "/user-address",
            "/user-address/([a-zA-Z0-9]+)",
            "/user-address/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-bank/add-bank",
            "/user-bank/check-bank",
            "/user-bank/verify-bank",
            "/user-bank/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-bank/([a-zA-Z0-9]+)",
            "/user-wallet",
            "/user-wallet/account",
            "/user-wallet/account/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-wallet/balance",
            "user-wallet/frequency",
            "/user-wallet/redeem",
            "/user-wallet/transaction/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-wallet/transactions",
            "/awards",
            "/awards/user-award",
            "/awards/user-award/([a-zA-Z0-9]+)",
            "/awards/([a-zA-Z0-9]+)/user-award/([a-zA-Z0-9]+)",
            "/certifications",
            "/certifications/([a-zA-Z0-9]+)",
            "/certifications/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/chat",
            "/chat/page",
            "/notification",
            "/notification/archive",
            "/notification/archive-all",
            "/notification/email",
            "/notification/page",
            "/notification/read",
            "/notification/read-all",
            "/skills",
            "/skills/user/skill",
            "/skills/user/skill/([a-zA-Z0-9]+)",
            "/skills/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/task",
            "/task/bidder-tasks",
            "/task/bids",
            "/task/categories",
            "/task/featured-taskers",
            "/task/search-tasks",
            "/task/([a-zA-Z0-9]+)",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)/reschedule-request",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)/reschedule-response",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)/status/([a-zA-Z]+)",
            "/task/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)/tasker-bookings",
            "/user-card/add-card",
            "/user-card/check-card",
            "/user-card/([a-zA-Z0-9]+)/user/([a-zA-Z0-9]+)",
            "/user-card/([a-zA-Z0-9]+)",
            "/task/([a-zA-Z0-9]+)/timeline");


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
