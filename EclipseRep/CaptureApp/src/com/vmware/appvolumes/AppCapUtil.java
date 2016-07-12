/**
 * 
 */
package com.vmware.appvolumes;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

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

// The main utility class for kicking off the process
public class AppCapUtil {

	private Handler fileHandler;
	private Logger logUtil;

	// the object used to read config file
	private PropertiesCache cofig = new PropertiesCache();
	// the object used to type keyboard
	private KeyMapperObject keyboard = new KeyMapperObject();

	// application object, install method
	private ApplicationObject targetApp;
	// app capture object, capture the applications
	private AppCaptureObject appCapture;

	public AppCapUtil() throws Exception {
		logUtil = Logger.getLogger("com.vmware.appvolumes");
		fileHandler = new FileHandler(".\\logTest.log", 200, 10);

		fileHandler.setLevel(Level.ALL);
		logUtil.addHandler(fileHandler);

		this.targetApp = new ApplicationObject(this.logUtil);
		this.appCapture = new AppCaptureObject(this.logUtil);

	}

	/*
	 * Judge if AppCapture is installed
	 */

	public boolean appCaptureIns() throws Exception {

		String install = this.cofig.getProperty("AppCaptureInstalled");

		if (install.equals("1")) {
			return true;

		} else
			return false;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			File conFile = new File(".\\config.ini");

			String path = new java.io.File(".").getCanonicalPath();
			System.out.println("get config.ini file from " + path);

			// Check if config.ini is ready
			if (!conFile.exists() || conFile.isDirectory()) {

				System.out.println("No config.ini avaiable in current dir \n");
				// test.logUtil.log(Level.SEVERE,"No config.ini avaiable in current dir "
				// + path);

				System.exit(1);
			}

			AppCapUtil test = new AppCapUtil();

			// Check if AppCapture tool is ready in config.ini
			if (!test.appCaptureIns()) {

				System.out.println("Please install AppCapture \n");
				test.logUtil.log(Level.SEVERE,
						"No AppCapture tool available, please install it!");
				System.out.println("Install Insturction: \n");
				System.out.println("1. Download the AppCapture installer \n");
				System.out
						.println("2. Run commonCapture.bat to install AppCapture in silent mode  \n");
				System.out.println("3. Update config.ini file  \n");
				System.out
						.println("Note: it must be installed with administrator privileges \n");

				System.exit(1);
			}

			String appCapStr = null;
			if (test.appCapture.osType())
				appCapStr = "C:\\Program Files (x86)\\VMware\\AppCapture\\AppCapture.exe";
			else
				appCapStr = "C:\\Program Files\\VMware\\AppCapture\\AppCapture.exe";

			System.out.println(appCapStr);
			File appCapExe = new File(appCapStr);

			// Check if the AppCapture.exe is available
			if (!appCapExe.exists() || appCapExe.isDirectory()) {

				System.out.println("No AppCapture tool avaiable \n");
				test.logUtil.log(Level.SEVERE,
						"No AppCapture tool available, please install it!");
				System.out.println("Install Insturction: \n");
				System.out.println("1. Download the AppCapture installer \n");
				System.out
						.println("2. Run commonCapture.bat to install AppCapture in silent mode  \n");
				System.out.println("3. Update config.ini file  \n");
				System.out
						.println("Note: it must be installed with administrator privileges \n");

				System.exit(1);
			}

			String appList = test.cofig.getProperty("ApplicationNumber");

			// Check if ApplicationNumber in config.ini is filled in
			if (appList == null) {

				System.out.println("Please fill in the config.ini \n");
				test.logUtil.log(Level.SEVERE,
						"No Application is available for capturing");
				System.out.println("Insturction: \n");
				System.out.println("1. Put the installer in target folder\n");
				System.out.println("2. Update it in config.ini file  \n");

				System.exit(1);
			}

			int appNum = Integer.parseInt(appList);

			for (int i = 0; i < appNum; i++) {

				String targetNumber = String.valueOf(i + 1);

				String appInstall = "Application" + targetNumber;

				// Check if ApplicationN in config.ini is filled in
				String appInstaller = test.cofig.getProperty(appInstall);
				if (appInstaller == null) {

					System.out.println("Please fill in the config.ini \n");
					System.out
							.println("ApplicationNumber is not correct for Application"
									+ appInstall);
					test.logUtil.log(Level.SEVERE,
							"No Application is available for capturing Application"
									+ appInstall);
					System.out.println("Insturction: \n");
					System.out
							.println("1. Put the installer in target folder\n");
					System.out.println("2. Update it in config.ini file  \n");

					System.exit(1);
				}

				File ins = new File(appInstaller);

				// Check if the installer for ApplicationN is available

				if (!ins.exists() || ins.isDirectory()) {

					System.out.println("No application install avaiable \n");
					test.logUtil.log(Level.SEVERE,
							"No Application installer available for capturing Application"
									+ appInstall);
					System.out.println("Put installer for:" + appInstall);

					System.exit(1);
				}

			}

			String appStackName = test.cofig.getProperty("AppStackName");

			test.appCapture.appCapture(appStackName);

			Thread.sleep(3000L);

			System.out
					.println("Here is the standard error of the command (if any):\n");

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
