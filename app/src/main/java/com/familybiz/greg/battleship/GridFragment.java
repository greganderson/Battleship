package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Greg Anderson
 */
public class GridFragment extends Fragment implements Player.OnPlayerGridChangedListener, Player.OnAllShipsDestroyedListener {

	public static String PLAYER_TURN = "player_turn";

	private Player mPlayer1;
	private Player mPlayer2;

	private boolean mTurnPlayer1 = true;

	private LinearLayout mRootLayout;

	private GridView mPlayer1PlayerGrid;
	private GridView mPlayer2PlayerGrid;
	private GridView mPlayer1OpponentGrid;
	private GridView mPlayer2OpponentGrid;

	private Date mTimeStarted;
	private boolean mInProgress;
	private Timer mTimer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mTimeStarted = new Date();
		mInProgress = true;
		mTimer = new Timer();

		getActivity().setTitle("Player 1");

		mRootLayout = new LinearLayout(getActivity());
		mRootLayout.setOrientation(LinearLayout.HORIZONTAL);

		mPlayer1PlayerGrid = new GridView(getActivity());
		mPlayer1PlayerGrid.setVisibility(View.VISIBLE);
		mPlayer1PlayerGrid.setBackgroundColor(Color.BLACK);

		mPlayer1OpponentGrid = new GridView(getActivity());
		mPlayer1OpponentGrid.setVisibility(View.VISIBLE);
		mPlayer1OpponentGrid.setBackgroundColor(Color.WHITE);

		mPlayer2PlayerGrid = new GridView(getActivity());
		mPlayer2PlayerGrid.setVisibility(View.GONE);
		mPlayer2PlayerGrid.setBackgroundColor(Color.BLACK);

		mPlayer2OpponentGrid = new GridView(getActivity());
		mPlayer2OpponentGrid.setVisibility(View.GONE);
		mPlayer2OpponentGrid.setBackgroundColor(Color.WHITE);

		mPlayer1 = new Player();
		mPlayer2 = new Player();

		mPlayer1.setOnPlayerGridChangedListener(this);
		mPlayer2.setOnPlayerGridChangedListener(this);

		mPlayer1.setOnAllShipsDestroyedListener(this);
		mPlayer2.setOnAllShipsDestroyedListener(this);

		String[][] shipCellsPlayer1 = mPlayer1.getShipCells();
		String[][] shipCellsPlayer2 = mPlayer2.getShipCells();

		initializePlayerShipCells(mPlayer1PlayerGrid, shipCellsPlayer1);
		initializeOpenWaterCells(mPlayer1OpponentGrid, true);
		initializePlayerShipCells(mPlayer2PlayerGrid, shipCellsPlayer2);
		initializeOpenWaterCells(mPlayer2OpponentGrid, false);

