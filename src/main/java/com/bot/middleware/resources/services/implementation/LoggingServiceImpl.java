package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.persistence.domain.Log;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.repository.LogsRepository;
import com.bot.middleware.resources.services.LoggingService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggingServiceImpl implements LoggingService {

    @Autowired
    private LogsRepository logsRepository;

    @Override
    public Log createLog(User user, String ipAddress, Object request, Object response) {
        Log log = new Log();
        log.setDeleted(false);
        log.setUserPublicId(user == null ? 0 : user.getPublicId());
        log.setIpAddress(ipAddress == null ? "127.0.0.1" : ipAddress);
        log.setRequest(request == null ? null : request.toString());
        log.setResponse(response == null ? null : response.toString());
        log = logsRepository.save(log);
        return log;
    }

    @Override
    public List<Log> getUserLogs(String userPublicId) {
        return logsRepository.findAllByUserPublicId(PublicIdGenerator.decodePublicId(userPublicId));
    }
}
