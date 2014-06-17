//
// StrategySimulator 1.6.0
//
// This file is part of the StrategySimulator framework, licensed under a
// Creative Commons Attribution-ShareAlike 3.0 Unported License.
// To view a copy of this license, see the LICENCE file, or visit
// http://creativecommons.org/licenses/by-nc-sa/4.0/
//
// For more information, visit the project's website at GitHub:
// https://github.com/hgj/StrategySimulator
//

package StrategySimulator;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class JarClassLoader {

	private static HashMap<URL, URLClassLoader> urlClassLoaders = new HashMap<>();

	public static Class loadClassFromJar(URL url, String className) throws ClassNotFoundException {
		// See if we need a URLClassLoader first
		if (!urlClassLoaders.containsKey(url)) {
			urlClassLoaders.put(url, new URLClassLoader(new URL[]{url}));
		}
		return urlClassLoaders.get(url).loadClass(className);
	}

}
