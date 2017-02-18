package com.icodeuplay.jmacro.app;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class Test {

	public static void main(String[] args) throws Exception {
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		int x = (int) b.getX();
		int y = (int) b.getY();
		System.out.print(y + "jjjjjjjjj");
		System.out.print(x);
	}

}
