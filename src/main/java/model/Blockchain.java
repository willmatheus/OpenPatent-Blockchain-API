package model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Blockchain {
    private final List<Block> chain = new ArrayList<>();
    private final File blockchainDir = new File("blockchain_data");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Blockchain() {
        if (!blockchainDir.exists()) blockchainDir.mkdirs();

        // Ativa tipagem dinâmica para suportar objetos genéricos (ex: PatentData)
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        loadBlockchain(); // Carrega blocos do disco

        if (chain.isEmpty()) {
            // Criação do bloco gênesis
            Block genesis = new Block(0,
                    new PatentData("Genesis", "OpenPatent", "Aplicativo de patentes", 99999999, generateCurrentDate()),
                    "0");
            chain.add(genesis);
            saveBlockToFile(genesis);
        }
    }

    private String generateCurrentDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public void addBlock(Object data) {
        Block lastBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), data, lastBlock.getHash());
        chain.add(newBlock);
        saveBlockToFile(newBlock);
    }

    public boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (current.getIndex() != i) {
                System.out.println("Índice incorreto no bloco " + i);
                return false;
            }

            if (!current.getHash().equals(current.calculateHash())) {
                System.out.println("Hash inválido no bloco " + i);
                return false;
            }

            if (!current.getPreviousHash().equals(previous.getHash())) {
                System.out.println("Hash anterior não bate no bloco " + i);
                return false;
            }

            if (!current.getHash().startsWith("00")) {
                System.out.println("Prova de trabalho inválida no bloco " + i);
                return false;
            }
        }

        return true;
    }

    public List<Block> getChain() {
        return chain;
    }

    private void saveBlockToFile(Block block) {
        try {
            File file = new File(blockchainDir, block.getIndex() + ".json");

            // Evita sobrescrever blocos antigos se já existem (opcional)
            if (file.exists()) return;

            objectMapper.writeValue(file, block);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar bloco em arquivo", e);
        }
    }

    private void loadBlockchain() {
        File[] files = blockchainDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("Nenhum bloco encontrado. Criando nova blockchain...");
            return;
        }

        Arrays.sort(files, Comparator.comparingInt(f -> Integer.parseInt(f.getName().replace(".json", ""))));

        for (File file : files) {
            try {
                Block block = objectMapper.readValue(file, Block.class);

                // Validação simples de sequência
                if (block.getIndex() != chain.size()) {
                    System.err.println("Bloco fora de ordem: " + file.getName());
                    continue;
                }

                chain.add(block);
                System.out.println("Bloco carregado: " + file.getName());
            } catch (Exception e) {
                System.err.println("Erro ao carregar bloco: " + file.getName() + " -> " + e.getMessage());
            }
        }
    }

    // (Opcional) Método para salvar toda a blockchain num único JSON (backup ou exportação)
    public void saveEntireBlockchain() {
        try {
            File file = new File("blockchain_backup.json");
            objectMapper.writeValue(file, chain);
            System.out.println("Blockchain salva em backup único.");
        } catch (Exception e) {
            System.err.println("Erro ao salvar backup completo da blockchain.");
        }
    }
}
