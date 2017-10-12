import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public abstract class HashUtilities {

	public static byte[] hash(String str) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			digest = null;
		}
		
		if(digest!=null) {
			byte[] encodedhash = digest.digest(
					str.getBytes(StandardCharsets.UTF_8));
			return encodedhash;
		}
		return null;
	}
	
	public static String bytesToHex(byte[] hash) {
		
		StringBuilder sb = new StringBuilder();
	    for (byte b : hash) {
	        sb.append(String.format("%02x", b));
	    }
	    
	    return sb.toString();
	}
	
	public static boolean compareHashes(byte[] h1, byte[] h2) {
		
		if(h1.length!=h2.length)
			return false;
		
		for(int i=0; i<h1.length; i++)
			if(h1[i]!=h2[i])
				return false;
		
		return true;
	}
	
	public static byte[] hexToByte(String s) {
	    return DatatypeConverter.parseHexBinary(s);
	}
	
	public static boolean isHex(String str) {
		
		int strLength = str.length();
		for(int i=0; i<strLength; i++)
			if(Character.digit(str.charAt(i), 16)==-1)
				return false;
		
		return true;
	}
}
