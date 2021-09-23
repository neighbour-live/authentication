package com.bot.middleware.crons;

import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserTemporary;
import com.bot.middleware.resources.services.UserService;
import com.bot.middleware.resources.services.UserTemporaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

@ComponentScan
public class Scheduler {
    @Autowired
    private UserService userService;

    @Autowired
    private UserTemporaryService userTemporaryService;

    //Running every 5 minutes
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Scheduled(cron = "0/5 * * * * ?")
    public void resetTemporaryUsers() {
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        System.out.println("\n\nReset Users Cron Started:: " + now.toString());
        ZonedDateTime yesterday = now.minusDays(1);
        Collection<UserTemporary> userTemporaryCollection = userTemporaryService.findAllByUpdateDateTimeBefore(yesterday);
        for (UserTemporary userTemp: userTemporaryCollection) {
            User user = userTemp.getUser();
            user.setEmail(userTemp.getEmail());
            userTemporaryService.save(userTemp);
            userService.save(user);
        }
        System.out.println("\n\nReset Users Cron Ended:: " + now.toString());
    }

}
