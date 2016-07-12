/**
 * 
 */
package com.vmware.appvolumes;

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

// The class for AppCapture object
public class AppCaptureObject {

	private Logger logUtil;
	private ApplicationObject targetApp;
	private KeyMapperObject keyboard = new KeyMapperObject();

	public AppCaptureObject(Logger logUtil) throws Exception {

		this.logUtil = logUtil;

		this.targetApp = new ApplicationObject(this.logUtil);
	}

	/*
	 * judge OS type
	 */

	public boolean osType() {

		String osArch = System.getProperty("os.arch");

		if (osArch.contains("64")) {
			System.out.println("64bit OS Arch is " + osArch);
			this.logUtil.log(Level.INFO, "64 bit OS Arch: " + osArch);
			return true;
		} else {

			System.out.println("32bit OS Arch is " + osArch);
			this.logUtil.log(Level.INFO, "32 bit OS Arch: " + osArch);
			return false;
		}

	}

	/*
	 * Main method for Capturing Applications in command line
	 */

	public void appCapture(String appStackName) throws Exception {

		String cmdStr = "cmd /k start ";

		this.logUtil.log(Level.INFO, "Start AppCapture with AppStack Name: "
				+ appStackName + " \n");

		Runtime.getRuntime().exec(cmdStr);
		Thread.sleep(5000L);

		this.keyboard.type("c:");
		this.keyboard.type("\n");
		String appCaptureLocator = null;

		// judge OS type
		if (this.osType())
			appCaptureLocator = "cd \"C:\\Program Files (x86)\\VMware\\AppCapture\"";
		else
			appCaptureLocator = "cd \"C:\\Program Files\\VMware\\AppCapture\"";

		this.keyboard.type(appCaptureLocator);
		this.keyboard.type("\n");

		// Capture command
		// TODO: handle x64 only
		String captureCommand = "AppCapture.exe /n " + appStackName;
		// Runtime.getRuntime().exec(captureCommand);
		this.keyboard.type(captureCommand);
		this.keyboard.type("\n");

		this.logUtil.log(Level.INFO, "Wait for installing applications: "
				+ appStackName + " \n");
		// install app
		// TODO:ApplicationObject
		Thread.sleep(30000L);
		this.targetApp.install();
		Thread.sleep(3000L);
		this.endRestart();

	}

	/*
	 * End capturing process then restart computer
	 */
	public void endRestart() throws Exception {

		// this.keyboard.type("exit");
		// this.keyboard.type("\n");
		this.logUtil.log(Level.INFO, "Complete capturing applications \n");
		Thread.sleep(3000L);

		this.logUtil.log(Level.INFO, "Computer reboot \n");
		Thread.sleep(3000L);

		this.keyboard.type("\n");
		Thread.sleep(3000L);

		this.keyboard.type("\n");
	}
}
