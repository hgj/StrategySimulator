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

import java.util.*;

/**
 * Abstract implementation of the {@link GameLogic} managing the whole
 * simulation process that the implementation can build on.
 */
public abstract class GameLogic {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The {@link PlayerManager}s, responsible for their {@link Player}s. The
	 * first {@link PlayerManager}'s key is 1.
	 */
	protected Map<Integer, PlayerManager> superPlayerManagers = new HashMap<>();

	/**
	 * The custom per game configuration from the configuration file.
	 */
	protected Configuration configuration;

	/**
	 * Fills up the local {@link #superPlayerManagers} {@link Map}, starting
	 * with the key 1. The implementations can extend the constructor and use
	 * it as usual, as long as they call this one (the parent's) first. Also,
	 * as a general rule, every initialisation that can be moved, should be
	 * moved to the {@link #reset()} method.
	 * from this object.
	 * @param configuration The custom per game configuration from the
	 * configuration file.
	 * @param superPlayerManagers A list with all the player managers.
	 */
	public GameLogic(Configuration configuration, List<PlayerManager> superPlayerManagers) {
		this.configuration = configuration;
		Integer i = 1;
		for (PlayerManager playerManager : superPlayerManagers) {
			this.superPlayerManagers.put(i++, playerManager);
			playerManager.setGameLogic(this);
		}
		logger.debug("StrategySimulator.Library.GameLogic constructed. Stored the List of superPlayerManagers.");
	}

	/**
	 * Return the name of the game.
	 * @return "GameLogic"
	 */
	public String getName() {
		return "GameLogic";
	}

	/**
	 * Return the number of {@link PlayerManager}s == number of {@link
	 * Player}s.
	 * @return The size of {@link #superPlayerManagers}.
	 */
	public Integer getNumberOfPlayerManagers() {
		return superPlayerManagers.size();
	}

	// TODO: Return the playermanagers to the Framework

	/**
	 * The number of the current (or already completed) round.
	 */
	protected Integer round = 0;

	/**
	 * State enum to represent the {@link #state} of the object.
	 */
	public enum State {

		/**
		 * The object is not stable. Something went wrong, so we ended up here.
		 * The game stays in this state until it is reset with the {@link
		 * #resetWrapper()} method. Only the {@link #resetWrapper()} can be
		 * called in this state.
		 */
		UNSTABLE,

		/**
		 * The object was newly created or has been reset to that state. The
		 * object stays in this state until a successful {@link
		 * #initialiseWrapper()} is called. That's why this state does not mean
		 * that the {@link #initialiseWrapper()} method was not called yet.
		 * Only the {@link #initialiseWrapper()} method can be called in this
		 * state.
		 */
		RESET,

		/**
		 * The game has been successfully initialised. The object stays in this
		 * state until a successful {@link #stepGameWrapper()} is called.
		 * That's why this state does not mean that the {@link
		 * #stepGameWrapper()} method was not called yet. Only the {@link
		 * #stepGameWrapper()} or  {@link #resetWrapper()} methods can be
		 * called
		 * in this state.
		 */
		INITIALISED,

		/**
		 * The game has started - at least one successful {@link
		 * #stepGameWrapper()} was called. The object stays in this state until
		 * a call to {@link #stepGameWrapper()} returns false. Only the {@link
		 * #stepGameWrapper()} or {@link #resetWrapper()} methods can be called
		 * in this state.
		 */
		STARTED,

		/**
		 * The last {@link #stepGameWrapper()} returned false as the simulation
		 * is finished. The game stays in this state until a successful {@link
		 * #finaliseWrapper()} is called. Only the {@link #finaliseWrapper()}
		 * or {@link #resetWrapper} methods can be called in this state.
		 */
		FINISHED,

		/**
		 * The call to the {@link #finaliseWrapper()} method was successful.
		 * The game stays in this state until it is reset with the {@link
		 * #resetWrapper()} method. Only the {@link #resetWrapper()} method can
		 * be called in this state.
		 */
		FINALISED
	}

