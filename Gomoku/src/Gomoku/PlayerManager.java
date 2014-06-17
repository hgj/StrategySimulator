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

/**
 * Implementation of the {@link StrategySimulator.Library.PlayerManager}, with
 * no extra functionality at all.
 */
public class PlayerManager extends StrategySimulator.Library.PlayerManager
		implements PlayerManagerPlayerInterface {

	protected GameLogic gameLogic;

	protected final Player player;

	protected char playerChar;

	protected char enemyChar;

	/**
	 * The constructor just calls the parent's.
	 * @param superPlayer The {@link StrategySimulator.Library.Player} to be
	 * managed.
	 * @param playerID The unique ID of the player.
	 * @param configuration The extra configuration coming from the game
	 * configuration file.
	 */
	public PlayerManager(StrategySimulator.Library.Player superPlayer, long playerID, Configuration configuration) {
		super(superPlayer, playerID, configuration);
		this.player = (Player) superPlayer;
		logger.debug("EmptyGame.PlayerManager constructed. Stored the player.");
	}

	@Override
	public String getPlayerIdentity() {
		return super.getPlayerIdentity() + "(" + playerChar + ")";
	}

	@Override
	public void setGameLogic(StrategySimulator.Library.GameLogic superGameLogic) {
		super.setGameLogic(superGameLogic);
		gameLogic = (GameLogic) superGameLogic;
	}

	@Override
	public char getPlayerChar() {
		return playerChar;
	}

	@Override
	public int getMapWidth() {
		return gameLogic.width;
	}

	@Override
	public int getMapHeight() {
		return gameLogic.height;
	}

	@Override
	public char getMapAt(int x, int y) {
		return gameLogic.getMapAt(x, y);
	}

	@Override
	public char[][] getWholeMap() {
		return gameLogic.getWholeMap();
	}

	@Override
	public boolean isValidCoordinate(int x, int y) {
		return gameLogic.isValidCoordinate(x, y);
	}

	@Override
	public boolean resetPlayer() {
		if (configuration.get("character") != null) {
			playerChar = configuration.get("character").charAt(0);
		} else {
			return false;
		}
		return super.resetPlayer();
	}

	/**
	 * Step the player. This is a method you should replace with your custom
	 * stepping mechanism.
	 * @return True on success, false otherwise.
	 */
	protected int[] stepPlayer() {
		logger.debug("Stepping the player.");
		return player.step(
				gameLogic.emptyChar,
				playerChar,
				enemyChar);
	}
}
