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

/**
 * The implementation of the {@link StrategySimulator.Library.Player} class,
 * with basic functionality, to get started with.
 */
public abstract class Player extends StrategySimulator.Library.Player {

	/**
	 * The governing {@link PlayerManager}. The received {@link
	 * StrategySimulator.Library.PlayerManagerPlayerInterface}'s reference is
	 * copied into this member.
	 */
	protected PlayerManagerPlayerInterface manager;

	/**
	 * Can do anything as a regular constructor, but should call the parent's
	 * constructor first. As a rule of thumb, everything that can be moved from
	 * here, should be placed in the {@link #reset()} method.
	 */
	public Player() {
		logger.debug("EmptyGame.Player constructed.");
	}

	/**
	 * Copy the reference of the {@link StrategySimulator.Library.PlayerManagerPlayerInterface}
	 * into {@link #manager} casted into a {@link PlayerManagerPlayerInterface}.
	 * @param superManager The governing {@link PlayerManager}.
	 */
	@Override
	protected void setManager(StrategySimulator.Library.PlayerManagerPlayerInterface superManager) {
		super.setManager(superManager);
		this.manager = (PlayerManagerPlayerInterface) superManager;
		logger.debug("Stored the manager.");
	}

	/**
	 * Basic implementation of the method (that does nothing at all).
	 * @return True.
	 */
	@Override
	protected boolean reset() {
		logger.debug("Reseted.");
		return true;
	}

	/**
	 * Basic implementation of the method (that does nothing at all).
	 * @return True.
	 */
	@Override
	protected boolean initialise() {
		logger.debug("Initialised.");
		return true;
	}

	/**
	 * A custom "step" method, that the children should implement.
	 * @return Whatever.
	 */
	protected abstract boolean step();

	/**
	 * Basic implementation of the method (that does nothing at all).
	 * @return True.
	 */
	@Override
	protected boolean finalise() {
		logger.debug("Finalised.");
		return true;
	}
}
