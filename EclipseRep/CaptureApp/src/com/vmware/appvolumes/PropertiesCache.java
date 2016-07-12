package com.vmware.appvolumes;

/**
 * VMware utility class developed for packaging the applications silent
 * installer into AppStacks So that the applications could be used for App
 * Volumes 3.0, Horizon Air Hybrid-Mode, etc.
 * 
 * Author: Ellen Zhang Website:http://pubs.vmware.com/appvolumes-30 Product:
 * VMware App Volumes Product: Horizon Air Hybrid-Mode Reference:
 * http://pubs.vmware
 * .com/appvolumes-30/topic/com.vmware.ICbase/PDF/appvolumes-30
 * -install-admin.pdf
 * 
 * Prerequisites: UAC is turned off
 * 
 * 
 * Overall Design: 1. Silent install AppCapture utility with administrator
 * privileges Note: Detail information:
 * http://pubs.vmware.com/appvolumes-30/topic
 * /com.vmware.ICbase/PDF/appvolumes-30-install-admin.pdf
 * 
 * 2. Packaging the customer installer into AppStacks Note: The installer should
 * support silent install mode
 * 
 * 3. Put the packaged AppStacks into customer's file share
 * 
 * @author Ellen Zhang (zhange@vmware.com)
 * 
 *
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

//The class used to handle config.ini file
public class PropertiesCache {
	private final Properties configProp = new Properties();

	PropertiesCache() throws IOException {

		InputStream in = new FileInputStream(".\\config.ini");

		System.out.println("Read all properties from config.ini");
		try {
			configProp.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return configProp.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return configProp.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return configProp.containsKey(key);
	}

}