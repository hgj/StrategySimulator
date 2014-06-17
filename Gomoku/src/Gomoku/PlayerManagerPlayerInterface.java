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

/**
 * This interface exports methods to the {@link Player}s, as they receive their
 * {@link PlayerManager} object as this.
 */
public interface PlayerManagerPlayerInterface
		extends StrategySimulator.Library.PlayerManagerPlayerInterface {

	public char getPlayerChar();

	public int getMapWidth();

	public int getMapHeight();

	public char getMapAt(int x, int y);

	public char[][] getWholeMap();

	public boolean isValidCoordinate(int x, int y);

}
