package com.familybiz.greg.battleship.network;


import com.familybiz.greg.battleship.network.PostObjects.CreateGame;
import com.familybiz.greg.battleship.network.PostObjects.Guess;
import com.familybiz.greg.battleship.network.PostObjects.JoinGame;
import com.familybiz.greg.battleship.network.PostObjects.PlayerBoard;
import com.familybiz.greg.battleship.network.PostObjects.PlayerStatus;

/**
 * Created by Greg Anderson
 */
public class PostRequest {

	private PlayerStatus mPlayerStatus;
	private CreateGame mCreateGame;
	private JoinGame mJoinGame;
	private Guess mGuess;
	private PlayerBoard mPlayerBoard;

	public PostRequest() {
		mPlayerStatus = new PlayerStatus();
		mCreateGame= new CreateGame();
		mJoinGame = new JoinGame();
		mGuess = new Guess();
		mPlayerBoard = new PlayerBoard();
	}


	// Player status

	public void getPlayerStatus(String gameId, String playerId) {
		mPlayerStatus.executePost(gameId, playerId);
	}

	public void setOnPlayerStatusReceivedListener(PlayerStatus.OnPlayerStatusReceivedListener onPlayerStatusReceivedListener) {
		mPlayerStatus.setOnPlayerStatusReceivedListener(onPlayerStatusReceivedListener);
	}


	// Create game

	public void createGame(String gameName, String playerName) {
		mCreateGame.executePost(gameName, playerName);
	}
	public void setOnCreateGameListener(CreateGame.OnCreateGameListener onCreateGameListener) {
		mCreateGame.setOnCreateGameListener(onCreateGameListener);
	}


	// Join game

	public void joinGame(String gameId, String playerName) {
		mJoinGame.executePost(gameId, playerName);
	}

	public void setOnJoinGameListener(JoinGame.OnJoinGameListener onJoinGameListener) {
		mJoinGame.setOnJoinGameListener(onJoinGameListener);
	}


	// Make guess

	public void makeGuess(String gameId, String playerId, int x, int y) {
		mGuess.executePost(gameId, playerId, x, y);
	}

	public void setGuessMadeListener(Guess.OnGuessMadeListener guessMadeListener) {
		mGuess.setGuessMadeListener(guessMadeListener);
	}


	// Get boards

	public void loadBoards(String gameId, String playerId) {
		mPlayerBoard.executePost(gameId, playerId);
	}

	public void setOnBoardReceivedListener(PlayerBoard.OnBoardReceivedListener boardReceivedListener) {
		mPlayerBoard.setBoardReceivedListener(boardReceivedListener);
	}

}
