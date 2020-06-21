package com.cellery.api.backend.ui.data;

import org.springframework.data.repository.CrudRepository;

public interface RoutinesRepository extends CrudRepository<RoutineEntity, Long> {
    RoutineEntity getOneByRoutineId(String routineId);
}
