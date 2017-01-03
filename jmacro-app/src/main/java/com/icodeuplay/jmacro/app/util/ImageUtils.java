/**
 * Project: core
 * Package: corporate.tools.fragmental.core.util
 */
package com.icodeuplay.jmacro.app.util;

import java.awt.Image;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.icodeuplay.jmacro.common.util.MessageUtils;

/**
 * Utilities methods to handle images
 */
public class ImageUtils implements Serializable {

	private static final long serialVersionUID = -5360104482853021299L;

	public static ImageIcon getImage(String resource) {
		try {
			return new ImageIcon(ImageUtils.class.getResource(resource));
		} catch (Exception e) {
			return new ImageIcon(ClassLoader.getSystemResource(resource));
		}
	}
	
	public static ImageIcon getImageByKey(String key) {
		return getImage(MessageUtils.getString(key));
	}
	
	public static void setAppIcons(JFrame frame) {
		List<Image> images = new ArrayList<Image>();
		images.add(getImage(MessageUtils.getString("app.icon.32")).getImage());
		images.add(getImage(MessageUtils.getString("app.icon.16")).getImage());
		
		try {
			Class<?> [] types = {java.util.List.class};
			Method method = Class.forName("java.awt.Window").getDeclaredMethod("setIconImages", types);
			
			Object [] parameters = {images};
			method.invoke(frame, parameters);
		} catch (Exception e) {
			frame.setIconImage(images.get(0));
		}		
	}
	
	public static ImageIcon getAppIcon() {
		return getImage(MessageUtils.getString("application.images.icon.app"));
	}
	
	public static ImageIcon getOnlineIcon() {
		return getImage(MessageUtils.getString("application.images.icon.online"));
	}
	
	public static ImageIcon getOfflineIcon() {
		return getImage(MessageUtils.getString("application.images.icon.offline"));
	}
}
