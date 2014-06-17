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

public abstract class Player extends StrategySimulator.Library.Player {

	protected PlayerManagerPlayerInterface manager;

	public Player() {
		logger.debug("EmptyGame.Player constructed.");
	}

	@Override
	protected void setManager(StrategySimulator.Library.PlayerManagerPlayerInterface superManager) {
		super.setManager(superManager);
		this.manager = (PlayerManagerPlayerInterface) superManager;
		logger.debug("Stored the manager.");
	}

	@Override
	protected boolean reset() {
		logger.debug("Reseted.");
		return true;
	}

	@Override
	protected boolean initialise() {
		logger.debug("Initialised.");
		return true;
	}

	protected abstract int[] step(char empty, char you, char enemy);

	@Override
	protected boolean finalise() {
		logger.debug("Finalised.");
		return true;
	}
}
