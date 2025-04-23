package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blockchain {
    private final List<Block> chain = new ArrayList<>();

    public Blockchain() {
        // Bloco Gênesis com uma patente de exemplo
        chain.add(new Block(0, new PatentData("Genesis", "Start", "the start of blockchain", 99999999), "0"));
    }

    public void addBlock(Object data) {
        Block lastBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), data, lastBlock.getHash());
        chain.add(newBlock);
    }

    public boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (!current.getHash().equals(current.calculateHash())) {
                return false;
            }

            if (!current.getPreviousHash().equals(previous.getHash())) {
                return false;
            }
        }
        return true;
    }

    public List<Block> getChain() {
        return chain;
    }
}
