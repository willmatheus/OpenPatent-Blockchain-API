package com.blockchain.openpatent.service;

import model.PatentData;
import model.UserData;
import model.data.BuyPatent;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;

    // URL e chave privada da carteira Ethereum (recomenda-se armazenar em application.properties)
    private static final String INFURA_URL = ""; // substitua pela sua chave
    private static final String PRIVATE_KEY = ""; // substitua pela sua chave

    public BlockchainService() {
        this.web3j = Web3j.build(new HttpService(INFURA_URL));
        this.credentials = Credentials.create(PRIVATE_KEY);
    }

    /**
     * Retorna a versão do cliente Ethereum conectado (usado para testar a conexão).
     */
    public Web3ClientVersion getClientVersion() throws IOException {
        return web3j.web3ClientVersion().send();
    }

    public void registerUser(UserData user) {
        // ainda não implementado
    }

    public void registerPatent(PatentData patentData) {
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            System.out.println("Conectado ao Ethereum via Infura:");
            System.out.println("Versão do cliente: " + clientVersion);
            System.out.println("Endereço da carteira: " + credentials.getAddress());

            // Exibe o saldo atual (em wei)
            BigInteger balance = web3j
                    .ethGetBalance(credentials.getAddress(), org.web3j.protocol.core.DefaultBlockParameterName.LATEST)
                    .send()
                    .getBalance();
            System.out.println("Saldo da conta: " + balance + " wei");

        } catch (Exception e) {
            System.err.println("Erro ao conectar à Infura: " + e.getMessage());
        }
    }

    public boolean buyPatent(BuyPatent data) {
        return false; // ainda não implementado
    }

    public List<PatentData> getUserPatents(String username) {
        UserData user = getUserByUsername(username);
        return user != null ? user.getUserPatents() : new ArrayList<>();
    }

    public List<PatentData> getAllPatents() {
        return new ArrayList<>(); // ainda não implementado
    }

    public PatentData getPatentByTitle(String title) {
        return null; // ainda não implementado
    }

    public List<UserData> getAllUsers() {
        return new ArrayList<>(); // ainda não implementado
    }

    public UserData getUserByUsername(String username) {
        return null; // ainda não implementado
    }

    public boolean login(String username, String rawPassword) {
        return false; // ainda não implementado
    }

    private String generateCurrentDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }
}
