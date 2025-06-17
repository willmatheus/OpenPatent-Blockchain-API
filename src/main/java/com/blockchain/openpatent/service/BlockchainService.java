package com.blockchain.openpatent.service;

import model.PatentData;
import model.UserData;
import model.data.BuyPatent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BlockchainService {

    public void registerUser(UserData user) {

    }

    public void registerPatent(PatentData patentData) {

    }

    private String generateCurrentDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public boolean buyPatent(BuyPatent data) {
    }


    public List<PatentData> getUserPatents(String username) {
        UserData user = getUserByUsername(username);
        return user != null ? user.getUserPatents() : new ArrayList<>();
    }

    public List<PatentData> getAllPatents() {

    }

    public PatentData getPatentByTitle(String title) {

    }

    public List<UserData> getAllUsers() {

    }

    public UserData getUserByUsername(String username) {

    }

    public boolean login(String username, String rawPassword) {

    }
}
