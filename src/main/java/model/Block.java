package model;

import java.security.MessageDigest;
import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Block {
    private int index;
    private String previousHash;
    private String hash;
    private Object data;
    private int nonce;
    private long timestamp;

    public Block() {}

    public Block(int index, Object data, String previousHash) {
        this.index = index;
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = Instant.now().toEpochMilli();
        this.nonce = 0;
        this.hash = mineBlock();
    }

    private String mineBlock() {
        String calculatedHash;
        do {
            nonce++;
            calculatedHash = calculateHash();
        } while (!calculatedHash.startsWith("00"));
        return calculatedHash;
    }

    public String calculateHash() {
        try {
            String input = previousHash + data.toString() + timestamp + nonce;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes)
                hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getNonce() { return nonce; }
    public void setNonce(int nonce) { this.nonce = nonce; }
}
