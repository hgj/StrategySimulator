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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The implemented {@link StrategySimulator.Library.GameLogic}, with no added
 * functionality at all. Please see the parent's documentation!
 * @see StrategySimulator.Library.GameLogic
 */
public class GameLogic extends StrategySimulator.Library.GameLogic {

	/**
	 * A new {@link Map} for the {@link PlayerManager}s, so you do not have to
	 * cast the objects every time you want to use them.
	 */
	protected Map<Integer, PlayerManager> playerManagers = new HashMap<>();

	/**
	 * Can do anything as a regular constructor, but should call the parent's
	 * constructor first. As a general rule, everything that can be moved from
	 * here, should be placed in the {@link #reset()} method. That means, you
	 * should also deal with the custom game configuration there.
	 * @param configuration The custom configuration coming from the game
	 * configuration file.
	 * @param players The list of {@link StrategySimulator.Library.PlayerManager}s,
	 * also used by the parent {@link StrategySimulator.Library.GameLogic}.
	 * @see StrategySimulator.Library.GameLogic#GameLogic(String,
	 * java.util.List)
	 */
	public GameLogic(Configuration configuration, List<StrategySimulator.Library.PlayerManager> players) {
		super(configuration, players);
		// Copy the references of the StrategySimulator.Library.PlayerManagers
		// into the playerManagers Map, casted into EmptyGame.PlayerManagers.
		for (Integer key : superPlayerManagers.keySet()) {
			playerManagers.put(key, (PlayerManager) superPlayerManagers.get(key));
		}
		logger.debug("EmptyGame.GameLogic constructed. Stored a list of playerManagers.");
	}

	/**
	 * Return the name of the game.
	 * @return "EmptyGame"
	 */
	@Override
	public String getName() {
		return "EmptyGame";
	}

	/**
	 * Should reset the game environment, as it might be in an unstable {@link
	 * State}. NOTE: The {@link PlayerManager}s are reset by the {@link
	 * StrategySimulator.Library.GameLogic}!
	 * @return True on success, false otherwise.
	 * @see StrategySimulator.Library.GameLogic#reset()
	 */
	@Override
	protected boolean reset() {
		logger.debug("EmptyGame.GameLogic was reset.");
		return true;
	}

	/**
	 * Should check that the {@link PlayerManager}s did a good job on
	 * resetting.
	 * @return True on success, false otherwise.
	 * @see StrategySimulator.Library.GameLogic#resetAfter()
	 */
	@Override
	protected boolean resetAfter() {
		logger.debug("EmptyGame.GameLogic was reset after all players.");
		return true;
	}

	/**
	 * Should prepare the game environment for the simulation. NOTE: The {@link
	 * PlayerManager}s are initialised by the {@link StrategySimulator.Library.GameLogic}!
	 * @return True on success, false otherwise.
	 * @see StrategySimulator.Library.GameLogic#initialise()
	 */
	@Override
	protected boolean initialise() {
		logger.debug("EmptyGame.GameLogic was initialised.");
		return true;
	}

	/**
	 * Should check that the {@link PlayerManager}s did a good job on
	 * initialising.
	 * @return True on success, false otherwise.
	 * @see StrategySimulator.Library.GameLogic#initialiseAfter()
	 */
	@Override
	protected boolean initialiseAfter() {
		logger.debug("EmptyGame.GameLogic was initialised after all players.");
		return true;
	}

	/**
	 * Should evaluate the current round. NOTE: The {@link PlayerManager}s are
	 * NOT stepped by the {@link StrategySimulator.Library.GameLogic}, you have
	 * to "step" them here!
	 * @return False, to stop the game.
	 * @see StrategySimulator.Library.GameLogic#stepGame()
	 */
	@Override
	protected boolean stepGame() {
		boolean playersResult = true;

		for (Iterator<PlayerManager> it = playerManagers.values().iterator(); it.hasNext() && playersResult; ) {
			PlayerManager playerManager = it.next();
			boolean playerResult = playerManager.stepPlayer();
			playersResult &= playerResult;
			if (playerResult) {
				logger.debug("Stepping player {} was successful.", playerManager.getPlayerIdentity());
			} else {
				logger.debug("Stepping player {} failed.", playerManager.getPlayerIdentity());
			}
		}
		// We would return this, but who wants an infinite loop?
		// return playersResult;
		return false;
	}

	/**
	 * Should finalise the game environment. NOTE: The {@link PlayerManager}s
	 * are finalised by the {@link StrategySimulator.Library.GameLogic}!
	 * @return True on success, false otherwise.
	 * @see StrategySimulator.Library.GameLogic#finalise()
	 */
	@Override
	protected boolean finalise() {
		logger.debug("EmptyGame.GameLogic was finalised.");
		return true;
	}

	/**
	 * Should check that the {@link PlayerManager}s did a good job on
	 * finalising.
	 * @return True on success, false otherwise.
	 * @see StrategySimulator.Library.GameLogic#finaliseAfter()
	 */
	@Override
	protected boolean finaliseAfter() {
		logger.debug("EmptyGame.GameLogic was finalised after all players.");
		return true;
	}

}
