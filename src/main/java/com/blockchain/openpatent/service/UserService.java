package com.blockchain.openpatent.service;

import com.blockchain.openpatent.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blockchain.openpatent.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(UserData user) {
        userRepository.save(user);
    }
}
