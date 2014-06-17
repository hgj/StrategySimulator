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

import hu.hgj.improvedconfiguration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of the {@link PlayerManager} class, that provides
 * basic functionality towards the {@link Player} and the {@link GameLogic}.
 */
public abstract class PlayerManager implements PlayerManagerPlayerInterface {

	/**
	 * The {@link Logger} for the object.
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The {@link GameLogic} this PlayerManager belongs to.
	 */
	protected GameLogic superGameLogic;

	/**
	 * The {@link Player}, that is managed by this {@link PlayerManager}
	 */
	protected final Player superPlayer;

	/**
	 * The globally unique ID of the Player.
	 */
	public final long playerID;

	/**
	 * The custom configuration extracted from the game configuration file.
	 */
	public final Configuration configuration;

	/**
	 * Constructs the {@link PlayerManager} and connects the {@link Player} to
	 * the game.
	 * @param superPlayer The {@link Player} to be managed by this instance.
	 * @param playerID The unique ID of the player.
	 * @param configuration The extra configuration string for the player.
	 */
	public PlayerManager(Player superPlayer, long playerID, Configuration configuration) {
		this.superPlayer = superPlayer;
		this.playerID = playerID;
		this.configuration = configuration;
		logger.debug("StrategySimulator.Library.PlayerManager constructed. Stored the superPlayer, calling superPlayer.setManager().");
		this.superPlayer.setManager(this);
	}

	/**
	 * Set the received GameLogic.
	 * @param superGameLogic The received GameLogic.
	 */
	public void setGameLogic(GameLogic superGameLogic) {
		this.superGameLogic = superGameLogic;
	}

	/**
	 * Basic pull method, that gets the {@link Player}'s name, by calling the
	 * {@link Player}'s {@link Player#getName()} method.
	 * @return The {@link #superPlayer}'s name.
	 */
	public String getPlayerName() {
		return superPlayer.getName();
	}

	/**
	 * Returns the player's name, with its ID.
	 * @return {@link #playerID}:{@link #getPlayerName()}
	 */
	public String getPlayerIdentity() {
		return playerID + ":" + getPlayerName();
	}

	/**
	 * Getter for the {@link #playerID} field.
	 * @return {@link #playerID}
	 */
	@Override
	public long getPlayerID() {
		return playerID;
	}

	/**
	 * Calls the {@link Player}'s {@link Player#reset()} method. Should reset
	 * the {@link Player} through it's mentioned method, and also the {@link
	 * PlayerManager} object itself. Implementations can do other things here,
	 * but should obey the return mechanism: True on success, false otherwise.
	 * @return The value that the {@link Player#reset()} method returns.
	 */
	public boolean resetPlayer() {
		logger.debug("StrategySimulator.Library.PlayerManager resetting player.");
		return superPlayer.reset();
	}

	/**
	 * Calls the {@link Player}'s {@link Player#initialise()} method. Should
	 * initialise the {@link Player} through it's mentioned method, and also
	 * the {@link PlayerManager} object itself. Implementations can do other
	 * things here, but should obey the return mechanism: True on success, false
	 * otherwise.
	 * @return The value that the {@link Player#initialise()} method returns.
	 */
	public boolean initialisePlayer() {
		logger.debug("StrategySimulator.Library.PlayerManager initialising player.");
		return superPlayer.initialise();
	}

	/**
	 * Calls the {@link Player}'s {@link Player#finalise()} method. Should
	 * finalise the {@link Player} through it's mentioned method, and also the
	 * {@link PlayerManager} object itself. Implementations can do other things
	 * here, but should obey the return mechanism: True on success, false
	 * otherwise.
	 * @return The value that the {@link Player#finalise()} method returns.
	 */
	public boolean finalisePlayer() {
		logger.debug("StrategySimulator.Library.PlayerManager finalising player.");
		return superPlayer.finalise();
	}

}
