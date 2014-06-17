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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A custom {@link ClassLoader} that loads a {@link Class} from a {@link File}.
 */
public class FileClassLoader extends ClassLoader {

	/**
	 * Constructor that calls the parent's constructor.
	 * @param cl A reference to the {@link ClassLoader} that defined it.
	 */
	public FileClassLoader(ClassLoader cl) {
		super(cl);
	}

	/**
	 * Loads a {@link Class} from the given {@link File}.
	 * @param file The {@link File} to load the {@link Class} from.
	 * @return The loaded {@link Class}.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Class loadClassFromFile(File file) throws FileNotFoundException, IOException {
		// Read in the bytes from the file
		FileInputStream fis = new FileInputStream(file);
		byte bytes[] = new byte[(int) file.length()];
		fis.read(bytes);
		fis.close();
		// Define and then return the class
		return defineClass(null, bytes, 0, bytes.length);
	}

}
