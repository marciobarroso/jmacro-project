package com.icodeuplay.jmacro.app.screens.help;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.icodeuplay.jmacro.common.util.FileUtils;
import com.icodeuplay.jmacro.common.util.MessageUtils;

/**
 * <p>
 * Generic Help Panel
 * </p>
 * 
 * <p>
 * Requirements:
 * </p>
 * 
 * <p>
 * Defines the help directory in application classpath
 * </p>
 * 
 */
public class HelpView implements Serializable {

	private static final long serialVersionUID = -4207885576799019733L;

	public HelpView() {
		try {
			CodeSource src = this.getClass().getProtectionDomain().getCodeSource();
			List<String> resources = new ArrayList<String>();

			if (src != null) {
				URL jar = src.getLocation();
				ZipInputStream zip = new ZipInputStream(jar.openStream());
				ZipEntry entry = null;
				String name = null;

				while ((entry = zip.getNextEntry()) != null) {
					name = entry.getName();
					// add all files under help directory
					if (name.startsWith("help")) {
						if (name.endsWith("/"))
							resources.add(name.substring(0, name.length() - 1));
						else
							resources.add(name);
					}
				}
			}

			File directory = FileUtils.createTempDir(MessageUtils.getString("application.title"));

			// not jar
			if (resources.size() == 0) {
				String path = "help";
				URL url = getClass().getResource(path) == null ? ClassLoader.getSystemResource(path) : getClass()
						.getResource(path);
				org.apache.commons.io.FileUtils.copyDirectory(new File(url.getFile()),
						new File(directory.getAbsolutePath() + File.separator + "help"));
			} else {
				URL url = null;
				File file = null;
				for (String resource : resources) {
					url = getClass().getResource(resource) == null ? ClassLoader.getSystemResource(resource)
							: getClass().getResource(resource);
					file = new File(directory.getAbsolutePath() + File.separator + resource);

					if (!file.exists()) {
						if (resource.indexOf(".") > -1) { // is file
							file.createNewFile();
							org.apache.commons.io.FileUtils.copyURLToFile(url, file);
						} else {
							file.mkdirs();
						}
					}
				}
			}

			Runtime.getRuntime().exec("hh.exe " + directory.getAbsolutePath() + "/help/index.html");
		} catch (Exception e) {
			MessageUtils.showMessage(null, "application.messages.help.error", MessageUtils.ERROR_MESSAGE_TYPE);
		}
	}
}