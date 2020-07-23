package com.cellery.api.backend.ui.data;

import org.springframework.data.repository.CrudRepository;

public interface LogRepository extends CrudRepository<LogEntity,Long> {
    LogEntity findByLogId(String logId);
}
