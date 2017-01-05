package com.icodeuplay.jmacro.app;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.icodeuplay.jmacro.app.common.AbstractSystemTrayApplication;
import com.icodeuplay.jmacro.app.util.ImageUtils;
import com.icodeuplay.jmacro.app.util.LookAndFeelSelector;
import com.icodeuplay.jmacro.common.exceptions.JMacroException;
import com.icodeuplay.jmacro.common.util.FileUtils;
import com.icodeuplay.jmacro.common.util.MessageUtils;

/**
 * Application main class
 */
public class Main extends AbstractSystemTrayApplication {

	private MenuItem record;
	private MenuItem run;
	private MenuItem settings;

	public void populateMenu() {
		this.record = new MenuItem(MessageUtils.getString("app.label.new"));
		this.addApplicationMenuItem(record);

		record.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// new 
				if( record.getLabel().equals(MessageUtils.getString("app.label.new")) ) {
					
					// set label stop
					record.setLabel(MessageUtils.getString("app.label.stop"));
					ImageIcon image = ImageUtils.getImage(MessageUtils.getString("app.icon.recording"));
					String message = MessageUtils.getString("app.messages.recording.macro");
					setApplicationIcon(image, message);					
				
				} else { // stop
					
					record.setLabel(MessageUtils.getString("app.label.new"));
					setDefaultApplicationIcon();
					
				}
			}
		});
		
		this.run = new MenuItem(MessageUtils.getString("app.label.run"));
		this.addApplicationMenuItem(run);

		this.settings = new MenuItem(MessageUtils.getString("app.label.settings"));
		this.addApplicationMenuItem(settings);

		MenuItem help = new MenuItem(MessageUtils.getString("app.label.help"));
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String help = FileUtils.createTempDir(MessageUtils.getString("app.id") + File.separator + "help")
							.getAbsolutePath() + File.separator;
					Runtime.getRuntime().exec("hh.exe " + help + "index.html");
				} catch (Exception e) {
				}
			}
		});
		this.addSystemMenuItem(help);

	}

	@Override
	public void beforeRun() {
		try {
			// copy help content to temp folder
			File helpDir = FileUtils.createTempDir(MessageUtils.getString("app.id") + File.separator + "help");
			File source = new File(getClass().getResource("/help").toURI());
			FileUtils.copyDirectory(source, helpDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
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