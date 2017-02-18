package com.icodeuplay.jmacro.app;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.util.Timer;
import java.util.TimerTask;

public final class ClickMouse extends TimerTask {
	public static int x, y, d;

	public static void main(String[] args) {
		TimerTask clikMouse = new ClickMouse();
		Timer t = new Timer();
		t.schedule(clikMouse, 100,100);
	}

	public void run() {
		int x = 0;
		int y = 0;

		PointerInfo pointInfo = MouseInfo.getPointerInfo();
		Point point = pointInfo.getLocation();

		if (x != (int) point.getX() || y != (int) point.getY()) {
			x = (int) point.getX();
			y = (int) point.getY();
			System.out.println("Coordinates: " + x + "x" + y);
		}
	}
}