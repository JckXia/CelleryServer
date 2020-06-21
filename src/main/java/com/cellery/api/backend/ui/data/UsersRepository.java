package com.cellery.api.backend.ui.data;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
    UserEntity getOneByEmail(String email); // returns a reference to the db object
}
