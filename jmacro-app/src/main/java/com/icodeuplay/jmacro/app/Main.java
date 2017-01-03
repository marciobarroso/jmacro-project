package com.icodeuplay.jmacro.app;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icodeuplay.jmacro.app.common.ApplicationFrame;
import com.icodeuplay.jmacro.app.screens.about.AboutView;
import com.icodeuplay.jmacro.app.screens.help.HelpView;
import com.icodeuplay.jmacro.app.screens.initial.InitialPanel;
import com.icodeuplay.jmacro.app.util.ImageUtils;
import com.icodeuplay.jmacro.app.util.LookAndFeelSelector;
import com.icodeuplay.jmacro.common.exceptions.JMacroException;
import com.icodeuplay.jmacro.common.util.Benchmark;
import com.icodeuplay.jmacro.common.util.MessageUtils;
import com.icodeuplay.jmacro.common.util.ScreenUtils;

/**
 * Application main class
 */
public class Main {

	private ApplicationFrame containner;
	private JMenu applicationMenu;
	private JMenu helpMenu;

	private JMenuItem exit;

	private JMenuItem processManagerItem;

	private JMenuItem helpMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem themeMenuItem;

	private Benchmark benchmark;

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public Main() {
		try {
			this.benchmark = new Benchmark();

			this.containner = ApplicationFrame.getInstance(ScreenUtils.getBounds(800, 600, true));
			LOGGER.debug("Initialize main container in " + this.benchmark.getPartialTime());

			this.containner.addPanel(new InitialPanel());
			LOGGER.debug("Load initialPanel in " + this.benchmark.getPartialTime());

			this.populateMenu();
			LOGGER.debug("Populate menu in " + this.benchmark.getPartialTime());

			this.benchmark.end();
			LOGGER.debug("Finish in " + this.benchmark.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void populateMenu() {
		this.applicationMenu = new JMenu(MessageUtils.getString("application.label.menu.application"));
		this.helpMenu = new JMenu(MessageUtils.getString("application.label.menu.help"));

		this.processManagerItem = new JMenuItem(
				MessageUtils.getString("application.label.menu.application.processmanager"));
		this.exit = new JMenuItem(MessageUtils.getString("application.label.menu.application.exit"));

		this.helpMenuItem = new JMenuItem(MessageUtils.getString("application.label.menu.help"));
		this.aboutMenuItem = new JMenuItem(MessageUtils.getString("application.label.menu.help.about"));
		this.themeMenuItem = new JMenuItem(MessageUtils.getString("application.label.menu.help.theme"));

		this.processManagerItem.setIcon(ImageUtils.getImageByKey("app.icon.processmanager"));
		this.exit.setIcon(ImageUtils.getImageByKey("app.icon.exit"));
		this.helpMenuItem.setIcon(ImageUtils.getImageByKey("app.icon.help"));
		this.aboutMenuItem.setIcon(ImageUtils.getImageByKey("app.icon.about"));
		this.themeMenuItem.setIcon(ImageUtils.getImageByKey("app.icon.theme"));

		this.applicationMenu.add(processManagerItem);
		this.applicationMenu.addSeparator();
		this.applicationMenu.add(exit);

		this.helpMenu.add(helpMenuItem);
		this.helpMenu.add(themeMenuItem);
		this.helpMenu.addSeparator();
		this.helpMenu.add(aboutMenuItem);

		this.containner.addMenu(applicationMenu);
		this.containner.addMenu(helpMenu);

		this.exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (MessageUtils.showConfirmDialog(null, MessageUtils.getString("application.messages.exit.confirm"),
						MessageUtils.getString("application.messages.exit.confirm.title"),
						MessageUtils.INFORMATION_MESSAGE_TYPE)) {
					System.exit(1);
				}
			}
		});

		this.helpMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new HelpView();
			}
		});

		this.themeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LookAndFeelSelector.selectStyle(Main.this.containner);
			}
		});

		this.aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AboutView();
			}
		});

		applicationMenu.setFont(new Font("Verdana", Font.PLAIN, 11));
		helpMenu.setFont(new Font("Verdana", Font.PLAIN, 11));
		exit.setFont(new Font("Verdana", Font.PLAIN, 11));
		processManagerItem.setFont(new Font("Verdana", Font.PLAIN, 11));
		helpMenuItem.setFont(new Font("Verdana", Font.PLAIN, 11));
		aboutMenuItem.setFont(new Font("Verdana", Font.PLAIN, 11));
		themeMenuItem.setFont(new Font("Verdana", Font.PLAIN, 11));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					LookAndFeelSelector.run();
					new Main();
				} catch (Exception e) {
					throw new JMacroException(e);
				}
			}
		});
	}
}