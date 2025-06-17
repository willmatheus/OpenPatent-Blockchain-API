package com.blockchain.openpatent.model;

import jakarta.persistence.*;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String username;
    private String cpf;
    private String password;
    private double wallet = 300;

    @OneToMany(mappedBy = "userData", cascade = CascadeType.ALL)
    private List<PatentData> userPatents = new ArrayList<>();

    public UserData() {}

    public UserData(String name, String username, String cpf, String hashedPassword) {
        this.name = name;
        this.username = username;
        this.cpf = cpf;
        this.password = hashedPassword;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getHashedPassword() { return password; }
    public void setHashedPassword(String hashedPassword) { this.password = hashedPassword; }

    public List<PatentData> getUserPatents() { return userPatents; }
    public void setUserPatents(List<PatentData> userPatents) { this.userPatents = userPatents; }

    public void addUserPatent(PatentData patent) {
        userPatents.add(patent);
    }

    public void removeUserPatent(PatentData patent) {
        userPatents.remove(patent);
    }

    public double getWallet() { return wallet; }
    public void setWallet(double wallet) { this.wallet = wallet; }

    public boolean checkPassword(String plainPassword) {
        return password.equals(hashPassword(plainPassword));
    }

    public String getPassword() {
        return password;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes)
                hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao aplicar hash na senha", e);
        }
    }

    @Override
    public String toString() {
        return name + "|" + username + "|" + cpf + "|" + password;
    }
}
