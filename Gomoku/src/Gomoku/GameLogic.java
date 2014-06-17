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

package Gomoku;

import hu.hgj.improvedconfiguration.Configuration;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class GameLogic extends StrategySimulator.Library.GameLogic {

	protected Map<Integer, PlayerManager> playerManagers = new HashMap<>();

	protected Integer width = 0;
	protected Integer height = 0;
	protected char[][] map = null;
	protected char emptyChar = '.';
	protected int nextPlayer = 1;
	protected int winner = 0;

	protected boolean createLog = false;
	protected PrintStream logFileStream = null;

	public GameLogic(Configuration configuration, List<StrategySimulator.Library.PlayerManager> players) {
		super(configuration, players);
		// Copy the references of the StrategySimulator.Library.PlayerManagers
		// into the playerManagers Map, casted into EmptyGame.PlayerManagers.
		for (Integer key : superPlayerManagers.keySet()) {
			playerManagers.put(key, (PlayerManager) superPlayerManagers.get(key));
		}
		logger.debug("Gomoku.GameLogic constructed. Stored a list of playerManagers.");
	}

	@Override
	public String getName() {
		return "Gomoku";
	}

	protected boolean isValidCoordinate(int x, int y) {
		return (x >= 0 && x < width) && (y >= 0 && y < height);
	}

	protected boolean isValidStep(int x, int y) {
		return isValidCoordinate(x, y) && (map[x][y] == emptyChar);
	}

	protected char getMapAt(int x, int y) {
		if (isValidCoordinate(x, y)) {
			return map[x][y];
		} else {
			return emptyChar;
		}
	}

	protected char[][] getWholeMap() {
		return map.clone();
	}

	protected int getPlayerForChar(char c) {
		if (playerManagers.get(1).playerChar == c) {
			return 1;
		} else if (playerManagers.get(2).playerChar == c) {
			return 2;
		} else {
			return 0;
		}
	}

	@Override
	protected boolean reset() {
		// Check the players
		if (playerManagers.size() != 2) {
			logger.error("The Gomoku game should be played by two players!");
			return false;
		}
		// Create the map
		width = configuration.getInteger("game.width");
		height = configuration.getInteger("game.height");
		if (width == null || height == null) {
			logger.error("Mandatory game configuration is missing ('game.width' or 'game.height').");
		}
		if (width < 5 || height < 5) {
			logger.error("Please specify a game map at least 5 by 5!");
			return false;
		}
		map = new char[width][height];
		if (configuration.getBoolean("createlog") == null) {
			createLog = false;
		} else {
			createLog = configuration.getBoolean("createlog");
		}
		logger.debug("Gomoku.GameLogic was reset, game environment constructed.");
		return true;
	}

	@Override
	protected boolean resetAfter() {
		// Check players
		if (playerManagers.get(1).playerChar == playerManagers.get(2).playerChar
				|| playerManagers.get(1).playerChar == ' ' || playerManagers.get(2).playerChar == ' ') {
			logger.error("Could not finish resetting GameLogic, as the players have invalid configuration!");
			return false;
		} else {
			playerManagers.get(1).enemyChar = playerManagers.get(2).playerChar;
			playerManagers.get(2).enemyChar = playerManagers.get(1).playerChar;
		}
		// Create log if neccessary
		if (createLog) {
			File logFile = new File("Gomoku--" + new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss").format(Calendar.getInstance().getTime()) + ".log");
			try {
				logFileStream = new PrintStream(logFile);
				logFileStream.println(width + " " + height);
				logger.info("Created logfile '{}'.", logFile.getAbsolutePath());
			} catch (Exception e) {
				logger.warn("Can not create logfile '{}'.", logFile.getAbsolutePath());
				createLog = false;
			}
		} else {
			logger.info("No logfile is created.");
		}
		return true;
	}

	@Override
	protected boolean initialise() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < height; x++) {
				map[x][y] = emptyChar;
			}
		}
		nextPlayer = 1;
		winner = 0;
		logger.debug("Gomoku.GameLogic was initialised, map is blanked.");
		return true;
	}

	@Override
	protected boolean initialiseAfter() {
		logger.debug("Gomoku.GameLogic was initialised after all players.");
		return true;
	}

	protected int checkMap() {
		// Map axes are: y up, x right
		char lastChar;
		int number;
		// Check for horizontal win
		for (int y = 0; y < height; y++) {
			lastChar = emptyChar;
			number = 0;
			for (int x = 0; x < width; x++) {
				if (lastChar == map[x][y]) {
					++number;
					if (number == 5 && lastChar != emptyChar) {
						return getPlayerForChar(lastChar);
					}
				} else {
					lastChar = map[x][y];
					number = 1;
				}
			}
		}
		// Check for vertical win
		for (int x = 0; x < width; x++) {
			lastChar = emptyChar;
			number = 0;
			for (int y = 0; y < height; y++) {
				if (lastChar == map[x][y]) {
					++number;
					if (number == 5 && lastChar != emptyChar) {
						return getPlayerForChar(lastChar);
					}
				} else {
					lastChar = map[x][y];
					number = 1;
				}
			}
		}
		// Check for \ diagonal win
		for (int sy = 4; sy < height; sy++) {
			lastChar = emptyChar;
			number = 0;
			int x = 0;
			int y = sy;
			while (y >= 0 && x < width) {
				if (lastChar == map[x][y]) {
					++number;
					if (number == 5 && lastChar != emptyChar) {
						return getPlayerForChar(lastChar);
					}
				} else {
					lastChar = map[x][y];
					number = 1;
				}
				--y;
				++x;
			}
		}
		for (int sx = 0; sx < width - 4; sx++) {
			lastChar = emptyChar;
			number = 0;
			int x = sx;
			int y = height - 1;
			while (y >= 0 && x < width) {
				if (lastChar == map[x][y]) {
					++number;
					if (number == 5 && lastChar != emptyChar) {
						return getPlayerForChar(lastChar);
					}
				} else {
					lastChar = map[x][y];
					number = 1;
				}
				--y;
				++x;
			}
		}
		// Check for / diagonal win
		for (int sy = 0; sy < height - 4; sy++) {
			lastChar = emptyChar;
			number = 0;
			int x = 0;
			int y = sy;
			while (y < height && x < width) {
				if (lastChar == map[x][y]) {
					++number;
					if (number == 5 && lastChar != emptyChar) {
						return getPlayerForChar(lastChar);
					}
				} else {
					lastChar = map[x][y];
					number = 1;
				}
				++y;
				++x;
			}
		}
		for (int sx = 1; sx < width - 4; sx++) {
			lastChar = emptyChar;
			number = 0;
			int x = sx;
			int y = 0;
			while (y < height && x < width) {
				if (lastChar == map[x][y]) {
					++number;
					if (number == 5 && lastChar != emptyChar) {
						return getPlayerForChar(lastChar);
					}
				} else {
					lastChar = map[x][y];
					number = 1;
				}
				++y;
				++x;
			}
		}
		// Check for empty places
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map[x][y] == emptyChar) {
					return 0;
				}
			}
		}
		// We did not find any empty place, so it is a draw
		return -1;
	}

	@Override
	protected boolean stepGame() {

		PlayerManager playerManager = playerManagers.get(nextPlayer++);
		if (nextPlayer == 3) {
			nextPlayer = 1;
		}

		int[] result = playerManager.stepPlayer();
		if (createLog) {
			logFileStream.println(result[0] + " " + result[1]);
		}
		if (isValidStep(result[0], result[1])) {
			map[result[0]][result[1]] = playerManager.playerChar;
			logger.debug("Player {} placed its {} to {},{}.", playerManager.getPlayerIdentity(), playerManager.playerChar, result[0], result[1]);
		} else {
			logger.info("Player {} loses as it wants to put to the invalid coordinate {},{}.", playerManager.getPlayerIdentity(), result[0], result[1]);
			winner = nextPlayer;
			return false;
		}

		String drawnMap = "";
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				drawnMap += map[x][y] + " ";
			}
			drawnMap += "\n";
		}
		logger.debug("The map looks like this:\n{}", drawnMap);

		winner = checkMap();
		if (winner != 0) {
			logger.info("Game ended.");
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected boolean finalise() {
		if (winner > 0) {
			logger.info("{} won the game.", playerManagers.get(winner).getPlayerIdentity());
		} else {
			logger.info("The game is a draw.");
		}
		if (createLog) {
			logFileStream.close();
		}
		logger.debug("Gomoku.GameLogic was finalised.");
		return true;
	}

	@Override
	protected boolean finaliseAfter() {
		logger.debug("Gomoku.GameLogic was finalised after all players.");
		return true;
	}

}
