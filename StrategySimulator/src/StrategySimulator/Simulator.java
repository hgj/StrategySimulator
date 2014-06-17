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

import StrategySimulator.Library.GameLogic;
import StrategySimulator.Library.Player;
import StrategySimulator.Library.PlayerManager;
import StrategySimulator.Library.PlayerManagerPlayerInterface;
import hu.hgj.improvedconfiguration.Configuration;
import hu.hgj.improvedconfiguration.ConfigurationParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The heart of the framework, that controls the whole simulation. The class can
 * load a game and control its state and players.
 */
public class Simulator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The loaded {@link StrategySimulator.Library.GameLogic}.
	 */
	private GameLogic gameLogic = null;

	private File gameConfigurationDirectory = null;

	private Configuration gameConfiguration = null;

	private long nextPlayerID = 1;

	/**
	 * Plays the whole game, by calling all the required steps for the game to
	 * simulate successfully.
	 * @return True on success, false otherwise.
	 */
	protected boolean playGame() {
		if (gameLogic != null) {
			logger.info("Starting simulation, resetting GameLogic.");
			if (gameLogic.resetWrapper()) {
				logger.trace("Initialising GameLogic.");
				if (gameLogic.initialiseWrapper()) {
					logger.trace("Stepping simulation.");
					while (gameLogic.stepGameWrapper()) {
						// nothing
					}
				} else {
					logger.error("Failed to initialise GameLogic.");
					return false;
				}
				logger.trace("Game finished, finalising GameLogic.");
				if (!gameLogic.finaliseWrapper()) {
					logger.trace("Failed to finalise GameLogic.");
					return false;
				}
			} else {
				return false;
			}
		} else {
			logger.error("No GameLogic loaded, can not start simulation.");
			return false;
		}
		logger.trace("Simulation successfully ended.");
		return true;
	}

	/**
	 * Load a game from its configuration directory.
	 * @param configFile The directory with all the neccessary game files.
	 * @return True on success, false otherwise.
	 */
	protected boolean loadGame(File configFile) {
		logger.trace("Loading configuration file '{}'.", configFile.getAbsolutePath());
		// Clear current configuration
		gameLogic = null;
		gameConfigurationDirectory = configFile.getParentFile().getAbsoluteFile();
		gameConfiguration = null;
		nextPlayerID = 1;
		// Load the configuration file
		try {
			if (!verifyConfiguration(configFile)) {
				logger.error("Could not load game.");
				return false;
			}
		} catch (IOException e) {
			logger.error("Failed to load game.", e);
			return false;
		}
		// Load all classes and create the objects for the game.
		logger.debug("Loading game classes.");
		try {
			if (!loadClasses()) {
				logger.error("Could not load classes.");
				return false;
			}
		} catch (ClassNotFoundException | IOException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			logger.error("Failed to load classes.", e);
			return false;
		}
		logger.info("'{}' successfully loaded with {} players.", gameLogic.getName(), gameLogic.getNumberOfPlayerManagers());
		// TODO: LOG: List players
		return true;
	}

	private boolean verifyConfiguration(File configFile) throws IOException {

		gameConfiguration = ConfigurationParser.loadConfiguration(configFile);

		if (gameConfiguration.get("game.package") == null || gameConfiguration.get("game.type") == null || gameConfiguration.get("game.path") == null) {
			logger.error("Some mandatory configuration entries ('game.package', 'game.type' or 'game.path') are missing.");
			return false;
		}

		if (!gameConfiguration.get("game.type").equals("jar") && !gameConfiguration.get("game.type").equals("class")) {
			logger.error("Configuration 'game.type' should be one of 'jar' or 'class'.");
			return false;
		}

		HashMap<String, String> playersConfiguration = gameConfiguration.getMatchingEntries("player.[0-9]+.class");
		if (playersConfiguration.isEmpty()) {
			logger.error("No player configuration found.");
			return false;
		}

		String gameName = gameConfiguration.get("game.name");
		if (gameName != null) {
			logger.info("Loaded configuration for '{}'.", gameName);
		} else {
			logger.info("Loaded configuration.");
		}
		return true;
	}

	/**
	 * Loads all the classes from the configuration directory and constructs all
	 * the objects for the game.
	 */
	@SuppressWarnings("unchecked")
	private boolean loadClasses() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

		FileClassLoader fileClassLoader = new FileClassLoader(Simulator.class.getClassLoader());

		//
		// Load the GameLogic class and constructor
		//

		Class<GameLogic> gameLogicClass = null;
		String gameSourcePath = gameConfiguration.get("game.path");
		String gameSourceType = gameConfiguration.get("game.type");
		String gamePackageName = gameConfiguration.get("game.package");
		String gamePackagePath = gamePackageName.replaceAll("\\.", File.separator);
		File gameLogicSourcePath = new File(gameSourcePath);
		if (!gameLogicSourcePath.isAbsolute()) {
			gameLogicSourcePath = new File(gameConfigurationDirectory.getAbsolutePath() + File.separator + gameSourcePath);
		}
		if (gameSourceType.equals("jar")) {
			// The gameLogicSourcePath should be the path to the jar file
			logger.trace("Loading GameLogic's class from {}", gameLogicSourcePath);
			gameLogicClass = JarClassLoader.loadClassFromJar(gameLogicSourcePath.toURI().toURL(), gamePackageName + ".GameLogic");
		} else if (gameSourceType.equals("class")) {
			// The gameLogicSourcePath should be a directory, that contains the class
			String gameLogicClassFilePath = gameLogicSourcePath.getAbsolutePath() + File.separator + gamePackagePath + File.separator + "GameLogic.class";
			logger.trace("Loading GameLogic's class from {}", gameLogicClassFilePath);
			gameLogicClass = fileClassLoader.loadClassFromFile(
					new File(gameLogicClassFilePath)
			);
		} else {
			logger.error("Invalid source type '{}'.", gameSourceType);
			return false;
		}
		Constructor<GameLogic> gameLogicConstructor = gameLogicClass.getConstructor(Configuration.class, List.class);
		logger.debug("Loaded the GameLogic class.");

		//
		// Load the PlayerManager class and constructor
		// and the PlayerManagerPlayerInterface
		//

		Class<PlayerManagerPlayerInterface> playerManagerPlayerInterfaceClass = null;
		if (gameSourceType.equals("jar")) {
			// The gameLogicSourcePath should still be the path to the jar file
			logger.trace("Loading PlayerManagerPlayerInterface's class from {}", gameLogicSourcePath);
			playerManagerPlayerInterfaceClass = JarClassLoader.loadClassFromJar(gameLogicSourcePath.toURI().toURL(),gamePackageName + ".PlayerManagerPlayerInterface");
		} else {
			// The gameLogicSourcePath should still be a directory, that contains the class
			String playerManagerPlayerInterfaceClassFilePath = gameLogicSourcePath.getAbsolutePath() + File.separator + gamePackagePath + File.separator +"PlayerManagerPlayerInterface.class";
			logger.trace("Loading PlayerManagerPlayerInterface's class from {}", playerManagerPlayerInterfaceClassFilePath);
			playerManagerPlayerInterfaceClass = fileClassLoader.loadClassFromFile(
					new File(playerManagerPlayerInterfaceClassFilePath)
			);
		}
		logger.debug("Loaded the PlayerManagerPlayerInterface interface.");

		Class<PlayerManager> playerManagerClass = null;
		if (gameSourceType.equals("jar")) {
			// The gameLogicSourcePath should still be the path to the jar file
			logger.trace("Loading PlayerManager's class from {}", gameLogicSourcePath);
			playerManagerClass = JarClassLoader.loadClassFromJar(gameLogicSourcePath.toURI().toURL(), gamePackageName + ".PlayerManager");
		} else {
			// The gameLogicSourcePath should still be a directory, that contains the class
			String playerManagerClassFilePath = gameLogicSourcePath.getAbsolutePath() + File.separator + gamePackagePath + File.separator + "PlayerManager.class";
			logger.trace("Loading PlayerManager's class from {}", playerManagerClassFilePath);
			playerManagerClass = fileClassLoader.loadClassFromFile(
					new File(playerManagerClassFilePath)
			);
		}
		Constructor<PlayerManager> playerManagerConstructor = null;
		playerManagerConstructor = playerManagerClass.getConstructor(Player.class, long.class, Configuration.class);
		logger.debug("Loaded the PlayerManager class.");

		//
		// Load Player classes, construct the objects and connect them
		// with their managers in an ArrayList
		//

		ArrayList<PlayerManager> playerManagers = new ArrayList<>();

		// Load the base player class
		Class<Player> basePlayerClass = null;
		if (gameSourceType.equals("jar")) {
			// The gameLogicSourcePath should still be the path to the jar file
			logger.trace("Loading PlayerManager's class from {}", gameLogicSourcePath);
			basePlayerClass = JarClassLoader.loadClassFromJar(gameLogicSourcePath.toURI().toURL(), gamePackageName + ".Player");
		} else {
			// The gameLogicSourcePath should still be a directory, that contains the class
			String playerClassFilePath = gameLogicSourcePath.getAbsolutePath() + File.separator + gamePackagePath + File.separator + "Player.class";
			logger.trace("Loading Player's class from {}", playerClassFilePath);
			basePlayerClass = fileClassLoader.loadClassFromFile(
					new File(playerClassFilePath)
			);
		}
		logger.debug("Loaded the Player class.");

		// Load the players from the playerConfigurations
		HashMap<String, Class<Player>> loadedPlayerClasses = new HashMap<>();
		HashMap<String, String> playerClasses = gameConfiguration.getMatchingEntries("player.[0-9]+.class");
		for (String key : playerClasses.keySet()) {
			int playerNumber = Integer.parseInt(key.replaceAll("[^0-9]", ""));
			logger.trace("Loading player number {}.", playerNumber);
			String playerName = gameConfiguration.get("player." + playerNumber + ".name");
			if (playerName == null) {
				playerName = "null";
			}
			String playerClassName = gameConfiguration.get("player." + playerNumber + ".class");
			String playerSourceType = gameConfiguration.get("player." + playerNumber + ".type");
			if (playerSourceType == null) {
				playerSourceType = gameSourceType;
			}
			String playerSourcePath = gameConfiguration.get("player." + playerNumber + ".path");
			if (playerSourcePath == null) {
				if (playerSourceType.equals("class")) {
					playerSourcePath = gameLogicSourcePath.getAbsolutePath() + File.separator + gamePackagePath + File.separator + "Players";
				} else {
					playerSourcePath = gameLogicSourcePath.getAbsolutePath();
				}
			}
			String playerInstancesString = gameConfiguration.get("player." + playerNumber + ".instances");
			if (playerInstancesString == null) {
				playerInstancesString = "1";
			}
			int playerInstances = Integer.parseInt(playerInstancesString);
			logger.trace("Adding {} {} as {}.", playerInstances, playerClassName, playerName);
			// Load the player class
			Class<Player> playerClass = null;
			if (!loadedPlayerClasses.containsKey(playerClassName)) {
				if (playerSourceType.equals("jar")) {
					logger.trace("Loading player's class from {}.", playerSourcePath);
					playerClass = JarClassLoader.loadClassFromJar(new File(playerSourcePath).toURI().toURL(), gamePackageName + ".Players." + playerClassName);
				} else {
					String playerClassFilePath = new File(playerSourcePath).getAbsolutePath() + File.separator + playerClassName + ".class";
					logger.trace("Loading player's class from {}", playerClassFilePath);
					playerClass = fileClassLoader.loadClassFromFile(
							new File(playerClassFilePath)
					);
				}
				loadedPlayerClasses.put(playerClassName, playerClass);
			} else {
				playerClass = loadedPlayerClasses.get(playerClassName);
			}
			// Get the constructor
			Constructor<Player> playerConstructor = null;
			playerConstructor = playerClass.getConstructor();
			for (int i = 1; i <= playerInstances; i++) {
				// Construct the player
				Player player = null;
				player = playerConstructor.newInstance();
				// Construct a PlayerManager for it
				PlayerManager playerManager = null;
				Configuration playerConfiguration = gameConfiguration.getSubset("player." + playerNumber + ".");
				playerManager = playerManagerConstructor.newInstance(player, nextPlayerID, playerConfiguration);
				// Add the player with its manager to the array
				playerManagers.add(playerManager);
				++nextPlayerID;
			}
		}

		//
		// Construct and prepare the GameLogic object
		//
		this.gameLogic = gameLogicConstructor.newInstance(gameConfiguration, playerManagers);
		logger.debug("The GameLogic was constructed with all the game elements.");
		return true;
	}

}
