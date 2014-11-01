package com.familybiz.greg.battleship;

public class GameCollection {

	private static GameCollection mInstance = null;

	public static synchronized GameCollection getInstance() {
		if (mInstance == null)
			mInstance = new GameCollection();
		return mInstance;
	}

	private GameCollection() {}


	private class Game {

		private boolean isDone;
		private boolean isPlayer1Turn;
		private Player.Cell[][] player1Cells;
		private Player.Cell[][] player2Cells;
	}

}
