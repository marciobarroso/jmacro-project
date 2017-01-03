package com.icodeuplay.jmacro.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.icodeuplay.jmacro.common.exceptions.JMacroException;

public class Serializator {

	public static final String SERIALIZATOR_KEY = ".ser";

	public static void serialize(Serializable object, String key) {
		try {
			String filename = key.concat(SERIALIZATOR_KEY);
			File file = new File(filename);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.flush();
			oos.close();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			throw new JMacroException(e);
		}
	}

	public static Serializable unserialize(String key) {
		try {
			String filename = key.concat(SERIALIZATOR_KEY);
			File file = new File(filename);
			if (file.exists()) {

				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Serializable obj = (Serializable) ois.readObject();
				ois.close();
				fis.close();
				return obj;

			} else {
				return null;
			}
		} catch (Exception e) {
			throw new JMacroException(e);
		}
	}
}
