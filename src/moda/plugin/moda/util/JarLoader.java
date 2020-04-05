package moda.plugin.moda.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.bukkit.plugin.Plugin;

@Deprecated
public class JarLoader {

	private static final Method ADD_URL_METHOD;

	static {
		try {
			ADD_URL_METHOD = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			ADD_URL_METHOD.setAccessible(true);
		} catch (final NoSuchMethodException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private final URLClassLoader classLoader;

	public JarLoader(final Plugin plugin) throws IllegalStateException {
		final ClassLoader classLoader = plugin.getClass().getClassLoader();
		if (classLoader instanceof URLClassLoader) {
			this.classLoader = (URLClassLoader) classLoader;
		} else {
			throw new IllegalStateException("ClassLoader is not instance of URLClassLoader");
		}
	}

	public void loadJar(final File file) {
		try {
			ADD_URL_METHOD.invoke(this.classLoader, file.toURI().toURL());
		} catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
