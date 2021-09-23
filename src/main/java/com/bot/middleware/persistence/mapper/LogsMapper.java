package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.Log;
import com.bot.middleware.persistence.dto.LogsDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogsMapper {

    public static LogsDTO createLogDTOLazy(Log log) {
        LogsDTO logsDTO = LogsDTO.builder()
                .id(log.getId())
                .userPublicId(PublicIdGenerator.encodedPublicId(log.getUserPublicId()))
                .ipAddress(log.getIpAddress())
                .request((log.getRequest() == null) ? "": log.getRequest())
                .response((log.getResponse() == null) ? "": log.getResponse())
                .createDateTime(log.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        return logsDTO;
    }

    public static List<LogsDTO> createLogsDTOListLazy(Collection<Log> logs) {
        List<LogsDTO> logsDTO = new ArrayList<>();
        logs.forEach(log -> logsDTO.add(createLogDTOLazy(log)));
        return logsDTO;
    }
}
