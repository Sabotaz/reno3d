package fr.limsi.rorqual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

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
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PLAN 3D ENERGY home edition";
		/*config.height = 1200;
		config.width = 1920;
		config.fullscreen = true;*/

        config.height = 720;
        config.width = 1280;
		config.fullscreen = false;

		new LwjglApplication(new MainApplicationAdapter(new ActionResolverImpl()), config);
	}
}
