package com.icodeuplay.jmacro.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import com.icodeuplay.jmacro.common.exceptions.JMacroException;

/**
 * Utilities methods to handle files
 */
public class FileUtils {

	private static final int BUFFER_SIZE = 4048; // 4kb

	public static final FileFilter ZIP_FILES_FILTER;
	public static final FileFilter ALL_FILES;

	static {
		ZIP_FILES_FILTER = new FileFilter() {
			public boolean accept(File file) {
				if (file.getName().toLowerCase().endsWith(".zip"))
					return true;
				return false;
			}
		};

		ALL_FILES = new FileFilter() {

			public boolean accept(File file) {
				if (file.isDirectory() && file.getName().equals("CVS")
						|| file.getName().startsWith(".")) {
					return false;
				} else {
					return true;
				}
			}
		};
	}

	/**
	 * Write a file
	 * 
	 * @param file
	 *            The file that will be write
	 * @param text
	 *            The content to write in file
	 * @param append
	 *            If <code>true</code> the content will be appended to the
	 *            original file content. If <code>false</code> a new file will
	 *            be generated.
	 * @param breakLine
	 *            If <code>true</code> breaks a line after append the text
	 *            content. If <code>false</code>, just append the text content.
	 * @throws JMacroException
	 */
	public static void writeFile(File file, String text, Boolean append,
			Boolean breakLine) throws JMacroException {
		if (file == null)
			throw new JMacroException("The file cannot be null");

		FileWriter writer = null;
		PrintWriter printer = null;
		try {

			if (!file.exists())
				file.createNewFile();
			if (!file.canWrite())
				throw new JMacroException("The file " + file.getAbsolutePath()
						+ " cannot be write");

			writer = new FileWriter(file, append);
			printer = new PrintWriter(writer, breakLine);

			if (breakLine)
				printer.println(text);
			else
				printer.append(text);

			printer.close();
			writer.close();

		} catch (IOException e) {
			throw new JMacroException("Error writing the file "
					+ file.getAbsolutePath(), e);
		}
	}

	/**
	 * Write a file
	 * 
	 * @param path
	 *            The file system path
	 * @param text
	 *            The content to write in file
	 * @param append
	 *            If <code>true</code> the content will be appended to the
	 *            original file content. If <code>false</code> a new file will
	 *            be generated.
	 * @param breakLine
	 *            If <code>true</code> breaks a line after append the text
	 *            content. If <code>false</code>, just append the text content.
	 * @throws JMacroException
	 */
	public static void writeFile(String path, String text, Boolean append,
			Boolean breakLine) throws JMacroException {
		File file = new File(path);
		writeFile(file, text, append, breakLine);
	}

