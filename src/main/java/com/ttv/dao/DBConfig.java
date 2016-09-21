package com.ttv.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBConfig {

	// private static final String fileName = "config.conf";

	// ========================================================================//
	// Database Parameters
	// ========================================================================//
	public static String db_shop_url_service = "jdbc:mysql://localhost:3306/vtc_swaphub?autoReconnect=true&characterEncoding=UTF-8";
	public static String db_shop_user_service = "root";
	public static String db_shop_pass_service = "";
	public static int db_connection = 3;
	public static int port = 1902;

	/**
	 * Contains the parameters and default values for this gateway such as
	 * system id, password, default npi, and ton of sender...
	 */
	private static Properties properties = new Properties();

	/**
	 * Loads configuration parameters from the file with the given name. Sets
	 * private variable to the loaded values.
	 */
	public static void loadProperties() throws IOException {

		FileInputStream propsFile = new java.io.FileInputStream("./conf/config.conf");
		properties.load(propsFile);
		propsFile.close();
		

		db_shop_url_service = properties.getProperty("db_shop_url_service",
				db_shop_url_service);
		db_shop_user_service = properties.getProperty("db_shop_user_service",
				db_shop_user_service);
		db_shop_pass_service = properties.getProperty("db_shop_pass_service",
				db_shop_pass_service);
		port = getIntProperty("port",port);

	}

	// Gets a property and converts it into byte.
	static byte getByteProperty(String propName, byte defaultValue) {
		return Byte.parseByte(properties.getProperty(propName,
				Byte.toString(defaultValue)).trim());
	}

	// Gets a property and converts it into integer.
	static int getIntProperty(String propName, int defaultValue) {
		return Integer.parseInt(properties.getProperty(propName,
				Integer.toString(defaultValue)).trim());
	}

}