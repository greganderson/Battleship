package com.familybiz.greg.battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

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

	public void addGame(String[][] player1Cells,
	                    String[][] player2Cells,
	                    String[][] player1Shots,
	                    String[][] player2Shots,
	                    boolean isPlayer1Turn,
	                    boolean isDone,
	                    Date timeStarted) {
		mGames.add(new Game(player1Cells, player2Cells, player1Shots, player2Shots, isPlayer1Turn, isDone, timeStarted));
		if (mOnGameCollectionChangedListener != null)
			mOnGameCollectionChangedListener.onGameCollectionChanged();
	}

	public GameDetail[] getListOfGames() {
		GameDetail[] result = new GameDetail[mGames.size()];
		for (int i = 0; i < mGames.size(); i++)
			result[i] = new GameDetail(convertDateToString(mGames.get(i).timeStarted), mGames.get(i).isDone);

		Arrays.sort(result, new Comparator<Object>() {
			@Override
			public int compare(Object game1, Object game2) {
				return ((GameDetail)game1).name.compareTo(((GameDetail)game2).name);
			}
		});
		return result;
	}

	private String convertDateToString(Date date) {
		String sDate = date.toString();
		return sDate.substring(sDate.indexOf(' '), sDate.length()-9);
	}


	private class Game {
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
	}


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
