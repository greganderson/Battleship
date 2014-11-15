package com.familybiz.greg.battleship.network;


import com.familybiz.greg.battleship.network.GetObjects.GameDetailRequest;
import com.familybiz.greg.battleship.network.GetObjects.GameList;
import com.familybiz.greg.battleship.network.requestObjects.Game;

/**
 * Created by Greg Anderson
 */
public class GetRequest {

	private GameList mGameList;
	private GameDetailRequest mGameDetailRequest;

	public GetRequest() {
		mGameList = new GameList();
		mGameDetailRequest = new GameDetailRequest();
	}

	// Get games

	public void getAllGames() {
		mGameList.executeGet();
	}

	public void setOnAllGamesReceivedListener(GameList.OnAllGamesReceivedListener onAllGamesReceivedListener) {
		mGameList.setOnAllGamesReceivedListener(onAllGamesReceivedListener);
	}


	// Get game detail

	public void getGameDetail(Game[] games, int id) {
		mGameDetailRequest.executeGet(games, id);
	}

	public void setOnGameDetailReceivedListener(GameDetailRequest.OnGameDetailReceivedListener onGameDetailReceivedListener) {
		mGameDetailRequest.setOnGameDetailReceivedListener(onGameDetailReceivedListener);
	}
}
