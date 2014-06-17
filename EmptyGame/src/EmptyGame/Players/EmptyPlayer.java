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

package EmptyGame.Players;

import EmptyGame.Player;

/**
 * A {@link Player} implementation, just to make the Simulator work.
 */
public class EmptyPlayer extends Player {

	/**
	 * The implementation of {@link Player#step()}, that does nothing.
	 * @return True.
	 */
	@Override
	protected boolean step() {
		logger.info("It seems that my unique ID is {}.", manager.getPlayerID());
		return true;
	}


}
