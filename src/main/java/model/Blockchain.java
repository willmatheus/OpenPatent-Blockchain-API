package model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import java.io.File;
import java.util.*;

public class Blockchain {
    private final List<Block> chain = new ArrayList<>();
    private final File blockchainDir = new File("blockchain_data");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Blockchain() {
        if (!blockchainDir.exists()) blockchainDir.mkdirs();
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        loadBlockchain();
        if (chain.isEmpty()) {
            Block genesis = new Block(0, new PatentData("Genesis", "OpenPatent", "Aplicativo de patentes", 99999999), "0");
            chain.add(genesis);
            saveBlockToFile(genesis);
        }
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
            objectMapper.writeValue(file, block);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar bloco em arquivo", e);
        }
    }

    private void loadBlockchain() {
        File[] files = blockchainDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return;

        Arrays.sort(files, Comparator.comparingInt(f -> Integer.parseInt(f.getName().replace(".json", ""))));

        for (File file : files) {
            try {
                Block block = objectMapper.readValue(file, Block.class);
                chain.add(block);
            } catch (Exception e) {
                System.err.println("Erro ao carregar bloco: " + file.getName() + " -> " + e.getMessage());
            }
        }
    }
}
