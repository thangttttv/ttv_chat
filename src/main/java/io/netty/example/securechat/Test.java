package io.netty.example.securechat;

public class Test {
	
	public static String convert(int n) {
		  return Integer.toHexString(n);
	}
	
	public static void main(String[] args) {
		System.out.println(convert(18));
		System.out.println((byte)0x0E);
	}
}
