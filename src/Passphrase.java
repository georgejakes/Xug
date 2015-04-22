

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Passphrase {
	private String password;
	private String encrypted_pass;
	private int passcode;
	public int flag_enc ;
	public int flag_code;
	
	
	public Passphrase(String secret_pass){
		this.password = secret_pass;
		flag_enc=0;
		flag_code=0;
	}
	
	public  String Encode(){
		if(flag_enc==0){
			//System.out.print("\n...MD5 encoding... ");
			this.encrypted_pass = getMD5(this.password);
			flag_enc = 1 ;
			return this.encrypted_pass;
			
		}
		else {
		return this.encrypted_pass;	
		}
	}
			
	
	  public static String getMD5( String input) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            byte[] messageDigest = md.digest(input.getBytes());
	            BigInteger number = new BigInteger(1, messageDigest);
	            String hashtext = number.toString(16);
	            // Now we need to zero pad it if you actually want the full 32 chars.
	            while (hashtext.length() < 32) {
	                hashtext = "0" + hashtext;
	            }
	            return hashtext;
	        }
	        catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e);
	        }
	 }
	  
	 public Integer getcode(){
		 if(this.flag_enc == 0){
			 String temp = Encode();		 
		 }
		 if(this.flag_code==0){
			 int base = 1;
			 for (char ch: this.encrypted_pass.toCharArray()) {
				
				 base = code_function(base, (int)ch);
				 
			 }
			 this.passcode = base;
			 this.flag_code = 1;	
		 }
		 return this.passcode;
	
	 }
	 public Integer  code_function(int base, int temp){
		 base = (base + temp) ;
		 return base;
	 }
	
}
