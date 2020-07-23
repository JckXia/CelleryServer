package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.LogDto;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.data.LogEntity;
import com.cellery.api.backend.ui.data.LogRepository;
import com.cellery.api.backend.ui.data.UserEntity;
import com.cellery.api.backend.ui.data.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


// Create log
// Get Log
// Update Log
// Delete Log
// Attach image to log

@Service
public class LogsService {

    private UsersRepository usersRepository;
    private LogRepository logsRepository;
    private  MapperUtil mapper;

    @Autowired
    public LogsService(UsersRepository userRepo, LogRepository logRepo , MapperUtil mapper){
        this.usersRepository = userRepo;
        this.logsRepository = logRepo;
        this.mapper = mapper;
    }

    @Transactional
    public LogDto createLog(String email){

        UserEntity userEntity = usersRepository.getOneByEmail(email);
        if(userEntity == null){
            throw new RuntimeException("Error! user does not exist!");
        }
        LogEntity logEntity = new LogEntity();
        logEntity.setLogId(UUID.randomUUID().toString());

        logEntity.setLogUser(userEntity);

        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        String timeStamp = date.format(currentTime);
        logEntity.setCreatedAt(timeStamp);
        logsRepository.save(logEntity);

        LogDto returnDto = mapper.strictMapper().map(logEntity,LogDto.class);

        return returnDto;
    }

    public Boolean logBelongsToUser(String email, String logId){
        LogEntity logEntity =  logsRepository.findByLogId(logId);
        if(logEntity == null){
            return false;
        }
        UserEntity logUser = logEntity.getLogUser();
        return logUser.getEmail().equals(email);
    }

    public LogDto findLogById(String logId){
        LogEntity logEntity = logsRepository.findByLogId(logId);
        LogDto returnDto = mapper.strictMapper().map(logEntity,LogDto.class);
        return returnDto;
    }
}
