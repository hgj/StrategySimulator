# StrategySimulator

StrategySimulator is a Java framework to create turn based strategy simulations.
Ideal start for AI developement and competitions or quick strategy simulations.

## Real time simulation

The framework is ideal for turn based games, but it does not define such process
for the simulation. You can easily use it to simulate - even real-time processes
- with the framework. Use a specific tick time, and tick (step) every player in
each tick.

## Components

### StrategySimulator.Library

The **Library** is the center of the framework - all other components are built
around this. Implemented **Game Logic**s and **Player**s are all based on the
classes in the **Library**.

### StrategySimulator

The **Simulator** itself is the foundation for all implemented games. It
prepares, loads and simulates the sessions. Putting it simple, this is the
application.

### EmptyGame

The **EmptyGame** is a base for all custom **Game Logic** implementations. That
means, that when you start creating your own game, start with this code, rename
and you are ready to go.

It is also a really dumb example for a game.

### Gomoku

**Gomoku** is a real **Game Logic** implementation. It is a full featured
simulator for the game Gomoku.

_This example implementation is used in real life at a Gomoku AI writing
competition every year._

## Inviting others to implement their players

If you want to share your game, and invite others to implement their players,
you have to send your game implementation. They can use this project to get the
**Library** and the **Simulator**, so they can test their implementations.

## Building

Building is done with **Gradle**. If you run `gradle build`, you should end up
with a `dist` directory containing all the dependencies, the two game
implementations and the simulator itself.

## Running

You can run the **Gomoku** game for example, with:

	cd dist
	java -jar StrategySimulator-1.6.0.jar Gomoku.conf

## Changes

- 1.6.0
	* Added Gradle
	* Added support for jar files (much easier than .class files)
	* Added ImprovedConfiguration for configuration
	* Cleaned up code
	* Added log support via SLF4J
	* Added to GitHub (again)
- 1.5.0
	* Old version, not under version control
