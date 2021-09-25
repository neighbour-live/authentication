package com.app.middleware.resources.services;

import com.app.middleware.persistence.domain.Log;
import com.app.middleware.persistence.domain.User;
import java.util.List;

public interface LoggingService {

    Log createLog(User user, String ipAddress, Object response, Object request);

    List<Log> getUserLogs(String userPublicId);
}
