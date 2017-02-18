package com.icodeuplay.jmacro.app.screens.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.icodeuplay.jmacro.app.util.ImageUtils;
import com.icodeuplay.jmacro.common.util.MessageUtils;
import com.icodeuplay.jmacro.common.util.ScreenUtils;

/**
 * <p>
 * Generic About View
 * </p>
 * 
 * <p>
 * Required informations set on messages.properties:
 * </p>
 * <ul>
 * <li>app.title</li>
 * <li>app.icon.64</li>
 * <li>app.images.about</li>
 * <li>app.label.version</li>
 * <li>app.label.created</li>
 * <li>app.messages.about.copyrights</li>
 * <li>app.messages.about.vendor</li>
 * </ul>
 */
public class AboutView extends JFrame {

	private static final long serialVersionUID = -3855916125984276563L;

	private JPanel northPanel;
	private JPanel infoPanel;
	private JPanel logoPanel;
	private JPanel southPanel;

	private JLabel labelImage;
	private JLabel labelTitle;
	private JLabel labelVersion;
	private JLabel labelVersionValue;
	private JLabel labelCreated;
	private JLabel labelCreatedValue;
	private JLabel labelCopyrights;
	private JLabel labelVendor;

	private JButton buttonOK;

	public AboutView() {
		getContentPane().setLayout(null);

		this.setBounds(ScreenUtils.getBounds(573, 310, true));
		this.setFont(new Font("Verdana", Font.PLAIN, 11));
		ImageUtils.setAppIcons(this);
		this.setTitle(MessageUtils.getString("app.title"));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.initComponents();

		this.setResizable(false);
		this.setVisible(true);
	}

	private void initComponents() {
		northPanel = new JPanel();
		northPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		northPanel.setBackground(Color.WHITE);
		northPanel.setBounds(0, 0, 566, 237);
		northPanel.setLayout(null);
		getContentPane().add(northPanel);

		logoPanel = new JPanel();
		logoPanel.setBackground(Color.WHITE);
		logoPanel.setBounds(10, 11, 230, 215);
		logoPanel.setLayout(new BorderLayout(0, 0));
		northPanel.add(logoPanel);

		infoPanel = new JPanel();
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBounds(250, 11, 306, 215);
		infoPanel.setLayout(null);
		northPanel.add(infoPanel);

		southPanel = new JPanel();
		southPanel.setBounds(0, 233, 566, 49);
		southPanel.setLayout(null);
		southPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		getContentPane().add(southPanel);

		labelImage = new JLabel("");
		labelImage.setIcon(ImageUtils.getImage(MessageUtils.getString("app.images.about")));
		labelImage.setHorizontalAlignment(SwingConstants.CENTER);
		logoPanel.add(labelImage, BorderLayout.CENTER);

		labelTitle = new JLabel(MessageUtils.getString("app.title"));
		labelTitle.setForeground(new Color(30, 144, 255));
		labelTitle.setBounds(10, 11, 286, 14);
		labelTitle.setFont(new Font("Verdana", Font.PLAIN, 11));
		infoPanel.add(labelTitle);

		labelVersion = new JLabel(MessageUtils.getString("app.label.version"));
		labelVersion.setFont(new Font("Verdana", Font.PLAIN, 11));
		labelVersion.setBounds(10, 49, 59, 14);
		infoPanel.add(labelVersion);

		labelVersionValue = new JLabel(MessageUtils.getString("app.version"));
		labelVersionValue.setFont(new Font("Verdana", Font.PLAIN, 11));
		labelVersionValue.setBounds(79, 49, 217, 14);
		infoPanel.add(labelVersionValue);

		labelCreated = new JLabel(MessageUtils.getString("app.label.created"));
		labelCreated.setFont(new Font("Verdana", Font.PLAIN, 11));
		labelCreated.setBounds(10, 74, 59, 14);
		infoPanel.add(labelCreated);

		labelCreatedValue = new JLabel(MessageUtils.getString("app.created"));
		labelCreatedValue.setFont(new Font("Verdana", Font.PLAIN, 11));
		labelCreatedValue.setBounds(79, 74, 217, 14);
		infoPanel.add(labelCreatedValue);

		labelVendor = new JLabel(MessageUtils.getString("app.messages.about.vendor"));
		labelVendor.setFont(new Font("Verdana", Font.PLAIN, 11));
		labelVendor.setVerticalAlignment(SwingConstants.BOTTOM);
		labelVendor.setBounds(10, 168, 286, 36);
		infoPanel.add(labelVendor);

		labelCopyrights = new JLabel(MessageUtils.getString("app.messages.about.copyrights"));
		labelCopyrights.setVerticalAlignment(SwingConstants.TOP);
		labelCopyrights.setFont(new Font("Verdana", Font.PLAIN, 11));
		labelCopyrights.setBounds(10, 99, 286, 55);
		infoPanel.add(labelCopyrights);

		buttonOK = new JButton("OK");
		buttonOK.setFont(new Font("Verdana", Font.PLAIN, 11));
		buttonOK.setBounds(467, 15, 89, 23);
		southPanel.add(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonOK();
			}
		});
	}

	private void buttonOK() {
		this.setVisible(false);
	}
}
