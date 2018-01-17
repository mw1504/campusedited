package com.sidmeier.campuschaos.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sidmeier.campuschaos.CampusChaos;
import com.sidmeier.campuschaos.utils.Constants;
import com.sun.corba.se.impl.orbutil.closure.Constant;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Campus Chaos";
//		config.width = config.getDesktopDisplayMode().width;
//		config.height = config.getDesktopDisplayMode().height;
		config.width = Constants.APP_WIDTH;
		config.height = Constants.APP_HEIGHT;
		System.out.println(config.getDesktopDisplayMode());
		config.fullscreen = true;
		new LwjglApplication(new CampusChaos(), config);
	}
}
