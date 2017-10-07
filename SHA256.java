import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class SHA256 {

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
}