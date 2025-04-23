package model;

import java.security.MessageDigest;
import java.time.Instant;

public class Block {
    private final int index;
    private final String previousHash;
    private final String hash;
    private final Object data;
    private final long timestamp;

    public Block(int index, Object data, String previousHash) {
        this.index = index;
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = Instant.now().toEpochMilli();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            String input = previousHash + data.toString() + timestamp;
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
    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
    public Object getData() { return data; }
    public long getTimestamp() { return timestamp; }
}