		mRootLayout.addView(mPlayer1PlayerGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		mRootLayout.addView(mPlayer1OpponentGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

		mRootLayout.addView(mPlayer2PlayerGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		mRootLayout.addView(mPlayer2OpponentGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

		return mRootLayout;
	}

	private void initializePlayerShipCells(GridView view, String[][] cells) {
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				CellView c = new CellView(getActivity(), x, y);

				// Set color of cell to gray if a ship, blue otherwise
				c.setBackgroundColor(cells[y][x].equals(Player.SHIP) ? GridView.CELL_COLOR_SHIP : GridView.CELL_COLOR_WATER);

				view.addView(c);
			}
		}
	}

	private void initializeOpenWaterCells(GridView view, boolean isPlayer1) {
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				CellView c = new CellView(getActivity(), x, y);
				c.setBackgroundColor(GridView.CELL_COLOR_WATER);

				if (isPlayer1) {
					c.setOnCellTouchedListener(new CellView.OnCellTouchedListener() {
						@Override
						public void onCellTouched(int x, int y) {
							if (!mPlayer2.opponentShotMissile(x, y))
								Toast.makeText(getActivity(), "Invalid shot", Toast.LENGTH_SHORT).show();
						}
					});
				}
				else {
					c.setOnCellTouchedListener(new CellView.OnCellTouchedListener() {
						@Override
						public void onCellTouched(int x, int y) {
							if (!mPlayer1.opponentShotMissile(x, y))
								Toast.makeText(getActivity(), "Invalid shot", Toast.LENGTH_SHORT).show();
						}
					});
				}

				view.addView(c);
			}
		}
	}

	/**
	 * Updates the cells in the given grid view to match the cell types of cells.
	 */
	private void updateCells(GridView view, String[][] cells) {
		for (int i = 0; i < view.getChildCount(); i++) {
			int x = i % Player.GRID_WIDTH;
			int y = i / Player.GRID_HEIGHT;
			CellView cell = (CellView)view.getChildAt(i);

			String cellType = cells[y][x];

			// Get color to set
			int color = -1;
			if (cellType.equals(Player.SHIP))
				color = GridView.CELL_COLOR_SHIP;
			else if (cellType.equals(Player.WATER))
				color = GridView.CELL_COLOR_WATER;
			else if (cellType.equals(Player.HIT))
				color = GridView.CELL_COLOR_HIT;
			else if (cellType.equals(Player.MISS))
				color = GridView.CELL_COLOR_MISS;
			else
				color = Color.BLACK;

			cell.setBackgroundColor(color);
		}
	}

	private void nextTurn() {
		if (mTurnPlayer1) {
			mTurnPlayer1 = false;
			mPlayer1PlayerGrid.setVisibility(View.GONE);
			mPlayer1OpponentGrid.setVisibility(View.GONE);
			mPlayer2PlayerGrid.setVisibility(View.VISIBLE);
			mPlayer2OpponentGrid.setVisibility(View.VISIBLE);
		}
		else {
			mTurnPlayer1 = true;
			mPlayer1PlayerGrid.setVisibility(View.VISIBLE);
			mPlayer1OpponentGrid.setVisibility(View.VISIBLE);
			mPlayer2PlayerGrid.setVisibility(View.GONE);
			mPlayer2OpponentGrid.setVisibility(View.GONE);
		}
		getActivity().setTitle("Player " + (mTurnPlayer1 ? 1 : 2));
	}

	@Override
	public void onPlayerGridChanged(int x, int y, boolean isHit) {
		if (mTurnPlayer1) {
			mPlayer1.playerShotMissile(x, y, isHit);
			mPlayer1OpponentGrid.setCellColor(x, y, isHit ? GridView.CELL_COLOR_HIT : GridView.CELL_COLOR_MISS);
		}
		else {
			mPlayer2.playerShotMissile(x, y, isHit);
			mPlayer2OpponentGrid.setCellColor(x, y, isHit ? GridView.CELL_COLOR_HIT : GridView.CELL_COLOR_MISS);
		}
		Toast.makeText(getActivity(), isHit ? "Hit!" : "Miss", Toast.LENGTH_SHORT).show();

		if (mOnChangeTurnListener != null)
			mOnChangeTurnListener.onChangeTurn(true);
		mTimer.schedule(new GameTimer(), 2000);
	}

	@Override
	public void onAllShipsDestroyed() {
		mInProgress = false;
		clearListeners();
		Toast.makeText(getActivity(), "Player " + (mTurnPlayer1 ? 1 : 2) + " wins!", Toast.LENGTH_SHORT).show();
	}

	private class GameTimer extends TimerTask {
		@Override
		public void run() {
			Intent intent = new Intent();
			intent.putExtra(PLAYER_TURN, !mTurnPlayer1);
			intent.setClass(getActivity(), TransitionActivity.class);
			startActivityForResult(intent, 1);
		}
	}

	public void saveAndClose() {
		clearListeners();
		saveCurrentGame();
	}

	private void saveCurrentGame() {
		GameCollection.getInstance().saveGame(
				mPlayer1.getShipCells(),
				mPlayer2.getShipCells(),
				mPlayer1.getOpponentCells(),
				mPlayer2.getOpponentCells(),
				mTurnPlayer1,
				mInProgress,
				mTimeStarted);
	}

	public void loadGame(GameCollection.Game game) {
		// Clear old player listeners
		clearPlayerListeners();

		mTimeStarted = game.timeStarted;
		mInProgress  = game.isDone;
		mTurnPlayer1 = game.isPlayer1Turn;

		// Set the title to reflect who's turn it is
		getActivity().setTitle(game.isPlayer1Turn ? "Player 1" : "Player 2");

		// Show the correct grids
		int player1GridVisibility =  game.isPlayer1Turn ? View.VISIBLE : View.GONE;
		int player2GridVisibility = !game.isPlayer1Turn ? View.VISIBLE : View.GONE;
		mPlayer1PlayerGrid.setVisibility(player1GridVisibility);
		mPlayer1OpponentGrid.setVisibility(player1GridVisibility);
		mPlayer2PlayerGrid.setVisibility(player2GridVisibility);
		mPlayer2OpponentGrid.setVisibility(player2GridVisibility);

		// Create new players and overwrite their information
		mPlayer1 = new Player(game.player1Cells, game.player1Shots);
		mPlayer2 = new Player(game.player2Cells, game.player2Shots);

		// Update the current grid view's cells to reflect the new cell information.
		updateCells(mPlayer1PlayerGrid, game.player1Cells);
		updateCells(mPlayer2PlayerGrid, game.player2Cells);
		updateCells(mPlayer1OpponentGrid, game.player1Shots);
		updateCells(mPlayer2OpponentGrid, game.player2Shots);

		// Reset the listeners
		mPlayer1.setOnPlayerGridChangedListener(this);
		mPlayer2.setOnPlayerGridChangedListener(this);

		mPlayer1.setOnAllShipsDestroyedListener(this);
		mPlayer2.setOnAllShipsDestroyedListener(this);
	}

	/**
	 * Removes all listeners.
	 */
	private void clearListeners() {
		// CellView touch
		for (int i = 0; i < mPlayer1OpponentGrid.getChildCount(); i++)
			((CellView)mPlayer1OpponentGrid.getChildAt(i)).setOnCellTouchedListener(null);
		for (int i = 0; i < mPlayer2OpponentGrid.getChildCount(); i++)
			((CellView)mPlayer2OpponentGrid.getChildAt(i)).setOnCellTouchedListener(null);

		clearPlayerListeners();
	}

	private void clearPlayerListeners() {
		// All ships destroyed
		mPlayer1.setOnAllShipsDestroyedListener(null);
		mPlayer2.setOnAllShipsDestroyedListener(null);

		// Opponent grid change
		mPlayer1.setOnPlayerGridChangedListener(null);
		mPlayer2.setOnPlayerGridChangedListener(null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mOnChangeTurnListener != null)
			mOnChangeTurnListener.onChangeTurn(false);
		nextTurn();
	}

	@Override
	public void onPause() {
		super.onPause();
		saveCurrentGame();
		GameCollection.getInstance().saveAllGames();
	}

	/****************** LISTENERS ******************/


	public interface OnChangeTurnListener {
		public void onChangeTurn(boolean inProgress);
	}
	OnChangeTurnListener mOnChangeTurnListener = null;
	public void setOnChangeTurnListener(OnChangeTurnListener onChangeTurnListener) {
		mOnChangeTurnListener = onChangeTurnListener;
	}
}
