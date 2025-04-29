package com.blockchain.openpatent.controller;

import com.blockchain.openpatent.service.BlockchainService;
import model.Block;
import model.PatentData;
import model.UserData;
import model.data.BuyPatent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

    @Autowired
    private BlockchainService blockchainService;

    @PostMapping("/register-user")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserData user) {
        blockchainService.registerUser(user);

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
        blockchainService.registerPatent(patentData);
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
        return blockchainService.getUserByUsername(username) !=
                null ? blockchainService.getUserByUsername(username) : null;
    }

    @GetMapping("/patents")
    public List<PatentData> getAllPatents() {
        return blockchainService.getAllPatents();
    }

    @GetMapping("/validate")
    public String validateBlockchain() {
        boolean valid = blockchainService.validateBlockchain();
        return valid ? "Blockchain válida." : "Blockchain inválida!";
    }

    @PostMapping("/buy-patent")
    public ResponseEntity<Boolean> buyPatent(@RequestBody BuyPatent patentData) {
        return ResponseEntity.ok(blockchainService.buyPatent(patentData));
    }
}

