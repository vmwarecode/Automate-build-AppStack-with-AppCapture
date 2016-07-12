package com.vmware.appvolumes;

import static java.awt.AWTKeyStroke.getAWTKeyStroke;
import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;

import java.awt.AWTException;
import java.awt.AWTKeyStroke;
import java.awt.Robot;
import java.awt.event.KeyEvent;

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

// The class for kicking off keyboard events
public class KeyMapperObject {

	private final Robot robot;

	public KeyMapperObject() throws AWTException {
		this.robot = new Robot();
	}

	// Recursion method to map keycode
	// including handle special chars
	private static AWTKeyStroke getKeyStroke(char c) {
		String upper = "`~'\"!@#$%^&*()_+{}|:<>?";
		String lower = "`~'\"1234567890-=[]\\;,./";

		int index = upper.indexOf(c);
		if (index != -1) {
			int keyCode;
			boolean shift = false;
			switch (c) {

			case '~':
				shift = true;
			case '`':
				keyCode = KeyEvent.VK_BACK_QUOTE;
				break;
			case '\"':
				shift = true;
			case '\'':
				keyCode = KeyEvent.VK_QUOTE;
				break;
			default:
				keyCode = (int) Character.toUpperCase(lower.charAt(index));
				shift = true;
			}
			return getAWTKeyStroke(keyCode, shift ? SHIFT_DOWN_MASK : 0);
		}
		return getAWTKeyStroke((int) Character.toUpperCase(c), 0);
	}

	// type directly method
	public void type(CharSequence chars) {
		type(chars, 0);
	}

	// hold for ms method
	public void type(CharSequence chars, int ms) {
		ms = ms > 0 ? ms : 0;
		for (int i = 0, len = chars.length(); i < len; i++) {
			char c = chars.charAt(i);
			AWTKeyStroke keyStroke = getKeyStroke(c);
			int keyCode = keyStroke.getKeyCode();
			boolean shift = Character.isUpperCase(c)
					|| keyStroke.getModifiers() == (SHIFT_DOWN_MASK + 1);
			if (shift) {
				robot.keyPress(KeyEvent.VK_SHIFT);
			}

			robot.keyPress(keyCode);
			robot.keyRelease(keyCode);

			if (shift) {
				robot.keyRelease(KeyEvent.VK_SHIFT);
			}
			if (ms > 0) {
				robot.delay(ms);
			}
		}
	}

}
