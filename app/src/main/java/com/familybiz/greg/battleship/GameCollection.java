package com.familybiz.greg.battleship;

import com.familybiz.greg.battleship.utils.DateParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Singleton class representing a collection of in progress and completed games.
 */
public class GameCollection {

	private static GameCollection mInstance = null;
	private static final String mFilename = "games.txt";
	private static final String SAVED_GAMES_LIST = "savedGames";

	public static synchronized GameCollection getInstance() {
		if (mInstance == null)
			mInstance = new GameCollection();
		return mInstance;
	}

	private ArrayList<Game> mGames;

	private GameCollection() {
		mGames = new ArrayList<Game>();
	}

	public void saveGame(Game game) {
		saveGame(
				game.player1Cells,
				game.player2Cells,
				game.player1Shots,
				game.player2Shots,
				game.isPlayer1Turn,
				game.isDone,
				game.timeStarted);
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

		writeToFile();
	}

	public void saveAllGames() {
		writeToFile();
	}

	/**
	 * Saves all of the existing games to device.
	 */
	private void writeToFile() {
		try {
			Gson gson = new Gson();

			String jsonGameList = gson.toJson(mGames);

			String result = "{\"" + SAVED_GAMES_LIST + "\":" + jsonGameList + "}";

			File file = new File(MainActivity.SAVED_GAMES_FILEPATH, mFilename);
			FileWriter writer = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.write(result);

			bufferedWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load all games from a saved file if it exists.
	 */
	public void loadGames() {
		try {
			File file = new File(MainActivity.SAVED_GAMES_FILEPATH, mFilename);
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String content = "";
			String input = "";
			while ((input = bufferedReader.readLine()) != null)
				content += input;

			Gson gson = new Gson();

			JsonParser parser = new JsonParser();
			JsonObject data = parser.parse(content).getAsJsonObject();

			Type gameListType = new TypeToken<Game[]>(){}.getType();
			Game[] games = gson.fromJson(data.get(SAVED_GAMES_LIST), gameListType);

			for (Game game : games) saveGame(game);

			bufferedReader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
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
			if (g.timeStarted.toString().equals(timeStarted.toString())) {
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
			if (game.timeStarted.toString().equals(date.toString()))
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
	 * Returns a full list of existing games with all of their information.
	 */
	public Game[] getAllGames() {
		return mGames.toArray(new Game[mGames.size()]);
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

}
