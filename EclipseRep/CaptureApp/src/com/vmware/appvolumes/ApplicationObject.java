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

// The class for Applications
// Note:
// The installation of Application here only implement the silent installation
// Please add the customized installation method if necessary
public class ApplicationObject {

	private Logger logUtil;
	private PropertiesCache cofig = new PropertiesCache();
	private KeyMapperObject keyboard = new KeyMapperObject();

	public ApplicationObject(Logger logUtil) throws Exception {

		this.logUtil = logUtil;

	}

	/*
	 * Main method for Installing Application in silent mode
	 */

	public void install() throws Exception {

		String appNumStr = this.cofig.getProperty("ApplicationNumber");
		int appNum = Integer.parseInt(appNumStr);
		String[] appList = new String[30];
		String[] appTime = new String[30];

		for (int i = 1; i <= appNum; i++) {
			String appItem = this.cofig.getProperty("Application"
					+ String.valueOf(i));
			appList[i - 1] = appItem;

			String appTItem = this.cofig.getProperty("App" + String.valueOf(i)
					+ "Time");
			appTime[i - 1] = appTItem;
		}

		this.installApplications(appNum, appList, appTime);

	}

	/*
	 * Handle applications which are planned to install. Recursion method: from
	 * bottom to top
	 */

	public int installApplications(int appNumber, String[] appList,
			String[] appTime) throws Exception {

		if (appNumber == 1) {
			// Install Application1
			this.logUtil.log(Level.INFO, "Handle app:" + appList[0] + " \n");
			System.out.println(appList[0]);
			this.appSilentInstall(appList[0]);

			int time = Integer.parseInt(appTime[0]) * 1000;

			Thread.sleep(time);
			System.out.println(appTime[0]);
			this.logUtil.log(Level.INFO, "Installing app:" + appList[0]
					+ " successfully! \n");

			this.appSilentInstallExit();
			return 1;
		} else {
			// Install Applications from the bottom to top (e.g. ApplicationN in
			// config.ini file)
			this.logUtil.log(Level.INFO, "Handle app:" + appList[appNumber - 1]
					+ " \n");

			this.appSilentInstall(appList[appNumber - 1]);

			System.out.println(appList[appNumber - 1]);

			int time = Integer.parseInt(appTime[appNumber - 1]) * 1000;

			Thread.sleep(time);
			System.out.println(appTime[appNumber - 1]);
			this.logUtil.log(Level.INFO, "Installing app:"
					+ appList[appNumber - 1] + " successfully! \n");

			this.appSilentInstallExit();
			return appNumber = installApplications(appNumber - 1, appList,
					appTime);
		}

	}

	/*
	 * Main method for Installing Application in silent mode
	 */

	public void appSilentInstall(String installerPath) throws Exception {

		String cmdStr = "cmd /c start start /wait " + installerPath
				+ " /S /v/qn";

		this.logUtil.log(Level.INFO, "Start installing app:" + installerPath
				+ " \n");

		System.out.println(cmdStr + " command line");

		Runtime.getRuntime().exec(cmdStr);
		System.out.println("ttt Install app" + installerPath
				+ " successfully! \n");

	}

	/*
	 * End method for Installing Application in silent mode
	 */

	public void appSilentInstallExit() throws Exception {

		this.keyboard.type("\n");
		Thread.sleep(3000L);
		this.keyboard.type("exit");
		this.keyboard.type("\n");
		Thread.sleep(3000L);
	}

}
