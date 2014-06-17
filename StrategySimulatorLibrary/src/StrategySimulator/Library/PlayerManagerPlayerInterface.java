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

/**
 * This is the interface of the {@link PlayerManager} provided to the {@link
 * Player}. Everything that a {@link Player} can access should be listed here.
 * NOTE: The JavaDoc talks with the Player here.
 */
public interface PlayerManagerPlayerInterface {

	/**
	 * Get your globally unique ID.
	 * @return Your globally unique ID.
	 */
	public long getPlayerID();

}
