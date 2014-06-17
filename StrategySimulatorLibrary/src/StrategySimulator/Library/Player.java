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

package StrategySimulator.Library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of the {@link Player} class, that provides basic
 * functionality for the {@link PlayerManager} and the {@link GameLogic}.
 */
public abstract class Player {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The governing {@link PlayerManager}. NOTE: This can not be final, as you
	 * can not connect the Player with the PlayerManager at the same time (in
	 * the constructors of both).
	 */
	protected PlayerManagerPlayerInterface superManager;

	/**
	 * Construct the player.
	 */
	public Player() {
		logger.debug("StrategySimulator.Library.Player constructed.");
	}

	/**
	 * Set the {@link PlayerManager} that governs this {@link Player} class.
	 * @param superManager The governing {@link PlayerManager}
	 */
	protected void setManager(PlayerManagerPlayerInterface superManager) {
		this.superManager = superManager;
		logger.debug("Stored the superManager.");
	}

	/**
	 * Get the name of this {@link Player}.
	 * @return The name of this {@link Player}, defaults to the name of the
	 * Class.
	 */
	protected String getName() {
		return this.getClass().getSimpleName();
	}

	//
	// Abstract methods to implement
	//

	/**
	 * Should reset the {@link Player} to its new-born state.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean reset();

	/**
	 * Should initiase the {@link Player}, so the simulation can begin
	 * afterwars.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean initialise();

	/**
	 * Should finalise the {@link Player} after the simulation is ended.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean finalise();

}
