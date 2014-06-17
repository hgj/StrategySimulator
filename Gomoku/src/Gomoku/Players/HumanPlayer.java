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

package Gomoku.Players;

import Gomoku.Player;

import java.util.Scanner;

public class HumanPlayer extends Player {

	protected static Scanner inputScanner = new Scanner(System.in);

	@Override
	protected int[] step(char empty, char you, char enemy) {
		int[] result = new int[2];
		// Print the map
		System.err.flush();
		System.out.flush();
		for (int y = manager.getMapHeight() - 1; y >= 0; y--) {
			if (y > 9) {
				System.out.print(y + " ");
			} else {
				System.out.print(y + "  ");
			}
			for (int x = 0; x < manager.getMapWidth(); x++) {
				System.out.print(manager.getMapAt(x, y) + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.print("   ");
		for (int x = 0; x < manager.getMapWidth(); x++) {
			if (x > 9) {
				System.out.print(x);
			} else {
				System.out.print(x + " ");
			}
		}
		System.out.println();
		System.out.print("Where do you want to place your " + manager.getPlayerChar() + "? ");
		try {
			Scanner answerScanner = new Scanner(inputScanner.nextLine());
			result[0] = answerScanner.nextInt();
			result[1] = answerScanner.nextInt();
		} catch (Exception e) {
			logger.error(e.toString());
			logger.error("Could not read the answer! Returning some bullshit to the manager.");
			result[0] = result[1] = -1;
			return result;
		}
		logger.info("Human says we should place our {} at {},{}.", manager.getPlayerChar(), result[0], result[1]);
		return result;
	}


}
