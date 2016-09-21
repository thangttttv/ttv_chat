package com.ttv.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.sf.json.JSONObject;

public class ChatUtil {
	
	public static  byte[] intToBytes(int i)
    {
	      byte[] result = new byte[4];

	      result[0] = (byte) (i >> 24);
	      result[1] = (byte) (i >> 16);
	      result[2] = (byte) (i >> 8);
	      result[3] = (byte) (i /*>> 0*/);

	      return result;
	}
	
	public static String md5(String input) throws NoSuchAlgorithmException {
	    String result = input;
	    if(input != null) {
	        MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
	        md.update(input.getBytes());
	        BigInteger hash = new BigInteger(1, md.digest());
	        result = hash.toString(16);
	        while(result.length() < 32) { //40 for SHA-1
	            result = "0" + result;
	        }
	    }
	    return result;
	}
	
	public static void main(String[] args) {
		 JSONObject obj = new JSONObject();

	      obj.put("name", "foo");
	      obj.put("num", new Integer(100));
	      obj.put("balance", new Double(1000.21));
	      obj.put("is_vip", new Boolean(true));

	      System.out.print(obj.toString());
	      
	      try {
			System.out.println(ChatUtil.md5("thangtt"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
