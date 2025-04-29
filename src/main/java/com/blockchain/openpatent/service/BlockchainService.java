package com.blockchain.openpatent.service;

import model.Block;
import model.Blockchain;
import model.PatentData;
import model.UserData;
import model.data.BuyPatent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BlockchainService {

    private final Blockchain blockchain = new Blockchain();

    public void registerUser(UserData user) {
        blockchain.addBlock(user);
    }

    public void registerPatent(PatentData patentData) {

        if (patentData.getRegistrationDate() == null || patentData.getRegistrationDate().isEmpty()) {
            patentData.setRegistrationDate(generateCurrentDate());
        }

        blockchain.addBlock(patentData);

        UserData user = getUserByUsername(patentData.getInventor());
        if (user != null) {
            user.addUserPatent(patentData);
            blockchain.addBlock(user);
        }
    }

    private String generateCurrentDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public boolean buyPatent(BuyPatent data) {
        PatentData patentData = getPatentByTitle(data.patent().getTitle());
        if (patentData == null) {
            return false;
        }

        UserData buyer = getUserByUsername(data.username());
        UserData seller = getUserByUsername(patentData.getInventor());

        if (buyer == null || seller == null) {
            return false;
        }

        if (buyer.getUsername().equals(seller.getUsername())) {
            return false;
        }

        if (buyer.getWallet() < patentData.getPrice()) {
            return false;
        }

        buyer.setWallet(buyer.getWallet() - patentData.getPrice());
        seller.setWallet(seller.getWallet() + patentData.getPrice());

        patentData.setInventor(buyer.getUsername());

        seller.removeUserPatent(patentData); // remove do vendedor
        buyer.addUserPatent(patentData);     // adiciona ao comprador

        blockchain.addBlock(patentData);
        blockchain.addBlock(seller);     // atualiza o vendedor no blockchain
        blockchain.addBlock(buyer);      // atualiza o comprador no blockchain

        return true;
    }

    public boolean validateBlockchain() {
        return blockchain.isValid();
    }

    public List<PatentData> getUserPatents(String username) {
        UserData user = getUserByUsername(username);
        return user != null ? user.getUserPatents() : new ArrayList<>();
    }

    public List<PatentData> getAllPatents() {
        Map<String, PatentData> latestPatents = new LinkedHashMap<>();

        for (Block block : blockchain.getChain()) {
            if (block.getData() instanceof PatentData) {
                PatentData patent = (PatentData) block.getData();
                // Sempre sobrescreve com a versão mais recente encontrada na cadeia
                latestPatents.put(patent.getTitle(), patent);
            }
        }

        return new ArrayList<>(latestPatents.values());
    }

    public PatentData getPatentByTitle(String title) {
        for (PatentData patent : getAllPatents()) {
            System.out.println(patent.getTitle());
            System.out.println(title);
            if (patent.getTitle().equals(title.replace("\"", ""))) {
                return patent;
            }
        }
        return null;
    }

    public List<UserData> getAllUsers() {
        Map<String, UserData> latestUsers = new HashMap<>();
        for (Block block : blockchain.getChain()) {
            if (block.getData() instanceof UserData) {
                UserData user = (UserData) block.getData();
                latestUsers.put(user.getUsername(), user); // sobrescreve com a versão mais recente
            }
        }
        return new ArrayList<>(latestUsers.values());
    }

    public UserData getUserByUsername(String username) {
        for (UserData user : getAllUsers()) {
            if (user.getUsername().equals(username.replace("\"", ""))) {
                return user;
            }
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
