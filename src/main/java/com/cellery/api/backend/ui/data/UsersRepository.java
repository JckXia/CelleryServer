package com.cellery.api.backend.ui.data;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
}