	/**
	 * Read a file content
	 * 
	 * @param file
	 *            The file that will be read
	 * @return the file content as <code>String</code>
	 * @throws JMacroException
	 */
	public static String readFile(File file) throws JMacroException {
		if (file == null)
			throw new JMacroException("The file cannot be null");
		if (!file.exists())
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " does not exists");
		if (!file.canRead())
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " could not be read");

		FileReader reader = null;
		BufferedReader buffer = null;
		StringBuffer content = null;

		try {
			reader = new FileReader(file);
			buffer = new BufferedReader(reader, 1 * 1024 * 1024);

			content = new StringBuffer();
			String line = null;
			while ((line = buffer.readLine()) != null) {
				if (!content.toString().equals(""))
					content.append("\r"); // Quebra linha caso n�o seja a
				// primeira

				content.append(line);
			}

			reader.close();
			buffer.close();
		} catch (IOException e) {
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " could not be read", e);
		}

		return content.toString();
	}

	/**
	 * Read a file content
	 * 
	 * @param path
	 *            file system path
	 * @return the file content as <code>String</code>
	 * @throws JMacroException
	 */
	public static String readFile(String path) throws JMacroException {
		return FileUtils.readFile(new File(path));
	}

	/**
	 * List files in a directory
	 * 
	 * @param directory
	 *            The directory that will be read
	 * @param recursive
	 *            If <code>true</code> will be list recursively all files into
	 *            the directories
	 * @return All files in directory
	 * @throws JMacroException
	 */
	public static List<File> readDirectory(File directory, Boolean recursive,
			FileFilter filter) throws JMacroException {
		if (directory == null)
			throw new JMacroException("The file cannot be null");
		if (!directory.exists())
			throw new JMacroException("The file " + directory.getAbsolutePath()
					+ " does not exists");
		if (!directory.canRead())
			throw new JMacroException("The file " + directory.getAbsolutePath()
					+ " could not be read");
		if (!directory.isDirectory())
			throw new JMacroException("The file " + directory.getAbsolutePath()
					+ " is not a directory");

		List<File> allFiles = new ArrayList<File>();

		for (File file : directory.listFiles()) {
			if (filter != null) {
				if (filter.accept(file)) {
					if (file.isDirectory()) {
						if (recursive) {
							allFiles.addAll(readDirectory(file, recursive,
									filter));
						} else {
							allFiles.add(file);
						}
					} else {
						allFiles.add(file);
					}
				}
			} else {
				if (file.isDirectory()) {
					if (recursive) {
						allFiles.addAll(readDirectory(file, recursive, filter));
					} else {
						allFiles.add(file);
					}
				} else {
					allFiles.add(file);
				}
			}
		}

		Collections.sort(allFiles, new Comparator<File>() {
			public int compare(File fileOne, File fileTwo) {
				if (fileOne == null || fileTwo == null)
					return 0;
				return fileOne.getAbsolutePath().compareTo(
						fileTwo.getAbsolutePath());
			}
		});

		return allFiles;
	}

	/**
	 * Delete a file
	 * 
	 * @param file
	 *            The file that will be deleted
	 * @return true if the file was successfully delete and false if was not
	 * @throws JMacroException
	 */
	public static Boolean deleteFile(File file) throws JMacroException {
		if (file == null)
			throw new JMacroException("The file cannot be null");
		if (!file.exists())
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " does not exists");
		if (!file.canRead())
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " could not be read");

		return file.delete();
	}

	private static List<File> readFiles(File directory, Boolean recursive,
			Boolean includeDirectories) {
		if (directory == null)
			throw new JMacroException("The directory cannot be null");
		if (!directory.exists())
			throw new JMacroException("The directory "
					+ directory.getAbsolutePath() + " does not exists");
		if (!directory.canRead())
			throw new JMacroException("The directory "
					+ directory.getAbsolutePath() + " could not be read");
		if (!directory.isDirectory())
			throw new JMacroException("The directory "
					+ directory.getAbsolutePath() + " is not a directory");

		List<File> files = new ArrayList<File>();

		for (File file : directory.listFiles()) {
			if (recursive && file.isDirectory()) {
				files.addAll(readFiles(file, recursive, includeDirectories));
				if (includeDirectories)
					files.add(file);
			} else {
				files.add(file);
			}
		}

		return files;
	}

	/**
	 * Delete a directory
	 * 
	 * @param directory
	 *            The directory that will be deleted
	 * @param recursive
	 *            If true, will delete all files into the directory recursively.
	 *            If false, delete only the files into the specified directory.
	 * @return true if the directory was successfully delete and false if was
	 *         not
	 * @throws JMacroException
	 */
	public static Boolean deleteDirectory(File directory, Boolean recursive)
			throws JMacroException {
		if (directory == null)
			throw new JMacroException("The directory cannot be null");
		if (!directory.exists())
			throw new JMacroException("The directory "
					+ directory.getAbsolutePath() + " does not exists");
		if (!directory.canRead())
			throw new JMacroException("The directory "
					+ directory.getAbsolutePath() + " could not be read");
		if (!directory.isDirectory())
			throw new JMacroException("The directory "
					+ directory.getAbsolutePath() + " is not a directory");

		List<File> files = readFiles(directory, recursive, true);

		for (File file : files) {
			deleteFile(file);
		}

		return deleteFile(directory);
	}

	/**
	 * Delete files
	 * 
	 * @param directory
	 * @param recursive
	 * @param filter
	 */
	public static void deleteFiles(File directory, Boolean recursive,
			FileFilter filter) {
		if (directory == null)
			throw new JMacroException("The directory cannot be null");
		List<File> files = FileUtils
				.readDirectory(directory, recursive, filter);

		for (File file : files) {
			file.delete();
		}
	}

	/**
	 * Create a temporary file in java temp files
	 * 
	 * @param name
	 * @return
	 * @throws JMacroException
	 */
	public static File createTempFile(String name) throws JMacroException {
		File file = null;

		try {
			file = new File(System.getProperty("java.io.tmpdir")
					.concat(File.separator).concat(name));
			file.createNewFile();
		} catch (Exception e) {
			throw new JMacroException(e);
		}

		return file;
	}

	/**
	 * Create a temporary directory
	 * 
	 * @param directory
	 *            the directory name
	 * @return a temporary file
	 * @throws JMacroException
	 */
	public static File createTempDir(String directory) throws JMacroException {
		File dir = null;

		try {
			dir = new File(System.getProperty("java.io.tmpdir")
					.concat(File.separator).concat(directory));
			if (dir.exists())
				dir.delete();
			dir.mkdirs();
			dir.deleteOnExit();
		} catch (Exception e) {
			throw new JMacroException(e);
		}

		return dir;
	}

	/**
	 * Create a temp directory
	 * 
	 * @return New temporary file folder
	 * @throws JMacroException
	 */
	public static File createTempDir() throws JMacroException {
		return FileUtils.createTempDir(new SimpleDateFormat("yyyyMMddHmsS")
				.format(new Date()));
	}

	public static File createTempFile() throws JMacroException {
		return FileUtils.createTempFile(new SimpleDateFormat("yyyyMMddHmsS")
				.format(new Date()).concat(".dat"));
	}

	/**
	 * Filter the file list
	 * 
	 * @param files
	 *            List of files
	 * @param filter
	 *            Filter to apply
	 * @return Filtered list of files
	 */
	public static List<File> filterListFiles(List<File> files, FileFilter filter) {
		List<File> filtered = new ArrayList<File>();

		for (File file : files) {
			if (filter.accept(file)) {
				filtered.add(file);
			}
		}

		return filtered;
	}

	/**
	 * Find a file using the filename
	 * 
	 * @param files
	 *            list of files
	 * @param name
	 *            name of needed file
	 * @return the file
	 */
	public static File getFileByName(List<File> files, String name) {
		for (File file : files) {
			if (file.getName().equals(name))
				return file;
		}
		throw new JMacroException("The file '" + name + "' cannot be found");
	}

	public static File downloadFile(String url) {
		String path = System.getProperty("java.io.tmpdir");
		String filename = null;

		if (url != null && !url.endsWith("/"))
			filename = url.substring(url.lastIndexOf("/") + 1, url.length());
		else
			filename = new SimpleDateFormat("yyyyMMddHmmS").format(new Date());

		try {
			BufferedInputStream in = new BufferedInputStream(
					new URL(url).openStream());
			java.io.FileOutputStream fos = new java.io.FileOutputStream(path
					+ File.separator + filename);
			java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,
					1024);
			byte data[] = new byte[1024];
			int count;

			while ((count = in.read(data, 0, 1024)) != -1) {
				bout.write(data, 0, count);
			}

			bout.close();
			fos.close();
			in.close();
		} catch (MalformedURLException e) {
			throw new JMacroException(e);
		} catch (IOException e) {
			throw new JMacroException(e);
		}

		return new File(path + File.separator + filename);
	}

	/**
	 * Returns list of lines
	 * 
	 * @param file
	 * @return
	 * @throws JMacroException
	 */
	public static List<String> readFileLines(File file) throws JMacroException {
		if (file == null)
			throw new JMacroException("The file cannot be null");
		if (!file.exists())
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " does not exists");
		if (!file.canRead())
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " could not be read");

		List<String> lines = new ArrayList<String>();

		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		String line = null;

		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}

			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			throw new JMacroException(e);
		} catch (IOException e) {
			throw new JMacroException(e);
		}

		return lines;
	}

	public static void unzip(File file, File directory) throws JMacroException {
		fileValidation(file);
		fileValidation(directory);

		if (!ZIP_FILES_FILTER.accept(file))
			throw new JMacroException("The file " + directory.getAbsolutePath()
					+ " is not a valid zip file");
		if (!directory.canWrite())
			throw new JMacroException("The directory "
					+ directory.getAbsolutePath() + " could not be writed");

		ZipFile zip = null;
		File tempFile = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[BUFFER_SIZE]; // aprox 20mb

		try {

			zip = new ZipFile(file);
			Enumeration<? extends ZipEntry> e = zip.entries();

			while (e.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) e.nextElement();
				tempFile = new File(directory, entry.getName());

				if (entry.isDirectory() && !tempFile.exists()) {
					tempFile.mkdirs();
					continue;
				}

				if (!tempFile.getParentFile().exists()) {
					tempFile.getParentFile().mkdirs();
				}

				try {

					is = zip.getInputStream(entry);
					os = new FileOutputStream(tempFile);
					int bytesLidos = 0;
					if (is == null) {
						throw new ZipException("Erro ao ler a entrada do zip: "
								+ entry.getName());
					}
					while ((bytesLidos = is.read(buffer)) > 0) {
						os.write(buffer, 0, bytesLidos);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception ex) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception ex) {
						}
					}
				}
			}
		} catch (ZipException e) {
			LoggerFactory.getLogger(FileUtils.class).error(e.getMessage(), e);
		} catch (IOException e) {
			LoggerFactory.getLogger(FileUtils.class).error(e.getMessage(), e);
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static void zip(File file, File directory) throws JMacroException {

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos, BUFFER_SIZE);
			zip(bos, directory);
		} catch (FileNotFoundException e) {
			LoggerFactory.getLogger(FileUtils.class).error(e.getMessage(), e);
		} catch (IOException e) {
			LoggerFactory.getLogger(FileUtils.class).error(e.getMessage(), e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static void zip(File file, List<File> files) throws JMacroException {

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos, BUFFER_SIZE);
			zip(bos, files);
		} catch (FileNotFoundException e) {
			LoggerFactory.getLogger(FileUtils.class).error(e.getMessage(), e);
		} catch (IOException e) {
			LoggerFactory.getLogger(FileUtils.class).error(e.getMessage(), e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static void zip(OutputStream os, File directory)
			throws JMacroException {
		ZipOutputStream zos = null;

		try {
			zos = new ZipOutputStream(os);
			String initialPath = directory.getParent();
			zipSupport(zos, directory, initialPath);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static void zip(OutputStream os, List<File> files)
			throws JMacroException {
		ZipOutputStream zos = null;

		try {
			zos = new ZipOutputStream(os);
			for (int i = 0; i < files.size(); i++) {
				String initialPath = files.get(i).getParent();
				zipSupport(zos, files.get(i), initialPath);
			}
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private static void zipSupport(ZipOutputStream zos, File file,
			String initialPath) throws JMacroException {

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		byte buffer[] = new byte[BUFFER_SIZE];
		try {

			if (file.isDirectory()) {

				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					zipSupport(zos, files[i], initialPath);
				}
			} else {
				String zipPath = null;
				int idx = file.getAbsolutePath().indexOf(initialPath);
				if (idx >= 0) {
					zipPath = file.getAbsolutePath().substring(
							idx + initialPath.length() + 1);
				}
				ZipEntry entrada = new ZipEntry(zipPath);
				zos.putNextEntry(entrada);
				zos.setMethod(ZipOutputStream.DEFLATED);
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis, BUFFER_SIZE);
				int bytesLidos = 0;
				while ((bytesLidos = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					zos.write(buffer, 0, bytesLidos);
				}
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(FileUtils.class).error(e.getMessage(), e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Generic File validation
	 * 
	 * @param file
	 * @throws JMacroException
	 */
	public static void fileValidation(File file) throws JMacroException {
		if (file == null)
			throw new JMacroException("The file cannot be null");
		if (!file.exists())
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " does not exists");
		if (!file.canRead())
			throw new JMacroException("The file " + file.getAbsolutePath()
					+ " could not be read");
	}

	public static byte[] toByteArray(File file) throws JMacroException {
		fileValidation(file);

		try {
			return IOUtils.toByteArray(new FileInputStream(file));
		} catch (Exception e) {
			throw new JMacroException(e);
		}
	}

	public static File fromByteArray(byte[] bytes, String name)
			throws JMacroException {
		try {
			File file = createTempFile(name);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(bytes);
			bos.close();
			return file;
		} catch (Exception e) {
			throw new JMacroException(e);
		}
	}
}