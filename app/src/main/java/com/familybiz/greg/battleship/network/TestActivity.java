package com.familybiz.greg.battleship.network;

/*
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.familybiz.greg.battleship.network.GetObjects.GameDetailRequest;
import com.familybiz.greg.battleship.network.GetObjects.GameList;
import com.familybiz.greg.battleship.network.PostObjects.CreateGame;
import com.familybiz.greg.battleship.network.PostObjects.Guess;
import com.familybiz.greg.battleship.network.PostObjects.JoinGame;
import com.familybiz.greg.battleship.network.PostObjects.PlayerBoard;
import com.familybiz.greg.battleship.network.PostObjects.PlayerStatus;
import com.familybiz.greg.battleship.network.requestObjects.Board;
import com.familybiz.greg.battleship.network.requestObjects.Game;
import com.familybiz.greg.battleship.network.requestObjects.GameDetail;
import com.familybiz.greg.battleship.network.requestObjects.Player;


public class TestActivity extends Activity implements GameList.OnAllGamesReceivedListener, GameDetailRequest.OnGameDetailReceivedListener, PlayerStatus.OnPlayerStatusReceivedListener, JoinGame.OnJoinGameListener, CreateGame.OnCreateGameListener, Guess.OnGuessMadeListener, PlayerBoard.OnBoardReceivedListener {
*/
public class TestActivity {

	public static final String baseUrl = "http://battleship.pixio.com/api/games";
}
/*

	// Get
	private Button getAllGamesButton;
	private Button getGameDetailButton;

	// Post
	private Button getPlayerStatusButton;
	private Button createGameButton;
	private Button joinGameButton;
	private Button makeGuessButton;
	private Button getPlayerBoardsButton;

	private GetRequest mGetRequest;
	private PostRequest mPostRequest;

	private Game[] mGames;
	private Game mCurrentGame;
	private GameDetail mGameDetail;
	private Player mPlayer;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGetRequest = new GetRequest();
		mPostRequest = new PostRequest();

		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);


		getAllGamesButton = new Button(this);
		getAllGamesButton.setText("Get List of Games");

		getGameDetailButton = new Button(this);
		getGameDetailButton.setText("Get Game Detail");

		getPlayerStatusButton = new Button(this);
		getPlayerStatusButton.setText("Get Player Status");

		createGameButton = new Button(this);
		createGameButton.setText("Create game!");

		joinGameButton = new Button(this);
		joinGameButton.setText("Join game!");

		makeGuessButton = new Button(this);
		makeGuessButton.setText("Make guess!");

		getPlayerBoardsButton = new Button(this);
		getPlayerBoardsButton.setText("Load boards!");

		rootLayout.addView(getAllGamesButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		rootLayout.addView(getGameDetailButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		rootLayout.addView(createGameButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		rootLayout.addView(joinGameButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		rootLayout.addView(makeGuessButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		rootLayout.addView(getPlayerBoardsButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		setContentView(rootLayout);


		// All games

		getAllGamesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mGetRequest.getAllGames();
			}
		});

		// Game detail

		getGameDetailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//mGetRequest.getGameDetail();
			}
		});

		// Player status

		getPlayerStatusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String gameId = "b6b0ab3d-44ca-4d41-b281-d701f5f39c2b";
				String playerId = "4b17fe5f-2774-4a23-92f5-0eac323aceca";
				mPostRequest.getPlayerStatus(gameId, playerId);
			}
		});

		// Create game

		createGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String gameName = "Game 1.1.1";
				String playerName = "Chuck Norris";
				mPostRequest.createGame(gameName, playerName);
			}
		});

		// Join game

		joinGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String gameId = "b6b0ab3d-44ca-4d41-b281-d701f5f39c2b";
				String playerName = "Chuck Norris";
				mPostRequest.joinGame(gameId, playerName);
			}
		});

		// Make guess

		makeGuessButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int x = 4;
				int y = 7;
				mPostRequest.makeGuess(mGames[0].id, mPlayer.playerId, x, y);
			}
		});

		// Load boards

		getPlayerBoardsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//
			}
		});

		mGetRequest.setOnAllGamesReceivedListener(this);
		mGetRequest.setOnGameDetailReceivedListener(this);
		mPostRequest.setOnPlayerStatusReceivedListener(this);
		mPostRequest.setOnJoinGameListener(this);
		mPostRequest.setOnCreateGameListener(this);
		mPostRequest.setGuessMadeListener(this);
		mPostRequest.setBoardReceivedListener(this);
	}

	@Override
	public void onAllGamesReceived(Game[] games) {
		mGames = games;
	}

	@Override
	public void onGameDetailReceived(GameDetail gameDetail) {
		mGameDetail = gameDetail;
	}

	@Override
	public void onPlayerStatusReceived(PlayerStatus playerStatus) {
		// TODO: Implement
	}

	@Override
	public void onJoinGame(Player player) {
		mPlayer = player;
	}

	@Override
	public void onCreateGame(Game game, Player player) {
		mCurrentGame = game;
		mPlayer = player;
	}

	@Override
	public void onGuessMade(boolean hit, int shipSunk) {
		// TODO: Implement
	}

	@Override
	public void onBoardReceived(Board playerBoard, Board opponentBoard) {
		// TODO: Implement
	}
}
*/