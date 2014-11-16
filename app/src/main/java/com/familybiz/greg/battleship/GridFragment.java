package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.familybiz.greg.battleship.network.PostObjects.Guess;
import com.familybiz.greg.battleship.network.PostObjects.PlayerBoard;
import com.familybiz.greg.battleship.network.PostObjects.PlayerStatus;
import com.familybiz.greg.battleship.network.PostRequest;
import com.familybiz.greg.battleship.network.requestObjects.BoardData;
import com.familybiz.greg.battleship.network.requestObjects.Game;
import com.familybiz.greg.battleship.network.requestObjects.Player;
import com.familybiz.greg.battleship.network.requestObjects.PlayerStatusData;

import java.util.Stack;

/**
 * Created by Greg Anderson
 */
public class GridFragment extends Fragment implements PlayerBoard.OnBoardReceivedListener, Guess.OnGuessMadeListener, PlayerStatus.OnPlayerStatusReceivedListener {

	private Game mGame;
	private Player mPlayer;

	private GridView mPlayerGrid;
	private GridView mOpponentGrid;

	private PostRequest mPostRequest;

	private Stack<Coord> mShotStack;
	private boolean mInProgress;

	private class Coord {
		int x;
		int y;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mPostRequest = new PostRequest();
		mShotStack = new Stack<Coord>();

		LinearLayout rootLayout = new LinearLayout(getActivity());
		rootLayout.setOrientation(LinearLayout.HORIZONTAL);

		mPlayerGrid = new GridView(getActivity());
		mPlayerGrid.setVisibility(View.VISIBLE);
		mPlayerGrid.setBackgroundColor(Color.BLACK);

		mOpponentGrid = new GridView(getActivity());
		mOpponentGrid.setVisibility(View.VISIBLE);
		mOpponentGrid.setBackgroundColor(Color.WHITE);

		initializeCells(mPlayerGrid);
		initializeCells(mOpponentGrid);
		initializeCellTouchListener();

		rootLayout.addView(mPlayerGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		rootLayout.addView(mOpponentGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

		mPostRequest.setOnBoardReceivedListener(this);
		mPostRequest.setGuessMadeListener(this);
		mPostRequest.setOnPlayerStatusReceivedListener(this);

		return rootLayout;
	}

	public void setGame(Game game) {
		mGame = game;
	}

	public void setPlayer(Player player) {
		getActivity().setTitle(player.playerName);
		mPlayer = player;
	}

	private void initializeCells(GridView view) {
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				CellView c = new CellView(getActivity(), x, y);
				view.addCell(x, y, c);
				view.setCellColor(x, y, GridView.CELL_COLOR_WATER);
				view.addView(c);
			}
		}
	}

	private void initializeCellTouchListener() {
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				mOpponentGrid.getCell(x, y).setOnCellTouchedListener(new CellView.OnCellTouchedListener() {
					@Override
					public void onCellTouched(int x, int y) {
						// Queue up a shot
						Coord coord = new Coord();
						coord.x = x;
						coord.y = y;
						mShotStack.push(coord);

						// Check whether the shot is allowed to be fired
						mPostRequest.getPlayerStatus(mGame.id, mPlayer.playerId);
					}
				});
			}
		}
	}

	public void loadCells() {
		mPostRequest.loadBoards(mGame.id, mPlayer.playerId);
	}

	/**
	 * Updates the cells in the given grid view to match the cell types of cells.
	 */
	private void updateCells(GridView view, BoardData board) {
		for (int y = 0; y < 100; y++) {
			for (int x = 0; x < 100; x++) {
				int color = view.getCellColor(x, y);
				String status = board.cells[y][x].status;

				if (status.equals("HIT")) {
					color = GridView.CELL_COLOR_HIT;
				}
				else if (status.equals("MISS")) {
					color = GridView.CELL_COLOR_MISS;
				}
				else if (status.equals("SHIP")) {
					color = GridView.CELL_COLOR_SHIP;
				}
				else if (!status.equals("NONE")) {
					Log.e("UPDATE CELLS", "Found a different enum than was defined.");
				}

				view.setCellColor(x, y, color);
			}
		}
	}

	@Override
	public void onBoardReceived(BoardData playerBoardData, BoardData opponentBoardData) {
		updateCells(mPlayerGrid, playerBoardData);
		updateCells(mOpponentGrid, opponentBoardData);
	}

	@Override
	public void onGuessMade(boolean hit, int shipSunk) {
		if (shipSunk == -1)
			Toast.makeText(getActivity(), "Invalid shot", Toast.LENGTH_SHORT).show();
		else {
			String hitMissText = hit ? "Hit!" : "Miss!";

			Toast.makeText(getActivity(), hitMissText, Toast.LENGTH_SHORT).show();

			// If ship was sunk
			if (shipSunk != 0)
				Toast.makeText(getActivity(), "Sunk!", Toast.LENGTH_SHORT).show();

			// Refresh the boards
			loadCells();
		}
	}

	@Override
	public void onPlayerStatusReceived(PlayerStatusData playerStatusData) {
		// Check if game is already done.
		mInProgress = !playerStatusData.winner.equals("DONE");

		// If it's not the players turn and there is a shot ready to fire, clear the shot
		if (!playerStatusData.isYourTurn && !mShotStack.empty())
			mShotStack.pop();

		// If there is a shot and the game is in progress, then fire the shot
		if (!mShotStack.empty() && mInProgress) {
			Coord coord = mShotStack.pop();
			mPostRequest.makeGuess(mGame.id, mPlayer.playerId, coord.x, coord.y);
		}
	}

	private void clearListeners() {
		mPostRequest.setOnBoardReceivedListener(null);
		mPostRequest.setGuessMadeListener(null);
		mPostRequest.setOnPlayerStatusReceivedListener(null);
	}
}
