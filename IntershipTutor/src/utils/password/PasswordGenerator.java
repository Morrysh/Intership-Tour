package utils.password;

import java.security.SecureRandom;

public class PasswordGenerator {

	static final String CHARS = "0123456789" +
							    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
							    "abcdefghijklmnopqrstuvwxyz";
							    // + "!@#$%^&*_=+-/";
	
	static SecureRandom random = new SecureRandom();

	public static String generatePassword(int length){
	   StringBuilder stringBuilder = new StringBuilder(length);
	   for(int i = 0; i < length; i++) 
		  stringBuilder.append(CHARS.charAt(random.nextInt(CHARS.length())));
	   return stringBuilder.toString();
	}
    
}