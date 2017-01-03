package com.icodeuplay.jmacro.app.common;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.icodeuplay.jmacro.app.util.ImageUtils;
import com.icodeuplay.jmacro.common.util.MessageUtils;
import com.icodeuplay.jmacro.common.util.ScreenUtils;

/**
 * Application base frame
 */
public class ApplicationFrame extends JFrame {

	private static final long serialVersionUID = 2670270713657572297L;

	private static ApplicationFrame INSTANCE;
	
	private JMenuBar containnerMenu;
	
	private static final int MENU_POSITION_X = 0;
	private static final int MENU_POSITION_Y = 0;
	private static final int MENU_WIDTH = 784;
	private static final int MENU_HEIGHT = 21;	
	
	private ApplicationFrame(Rectangle bounds) {
		super(MessageUtils.getString("application.title"));
		
		ImageUtils.setAppIcons(this);
		this.setPreferredSize(new Dimension(bounds.width, bounds.height));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.getContentPane().setLayout(new BorderLayout());
        this.setBounds(ScreenUtils.getBounds(bounds.width, bounds.height, true));
        
        this.containnerMenu = new JMenuBar();
        this.containnerMenu.setBounds(MENU_POSITION_X, MENU_POSITION_Y, MENU_WIDTH, MENU_HEIGHT);
        this.setJMenuBar(this.containnerMenu);       
        
        this.addWindowListener(new WindowListener() {
        	public void windowOpened(WindowEvent e) { }
			
			public void windowIconified(WindowEvent e) { }
			
			public void windowDeiconified(WindowEvent e) { }
			
			public void windowDeactivated(WindowEvent e) { }
			
			public void windowClosing(WindowEvent e) { 
				if( MessageUtils.showConfirmDialog(null, MessageUtils.getString("application.messages.exit.confirm"), MessageUtils.getString("application.messages.exit.confirm.title"), MessageUtils.INFORMATION_MESSAGE_TYPE)) {
					System.exit(1);
				}
			}
			
			public void windowClosed(WindowEvent e) { }
			
			public void windowActivated(WindowEvent e) { }
		});
        
        this.setResizable(false);
        this.setVisible(true);
	}
	
	public static ApplicationFrame getInstance(Rectangle bounds) {
		if( INSTANCE == null ) INSTANCE = new ApplicationFrame(bounds);
		return INSTANCE;
	}
	
	public void addPanel(final JPanel panel) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				getContentPane().removeAll();
				getContentPane().add(panel, BorderLayout.CENTER);
				validate();
				repaint();
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}
	
	protected void reset() {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				getContentPane().removeAll();
				validate();
				repaint();
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}
	
	public void addMenu(JMenu menu) {
		this.containnerMenu.add(menu);
	}
}