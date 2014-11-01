package com.familybiz.greg.battleship;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GameCollection {

	private static GameCollection mInstance = null;

	public static synchronized GameCollection getInstance() {
		if (mInstance == null)
			mInstance = new GameCollection();
		return mInstance;
	}

	private ArrayList<Game> mGames;

	private GameCollection() {
		mGames = new ArrayList<Game>();
	}

	public void addGame(Player.Cell[][] player1, Player.Cell[][] player2, boolean isPlayer1Turn, boolean isDone, Date timeStarted) {
		mGames.add(new Game(player1, player2, isPlayer1Turn, isDone, timeStarted));
	}

	public Map<String, Boolean> getListOfGames() {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		for (Game game : mGames)
			result.put(game.timeStarted.toString(), game.isDone);

		return result;
	}


	private class Game {
		public Player.Cell[][] player1Cells;
		public Player.Cell[][] player2Cells;
		public boolean isPlayer1Turn;
		public boolean isDone;
		public Date timeStarted;

		public Game(Player.Cell[][] player1, Player.Cell[][] player2, boolean isPlayer1Turn, boolean isDone, Date timeStarted) {
			player1Cells = player1;
			player2Cells = player2;
			this.isPlayer1Turn = isPlayer1Turn;
			this.isDone = isDone;
			this.timeStarted = timeStarted;
		}
	}

}