	/**
	 * The current state of the object/game, that defaults to unstable.
	 */
	private State state = State.UNSTABLE;

	/**
	 * Getter for the {@link #state} variable.
	 * @return {@link #state} as it is.
	 */
	public State getState() {
		return this.state;
	}

	//
	// Abstract methods to implement in children.
	//

	/**
	 * Should reset the state of the object to its new-born state. Only put
	 * things into this method, that you would put in the constructor. The
	 * PlayerManagers will be reset if this method returns true. The method may
	 * be called again if unsuccessful.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean reset();

	/**
	 * This method is called after a successful {@link #reset()} and when all
	 * PlayerManagers returned success from their {@link
	 * PlayerManager#resetPlayer()} methods.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean resetAfter();

	/**
	 * Should initialise the game logic. The implemented {@link GameLogic}
	 * should initialise the general game environment when this method is
	 * called. The method may be called again if unsuccessful.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean initialise();

	/**
	 * This method is called after a successful {@link #initialise()} and when
	 * all PlayerManagers returned success from their {@link
	 * PlayerManager#initialisePlayer()} methods.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean initialiseAfter();

	/**
	 * Should step the simulation, by evaluating all the strategies in the
	 * game. The rounds are started and ended by {@link #stepGameWrapper()}, so
	 * this method should only deal with the actual tasks that should be done
	 * in each round. While this method is 'running', the {@link #round} member
	 * represents the current started round's number.
	 * @return True if the simulation has to continue, false otherwise. The
	 * return value has *no connection* with an unsuccessful step!
	 */
	// TODO: SimulationException on unsuccessful step!
	protected abstract boolean stepGame();

	/**
	 * Should finalise the simulation. This method is called when the game is
	 * over, and the implemented {@link GameLogic} should finalise the
	 * environment. This method will not be called again if unsuccessful.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean finalise();

	/**
	 * This method is called after a successful {@link #finalise()} and when
	 * all PlayerManagers returned success from their {@link
	 * PlayerManager#finalisePlayer()} methods.
	 * @return True on success, false otherwise.
	 */
	protected abstract boolean finaliseAfter();

	//
	// Wrappers for the abstract methods.
	//

	/**
	 * This method wraps the {@link #reset()} method and resets the {@link
	 * PlayerManager}s.
	 * @return The return value from {@link #reset()}.
	 * @throws IllegalStateException If the method was called in an illegal
	 * state.
	 */
	public boolean resetWrapper() throws IllegalStateException {
		if (state == State.RESET) {
			logger.warn("Trying to reset GameLogic in {} state.", state.toString());
			throw new IllegalStateException("The simulation is already reset.");
		}

		// Try to reset the GameLogic
		boolean result = reset();

		// Try to reset the Players, if the GameLogic reset was successful
		boolean playersResult = true;
		if (result) {
			for (Iterator<PlayerManager> it = superPlayerManagers.values().iterator(); it.hasNext() && playersResult; ) {
				PlayerManager playerManager = it.next();
				boolean playerResult = playerManager.resetPlayer();
				playersResult &= playerResult;
				if (playerResult) {
					logger.debug("Successfully reset {}.", playerManager.getPlayerIdentity());
				} else {
					logger.debug("Could not reset {}.", playerManager.getPlayerIdentity());
				}
			}
		}
		result &= playersResult;

		// Call the resetAfter() method
		result &= resetAfter();

		if (result) {
			state = State.RESET;
			logger.trace("GameLogic is RESET.");
		} else {
			state = State.UNSTABLE;
			logger.trace("GameLogic is UNSTABLE, as it could not be reset.");
		}
		return result;
	}

