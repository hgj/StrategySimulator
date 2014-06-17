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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * StrategySimulator main class.
 */
public class Main {

	/**
	 * Version information.
	 */
	public static final String version = "1.6.0";

	private static final int E_OK = 0;
	private static final int E_USER = 1;
	private static final int E_INTERNAL = -1;

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * TODO
	 * @param arguments The arguments passed to the executable.
	 */
	public static void main(String[] arguments) {
		System.out.println("Strategy Simulator " + version);

		if (arguments.length != 1) {
			System.out.println("Usage: StragetySimulator <game-configuration>");
			if (arguments.length > 1) {
				System.err.println("What should I do with the extra argument(s)?");
			}
			System.exit(E_USER);
		}
		File configFile = new File(arguments[0]);

		// Create the Simulator
		Simulator simulator = new Simulator();
		logger.debug("Simulator object constructed, starting simulation.");

		if (simulator.loadGame(configFile)) {
			if (simulator.playGame()) {
				logger.info("Simulation finished. Exiting with code E_OK().", E_OK);
				System.exit(E_OK);
			}
		}
		System.exit(E_INTERNAL);
	}

}
