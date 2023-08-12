package com.appsdevelpersblog.app.ws;

import com.appsdevelpersblog.app.ws.oi.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
   
}
