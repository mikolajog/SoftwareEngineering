package pl.boardies.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Hash {
    public static String hash(String message) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(message.getBytes());
    byte[] digest = md.digest();

    StringBuffer hexString = new StringBuffer();

        for (int i = 0;i<digest.length;i++) {
        hexString.append(Integer.toHexString(0xFF & digest[i]));
    }


    return hexString.toString();
}
    public static String hash(String message, Boolean addTime) throws NoSuchAlgorithmException {
    	long time = System.currentTimeMillis();
	    return hash(message+String.valueOf(time));
}
private Hash(){}

}
