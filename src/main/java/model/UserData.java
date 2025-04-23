package model;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    private String name;
    private String username;
    private String cpf;
    private String hashedPassword;
    private final List<PatentData> userPatents;
    private double wallet = 300;

    public UserData(String name, String username, String cpf, String hashedPassword) {
        this.name = name;
        this.username = username;
        this.cpf = cpf;
        this.hashedPassword = hashedPassword;
        userPatents = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name + username + cpf + hashedPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void addUserPatent(PatentData patent) {
        userPatents.add(patent);
    }

    public List<PatentData> getUserPatents() {
        return userPatents;
    }

    public void removeUserPatent(PatentData patent) {
        userPatents.remove(patent);
    }

    public void setWallet(double coins) {
        this.wallet += coins;
    }

    public double getWallet() {
        return wallet;
    }

    // Novo método: compara uma senha em texto puro com a senha hash armazenada
    public boolean checkPassword(String plainPassword) {
        return hashedPassword.equals(hashPassword(plainPassword));
    }

    // Novo método: retorna a senha com hash (por compatibilidade)
    public String getPassword() {
        return hashedPassword;
    }

    // Método utilitário para aplicar SHA-256
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
}
