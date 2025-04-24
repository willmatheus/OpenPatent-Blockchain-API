package com.blockchain.openpatent.service;

import model.Block;
import model.Blockchain;
import model.PatentData;
import model.UserData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlockchainService {

    private final Blockchain blockchain = new Blockchain();

    public void registerUser(UserData user) {
        blockchain.addBlock(user);
    }

    public void registerPatent(PatentData patentData) {
        blockchain.addBlock(patentData);
        UserData user = getUserByUsername(patentData.getInventor());
        user.addUserPatent(patentData);
    }

    public boolean buyPatent(PatentData patentData, String username) {
        UserData userBuyer = getUserByUsername(username);

        if (userBuyer.getWallet() >= patentData.getPrice()) {
            UserData userSeller = getUserByUsername(patentData.getInventor());

            userBuyer.setWallet(-patentData.getPrice());
            userSeller.setWallet(patentData.getPrice());

            userBuyer.getUserPatents().add(patentData);
            userSeller.getUserPatents().remove(patentData);

            return true;
        } else {
            return false;
        }
    }

    public boolean validateBlockchain() {
        return blockchain.isValid();
    }

    public List<PatentData> getUserPatents(String username) {
        System.out.println("getAllUsers(): " + getAllUsers());
        return getUserByUsername(username).getUserPatents();
    }

    public List<PatentData> getAllPatents() {
        List<PatentData> patents = new ArrayList<>();
        for (Block block : blockchain.getChain()) {
            if (block.getData() instanceof PatentData) {
                patents.add((PatentData) block.getData());
            }
        }
        System.out.println(patents);
        return patents;
    }

    public List<UserData> getAllUsers() {
        List<UserData> users = new ArrayList<>();
        for (Block block : blockchain.getChain()) {
            if (block.getData() instanceof UserData) {
                users.add((UserData) block.getData());
            }
        }
        return users;
    }

    public UserData getUserByUsername(String username) {
        List<UserData> allUsers = getAllUsers();
        for (UserData user : allUsers) {
            if (user.getUsername().equals(username.replace("\"", ""))) {
                return user;
            }
            System.out.println(user.getUsername());
            System.out.println(username);
        }
        return null;
    }

    public boolean login(String username, String rawPassword) {
        for (UserData user : getAllUsers()) {
            if (user.getUsername().equals(username) && user.checkPassword(rawPassword)) {
                return true;
            }
        }
        return false;
    }
}