	/**
	 * This method wraps the {@link #initialise()} method and initialises the
	 * {@link PlayerManager}s.
	 * @return The return value from {@link #initialise()}.
	 * @throws IllegalStateException If the method was called in an illegal
	 * state.
	 */
	public boolean initialiseWrapper() throws IllegalStateException {
		if (state != State.RESET) {
			logger.warn("Trying to initialise GameLogic in {} state.", state.toString());
			throw new IllegalStateException("The simulation is already initialised or finised.");
		}

		// Initialise the GameLogic first
		boolean result = initialise();

		// Try to initialise the Players, if the GameLogic initialise was successful
		boolean playersResult = true;
		if (result) {
			for (Iterator<PlayerManager> it = superPlayerManagers.values().iterator(); it.hasNext() && playersResult; ) {
				PlayerManager playerManager = it.next();
				boolean playerResult = playerManager.initialisePlayer();
				playersResult &= playerResult;
				if (playerResult) {
					logger.debug("Successfully initialised {}.", playerManager.getPlayerIdentity());
				} else {
					logger.debug("Could not initialised {}.", playerManager.getPlayerIdentity());
				}
			}
		}
		result &= playersResult;

		// Call the initialiseAfter() method
		result &= initialiseAfter();

		if (result) {
			state = State.INITIALISED;
			logger.trace("GameLogic is INITIALISED.");
		} else {
			state = State.UNSTABLE;
			logger.trace("GameLogic is UNSTABLE, as it could not be initialised.");
		}
		return result;
	}

	/**
	 * This method wraps the {@link #stepGame()} method to measure the elapsed
	 * time, change the state and tick the {@link #round} variable.
	 * @return The return value from {@link #stepGame()}.
	 * @throws IllegalStateException If the method was called in an illegal
	 * state.
	 */
	public boolean stepGameWrapper() throws IllegalStateException {
		if (!(state == State.INITIALISED || state == State.STARTED)) {
			// TODO: Custom Exception class? And if, catch the exception.
			logger.warn("Trying to step GameLogic in {} state.", state.toString());
			throw new IllegalStateException("The simulation can only be stepped after initialisation and before it is finished.");
		}

		++round;

		logger.trace("Starting round {}.", round);
		long time = Calendar.getInstance().getTimeInMillis();
		boolean result = stepGame();
		time = Calendar.getInstance().getTimeInMillis() - time;
		logger.trace("Round {} finished in {} ms.", round, time);

		if (result) {
			if (state != State.STARTED) {
				state = State.STARTED;
				logger.trace("GameLogic is STARTED - we had our first successful step.");
			}
		} else {
			logger.trace("GameLogic is FINISHED - the last step returned false.");
			state = State.FINISHED;
		}
		return result;
	}

	/**
	 * This method wraps the {@link #finalise()} method and finalises the
	 * {@link PlayerManager}s.
	 * @return The return value from {@link #finalise()}.
	 * @throws IllegalStateException If the method was called in an illegal
	 * state.
	 */
	public boolean finaliseWrapper() throws IllegalStateException {
		if (state != State.FINISHED) {
			logger.warn("Trying to finalise GameLogic in {} state.", state.toString());
			throw new IllegalStateException("The simulation can only be finalised after it is finised.");
		}

		// Finalise the GameLogic first
		boolean result = finalise();

		// Try to finalise the Players, if the GameLogic finalisation was successful
		boolean playersResult = true;
		if (result) {
			for (Iterator<PlayerManager> it = superPlayerManagers.values().iterator(); it.hasNext() && playersResult; ) {
				PlayerManager playerManager = it.next();
				boolean playerResult = playerManager.finalisePlayer();
				playersResult &= playerResult;
				if (playerResult) {
					logger.debug("Successfully finalised {}.", playerManager.getPlayerIdentity());
				} else {
					logger.debug("Could not finalise {}.", playerManager.getPlayerIdentity());
				}
			}
		}
		result &= playersResult;

		// Call the finaliseAfter() method
		result &= finaliseAfter();

		if (result) {
			state = State.FINALISED;
			logger.trace("GameLogic is FINALISED.");
		} else {
			state = State.UNSTABLE;
			logger.trace("GameLogic is UNSTABLE, as it could not be finalised.");
		}
		return result;
	}

}
