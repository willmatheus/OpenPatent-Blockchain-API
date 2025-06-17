package com.blockchain.openpatent.controller;

import com.blockchain.openpatent.service.BlockchainService;
import com.blockchain.openpatent.model.PatentData;
import com.blockchain.openpatent.model.UserData;
import com.blockchain.openpatent.model.data.BuyPatent;
import com.blockchain.openpatent.service.PatentService;
import com.blockchain.openpatent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    private UserService userService;
    @Autowired
    private PatentService patentService;

    @PostMapping("/register-user")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserData user) {
        userService.registerUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Usuário registrado com sucesso!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserData loginData) {
        boolean success = blockchainService.login(loginData.getUsername(), loginData.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Login bem-sucedido!" : "Usuário ou senha incorretos!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-patent")
    public String registerPatent(@RequestBody PatentData patentData) {
        patentService.createPatent(patentData);
        return "Patente registrada com sucesso!";
    }

    @GetMapping("/users")
    public List<UserData> getAllUsers() {
        return blockchainService.getAllUsers();
    }

    @PostMapping("/user-patents")
    public List<PatentData> getUserPatents(@RequestBody String username) {
        return blockchainService.getUserPatents(username);
    }

    @PostMapping("/user")
    public UserData getUser(@RequestBody String username) {
        return blockchainService.getUserByUsername(username) != null
                ? blockchainService.getUserByUsername(username) : null;
    }

    @GetMapping("/patents")
    public List<PatentData> getAllPatents() {
        return patentService.getAllPatents();
    }

    @PostMapping("/buy-patent")
    public ResponseEntity<Boolean> buyPatent(@RequestBody BuyPatent patentData) {
        return ResponseEntity.ok(blockchainService.buyPatent(patentData));
    }

    // NOVO ENDPOINT PARA TESTAR CONEXÃO COM A INFURA
    @GetMapping("/test-connection")
    public ResponseEntity<String> testConnection() {
        try {
            Web3ClientVersion clientVersion = blockchainService.getClientVersion();
            if (!clientVersion.hasError()) {
                return ResponseEntity.ok("Conexão bem-sucedida com: " + clientVersion.getWeb3ClientVersion());
            } else {
                return ResponseEntity.status(500).body("Erro: " + clientVersion.getError().getMessage());
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Exceção: " + e.getMessage());
        }
    }
}
