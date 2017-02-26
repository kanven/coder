package com.kanven.tools.code.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;

import com.kanven.tools.code.Constant;

/**
 * 拓展加载器
 * 
 * @author kanven
 *
 * @param <T>
 */
public class ExtensionLoader<T> {

	private static final String CODER_DIRECTORY = "META-INF/coder/";

	private static final String CODER_INTERNAL_DIRECTORY = CODER_DIRECTORY + "internal/";

	private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();

	private Class<T> type;

	private Map<String, Holder> holders = new ConcurrentHashMap<String, Holder>();

	private Lock lock = new ReentrantLock();

	public ExtensionLoader(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("拓展接口不能为空！");
		}
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException("拓展类型不时接口类型！");
		}
		ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(clazz);
		if (loader == null) {
			EXTENSION_LOADERS.putIfAbsent(clazz, new ExtensionLoader<T>(clazz));
			loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(clazz);
		}
		return loader;
	}

	@SuppressWarnings("unchecked")
	public T getEntity(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("拓展名不能为空！");
		}
		Holder holder = holders.get(name);
		if (holder == null) {
			holders.putIfAbsent(name, new Holder());
			holder = holders.get(name);
		}
		Object entity = holder.getEntity();
		if (entity == null) {
			lock.lock();
			try {
				Map<String, Class<?>> clazzs = new HashMap<String, Class<?>>();
				loadFiles(clazzs, ExtensionLoader.CODER_INTERNAL_DIRECTORY);
				Set<String> keys = clazzs.keySet();
				for (String key : keys) {
					Class<?> clazz = clazzs.get(key);
					Object o = null;
					try {
						o = clazz.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new IllegalArgumentException("【" + clazz.getName() + "类初始化出现异常！】", e);
					}
					Holder container = new Holder();
					container.setEntity(o);
					holders.put(key, container);
				}
				entity = holders.get(name).getEntity();
			} finally {
				lock.unlock();
			}
		}
		return (T) entity;
	}

	private void loadFiles(Map<String, Class<?>> clazzs, String dir) {
		String path = dir + type.getSimpleName();
		Enumeration<URL> urls = null;
		ClassLoader loader = findClassLoader();
		try {
			if (loader != null) {
				urls = loader.getResources(path);
			} else {
				urls = ClassLoader.getSystemResources(path);
			}
			if (urls != null) {
				while (urls.hasMoreElements()) {
					URL url = urls.nextElement();
					loadFile(clazzs, url.openStream(), loader);
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("文件读取失败！", e);
		}
	}

	private void loadFile(Map<String, Class<?>> clazzs, InputStream input, ClassLoader loader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, Constant.DEFAULT_CHARSET));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (StringUtils.isBlank(line)) {
				continue;
			}
			if (line.startsWith("#")) {
				continue;
			}
			String content = line;
			int index = line.indexOf("#");
			if (index != -1) {
				content = line.substring(0, index);
			}
			content = content.trim();
			int pos = content.indexOf("=");
			if (pos == -1) {
				continue;
			}
			String name = content.substring(0, pos).trim();
			String className = content.substring(pos + 1).trim();
			try {
				Class<?> clazz = Class.forName(className);
				if (!type.isAssignableFrom(clazz)) {
					throw new IllegalStateException("【" + className + "】不是【" + type.getName() + "】实现类");
				}
				clazzs.putIfAbsent(name, clazz);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("【" + className + "】类加载失败！");
			}
		}
	}

	private ClassLoader findClassLoader() {
		return ExtensionLoader.class.getClassLoader();
	}

	private static class Holder {

		private Object entity;

		public Holder() {

		}

		public Object getEntity() {
			return entity;
		}

		public void setEntity(Object entity) {
			this.entity = entity;
		}

	}

}
