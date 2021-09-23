package com.bot.middleware.resources.services;

import com.bot.middleware.persistence.domain.Log;
import com.bot.middleware.persistence.domain.User;
import java.util.List;

public interface LoggingService {

    Log createLog(User user, String ipAddress, Object response, Object request);

    List<Log> getUserLogs(String userPublicId);
}
