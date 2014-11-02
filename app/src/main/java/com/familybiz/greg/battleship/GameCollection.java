package com.familybiz.greg.battleship;

import com.familybiz.greg.battleship.utils.DateParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Singleton class representing a collection of in progress and completed games.
 */
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

	public void saveGame(String[][] player1Cells,
	                     String[][] player2Cells,
	                     String[][] player1Shots,
	                     String[][] player2Shots,
	                     boolean isPlayer1Turn,
	                     boolean isDone,
	                     Date timeStarted) {

		// Check if game already exists, then update it if it does.  Create a new one if it does not.
		if (gameExists(timeStarted))
			updateGame(player1Cells, player2Cells, player1Shots, player2Shots, isPlayer1Turn, isDone, timeStarted);
		else
			mGames.add(new Game(player1Cells, player2Cells, player1Shots, player2Shots, isPlayer1Turn, isDone, timeStarted));

		if (mOnGameCollectionChangedListener != null)
			mOnGameCollectionChangedListener.onGameCollectionChanged();
	}

	/**
	 * Updates an existing game with the new information.
	 */
	private void updateGame(
			String[][] player1Cells,
			String[][] player2Cells,
			String[][] player1Shots,
			String[][] player2Shots,
			boolean isPlayer1Turn,
			boolean isDone,
			Date timeStarted) {
		Game game = null;
		for (Game g : mGames) {
			if (g.timeStarted.compareTo(timeStarted) == 0) {
				game = g;
				break;
			}
		}
		game.player1Cells = player1Cells;
		game.player2Cells = player2Cells;
		game.player1Shots = player1Shots;
		game.player2Shots = player2Shots;
		game.isPlayer1Turn = isPlayer1Turn;
		game.isDone = isDone;
	}

	/**
	 * Returns true if the game with the given date already exists.
	 */
	private boolean gameExists(Date date) {
		for (Game game : mGames)
			if (game.timeStarted.compareTo(date) == 0)
				return true;
		return false;
	}

	/**
	 * Returns a copy of the asked for game.
	 */
	public Game getGame(Date date) {
		for (Game game : mGames)
			if (game.timeStarted.toString().equals(date.toString()))
				return new Game(game);
		return null;
	}

	/**
	 * Returns a sorted array of game details.
	 */
	public GameDetail[] getListOfGames() {
		GameDetail[] result = new GameDetail[mGames.size()];
		for (int i = 0; i < mGames.size(); i++)
			result[i] = new GameDetail(
					DateParser.convertDateToString(mGames.get(i).timeStarted),
					mGames.get(i).isDone);

		// Alphabetize!!
		Arrays.sort(result, new Comparator<Object>() {
			@Override
			public int compare(Object game1, Object game2) {
				return ((GameDetail)game1).name.compareTo(((GameDetail)game2).name);
			}
		});

		return result;
	}


	/**
	 * Contains all the information needed for a game.
	 */
	public class Game {
		public String[][] player1Cells;
		public String[][] player2Cells;
		public String[][] player1Shots;
		public String[][] player2Shots;
		public boolean isPlayer1Turn;
		public boolean isDone;
		public Date timeStarted;

		public Game(String[][] player1Cells,
		            String[][] player2Cells,
		            String[][] player1Shots,
		            String[][] player2Shots,
		            boolean isPlayer1Turn,
		            boolean isDone,
		            Date timeStarted) {
			this.player1Cells = player1Cells;
			this.player2Cells = player2Cells;
			this.player1Shots = player1Shots;
			this.player2Shots = player2Shots;
			this.isPlayer1Turn = isPlayer1Turn;
			this.isDone = isDone;
			this.timeStarted = timeStarted;
		}

		public Game(Game game) {
			this.player1Cells = game.player1Cells;
			this.player2Cells = game.player2Cells;
			this.player1Shots = game.player1Shots;
			this.player2Shots = game.player2Shots;
			this.isPlayer1Turn = game.isPlayer1Turn;
			this.isDone = game.isDone;
			this.timeStarted = game.timeStarted;
		}
	}


	/**
	 * Represents the information to display on the list view.
	 */
	public class GameDetail {
		public String name;
		public boolean inProgress;
		public GameDetail(String name, boolean inProgress) {
			this.name = name;
			this.inProgress = inProgress;
		}
	}


	/****************** LISTENERS ******************/


	public interface OnGameCollectionChangedListener {
		public void onGameCollectionChanged();
	}
	private OnGameCollectionChangedListener mOnGameCollectionChangedListener = null;
	public void setOnGameCollectionChangedListener(OnGameCollectionChangedListener onGameCollectionChangedListener) {
		mOnGameCollectionChangedListener = onGameCollectionChangedListener;
	}
	public OnGameCollectionChangedListener getOnGameCollectionChangedListener() {
		return mOnGameCollectionChangedListener;
	}

}
