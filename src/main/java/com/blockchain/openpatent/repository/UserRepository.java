package com.blockchain.openpatent.repository;

import com.blockchain.openpatent.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Long> {
    UserData getUserDataByUsername(String username);
}
