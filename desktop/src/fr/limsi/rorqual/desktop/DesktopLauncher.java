package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import fr.limsi.rorqual.core.utils.analytics.Action;
import fr.limsi.rorqual.core.utils.analytics.ActionResolver;
import fr.limsi.rorqual.core.utils.analytics.Category;
import fr.limsi.rorqual.core.view.MainApplicationAdapter;

public class DesktopLauncher {

	static class ActionResolverImpl implements ActionResolver {
		public ActionResolverImpl() { };
		@Override
		public void setTrackerScreenName(String path) {	}

		@Override
		public void sendTrackerEvent(Category category, Action action) { }

		@Override
		public void sendTrackerEvent(Category category, Action action, String label) { }

		@Override
		public void sendTrackerEvent(Category category, Action action, long value) { }

		@Override
		public void sendTrackerEvent(Category category, Action action, String label, long value) { }

		@Override
		public void sendTiming(Category category, long value) { }

		@Override
		public void sendTiming(Category category, long value, String name) { }

		@Override
		public void sendTiming(Category category, long value, String name, String label) { }

		@Override
		public void sendEmail(String subject) { }
	}

	public static void main (String[] arg) {
        String id = "debug";
        int version = 0;
        if (arg.length >= 1)
            version = Integer.parseInt(arg[0]);
        if (arg.length >= 2)
            id = arg[1];
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PLAN 3D ENERGY home edition";
		/*config.height = 1200;
		config.width = 1920;
		config.fullscreen = true;*/
		if (id.equals("debug")) {
			config.height = 720;
			config.width = 1200;
			config.fullscreen = false;
		} else {
			config.height = 1080;
			config.width = 1920;
			config.fullscreen = true;
		}
		new LwjglApplication(new MainApplicationAdapter(new ActionResolverImpl(), version, id), config);
	}
}
