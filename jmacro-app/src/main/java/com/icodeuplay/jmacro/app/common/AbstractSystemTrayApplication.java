package com.icodeuplay.jmacro.app.common;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.icodeuplay.jmacro.app.screens.about.AboutView;
import com.icodeuplay.jmacro.app.util.ImageUtils;
import com.icodeuplay.jmacro.app.util.LookAndFeelSelector;
import com.icodeuplay.jmacro.common.exceptions.JMacroException;
import com.icodeuplay.jmacro.common.util.MessageUtils;

public abstract class AbstractSystemTrayApplication {

	protected PopupMenu menu;
	protected TrayIcon icon;
	protected SystemTray tray;
	protected ImageIcon image;
	protected MenuItem help;

	protected List<MenuItem> applicationMenu;
	protected List<MenuItem> systemMenu;

	public AbstractSystemTrayApplication() {
		this.applicationMenu = new ArrayList<MenuItem>();
		this.systemMenu = new ArrayList<MenuItem>();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					LookAndFeelSelector.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		try {

			this.menu = new PopupMenu();

			this.image = ImageUtils.getImage(MessageUtils.getString("app.icon.64"));
			this.icon = new TrayIcon(this.image.getImage(), MessageUtils.getString("app.title"));
			this.tray = SystemTray.getSystemTray();

			this.icon.setImageAutoSize(true);
			this.tray.add(this.icon);
			this.displayMessage(MessageUtils.getString("app.messages.tray.info.start"), TrayIcon.MessageType.INFO);

			this.menu = new PopupMenu();
			this.icon.setPopupMenu(this.menu);
			this.populateMenu();

			for (MenuItem item : this.applicationMenu) {
				this.menu.add(item);
			}

			this.menu.addSeparator();

			MenuItem about = new MenuItem(MessageUtils.getString("app.label.about"));
			about.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new AboutView();
				}
			});
			this.systemMenu.add(about);

			MenuItem exit = new MenuItem(MessageUtils.getString("app.label.exit"));
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			this.systemMenu.add(exit);

			for (MenuItem item : this.systemMenu) {
				this.menu.add(item);
			}

			this.beforeRun();

		} catch (Exception e) {
			throw new JMacroException(e);
		}
	}

	public void addApplicationMenuItem(MenuItem item) {
		this.applicationMenu.add(item);
	}

	public void addSystemMenuItem(MenuItem item) {
		this.systemMenu.add(item);
	}

	protected void displayMessage(String message, MessageType messageType) {
		this.icon.displayMessage(MessageUtils.getString("app.title"), message, messageType);
	}

	protected void setApplicationIcon(ImageIcon image, String message) {
		try {

			this.icon.setImage(image.getImage());
			this.displayMessage(message, TrayIcon.MessageType.INFO);

		} catch (Exception e) {
			throw new JMacroException(e);
		}
	}

	protected void setDefaultApplicationIcon() {
		try {
			this.image = ImageUtils.getImage(MessageUtils.getString("app.icon.64"));
			this.icon.setImage(this.image.getImage());
		} catch (Exception e) {
			throw new JMacroException(e);
		}
	}

	public abstract void populateMenu();

	public abstract void beforeRun();

}
