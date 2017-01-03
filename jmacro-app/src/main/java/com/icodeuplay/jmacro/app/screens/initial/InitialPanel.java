package com.icodeuplay.jmacro.app.screens.initial;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.icodeuplay.jmacro.app.util.ImageUtils;
import com.icodeuplay.jmacro.common.util.MessageUtils;

public class InitialPanel extends JPanel {

	private static final long serialVersionUID = 5834492707708790335L;

	public InitialPanel() {
		setLayout(new BorderLayout(0, 0));

		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setIcon(ImageUtils.getImage(MessageUtils.getString("app.images.about")));
		add(label, BorderLayout.CENTER);
	}

}
