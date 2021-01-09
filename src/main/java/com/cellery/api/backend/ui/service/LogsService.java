package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.LogDto;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.shared.Util.Utils;
import com.cellery.api.backend.ui.data.LogEntity;
import com.cellery.api.backend.ui.data.LogRepository;
import com.cellery.api.backend.ui.data.UserEntity;
import com.cellery.api.backend.ui.data.UsersRepository;
import com.cellery.api.backend.ui.model.request.CreateLogRequestModel;
import com.cellery.api.backend.ui.model.request.UpdateLogRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;


// Create log
// Get Log
// Update Log
// Delete Log
// Attach image to log

@Service
public class LogsService {

    private UsersRepository usersRepository;
    private LogRepository logsRepository;
    private MapperUtil mapper;
    private Utils utils;

    @Autowired
    public LogsService(UsersRepository userRepo, LogRepository logRepo, MapperUtil mapper, Utils utils) {
        this.usersRepository = userRepo;
        this.logsRepository = logRepo;
        this.mapper = mapper;
        this.utils = utils;
    }

    @Transactional
    public LogDto createLog(CreateLogRequestModel logCreationReq, String email) {

        UserEntity userEntity = usersRepository.getOneByEmail(email);
        if (userEntity == null) {
            throw new RuntimeException("Error! user does not exist!");
        }
        LogEntity logEntity = new LogEntity();
        logEntity.setLogId(UUID.randomUUID().toString());

        logEntity.setLogUser(userEntity);
        Long epochTimeStamp = logCreationReq.getCreationTimeStamp();
        logEntity.setCreatedTimeStamp(epochTimeStamp);
        logsRepository.save(logEntity);
        LogDto returnDto = mapper.strictMapper().map(logEntity, LogDto.class);

        return returnDto;
    }

    public Boolean logBelongsToUser(String email, String logId) {
        LogEntity logEntity = logsRepository.findByLogId(logId);
        if (logEntity == null) {
            return false;
        }

        UserEntity logUser = logEntity.getLogUser();
        return logUser.getEmail().equals(email);
    }

    public Boolean logCanBeEdited(UpdateLogRequestModel logUpdateReq, String logId) {
        LogEntity logEntity = logsRepository.findByLogId(logId);
        if (logEntity == null) {
            return false;
        }
        Long updateTimeStamp = logUpdateReq.getLogUpdateTimeStamp();
        Long logCreationStamp = logEntity.getCreatedTimeStamp();
        return utils.getDateFromEpoch(updateTimeStamp).equals(utils.getDateFromEpoch(logCreationStamp));
    }

    public List<LogDto> getLogEntityBetweenTimeStamps(Integer userId, Long startDate, Long endDate) {
        List<LogEntity> logEntityObjects = logsRepository.findAllBetweenRange(userId, startDate, endDate);
        return logEntityObjects.stream().map(logEntity -> mapper.strictMapper().map(logEntity, LogDto.class)).collect(Collectors.toList());
    }

    public LogDto updateLogEntity(UpdateLogRequestModel requestObject, String logId) {
        LogEntity logEntity = logsRepository.findByLogId(logId);
        logEntity.setAmRoutine(requestObject.getAmRoutine());
        logEntity.setPmRoutine(requestObject.getPmRoutine());
        logEntity.setRating(requestObject.getRating());
        logEntity.setIsTimeOfMonth(requestObject.getIsTimeOfMonth());
        logEntity.setNotes(requestObject.getNotes());
        logsRepository.save(logEntity);
        LogDto returnDto = mapper.strictMapper().map(logEntity, LogDto.class);
        return returnDto;
    }

}
