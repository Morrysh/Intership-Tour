package utils.password;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

public class SecurePassword {

	public static String getSHA1Password(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
            msdDigest.update(salt);
            generatedPassword = DatatypeConverter.printHexBinary(msdDigest.digest(passwordToHash.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static byte[] getSalt() {
        SecureRandom sr = null;
        byte[] salt = new byte[0];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            salt = new byte[16];
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return salt;
    }
	
}
