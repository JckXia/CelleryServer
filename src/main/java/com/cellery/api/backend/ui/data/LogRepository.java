package com.cellery.api.backend.ui.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogRepository extends CrudRepository<LogEntity, Long> {
    LogEntity findByLogId(String logId);

    @Query(value = "SELECT * FROM logs log WHERE log.created_time_stamp BETWEEN :startDateStamp AND :endDateStamp", nativeQuery = true)
    List<LogEntity> findAllBetweenRange(@Param("startDateStamp") long startDateStamp, @Param("endDateStamp") long endDateStamp);
}
