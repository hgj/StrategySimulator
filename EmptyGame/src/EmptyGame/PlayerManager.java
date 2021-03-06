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

package EmptyGame;

import hu.hgj.improvedconfiguration.Configuration;

/**
 * Implementation of the {@link StrategySimulator.Library.PlayerManager}, with
 * no extra functionality at all.
 */
public class PlayerManager extends StrategySimulator.Library.PlayerManager
		implements PlayerManagerPlayerInterface {

	/**
	 * Holds a reference for the received {@link StrategySimulator.Library.GameLogic}.
	 */
	protected GameLogic gameLogic;

	/**
	 * Holds a reference for the received {@link StrategySimulator.Library.Player}.
	 */
	protected final Player player;

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

	/**
	 * Call the overridden method, and set the local {@link #gameLogic}
	 * reference.
	 * @param superGameLogic The received GameLogic.
	 */
	@Override
	public void setGameLogic(StrategySimulator.Library.GameLogic superGameLogic) {
		super.setGameLogic(superGameLogic);
		gameLogic = (GameLogic) superGameLogic;
	}

	/**
	 * Step the player. This is a method you should replace with your custom
	 * stepping mechanism.
	 * @return True on success, false otherwise.
	 */
	protected boolean stepPlayer() {
		logger.debug("Stepping the player.");
		return player.step();
	}
}
