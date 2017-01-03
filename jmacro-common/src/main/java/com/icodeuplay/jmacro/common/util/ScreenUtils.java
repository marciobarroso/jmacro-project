package com.icodeuplay.jmacro.common.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

/**
 * Utilities methods for screen handle
 */
public class ScreenUtils {

	public static File screenshot(String path, String filename, String extension) {
		if (path == null)
			path = System.getProperty("java.io.tmpdir");
		if (filename == null)
			filename = new SimpleDateFormat("yyyyMMddHmmS").format(new Date());
		if (extension == null)
			extension = "jpg";

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		Rectangle rectangle = new Rectangle(dim);

		Robot robot = null;
		BufferedImage capturedImage = null;

		try {
			robot = new Robot();
			capturedImage = robot.createScreenCapture(rectangle);
			ImageIO.write(
					capturedImage,
					extension,
					new File(new StringBuilder(path).append(File.separator).append(filename).append(".")
							.append(extension).toString()));
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new File(path + File.separator + filename);
	}

	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static Rectangle getBounds(int width, int height, boolean center) {
		if (width == 0 && height == 0) {
			return new Rectangle(getScreenSize());
		} else if (center) {
			Dimension screen = ScreenUtils.getScreenSize();
			return new Rectangle(screen.width / 2 - width / 2, screen.height / 2 - height / 2, width, height);
		} else {
			return new Rectangle(0, 0, width, height);
		}
	}
}
