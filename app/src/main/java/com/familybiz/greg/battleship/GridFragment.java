package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Greg Anderson
 */
public class GridFragment extends Fragment implements Player.OnPlayerGridChangedListener {

	private Player player1;
	private Player player2;

	private GridView mPlayer1PlayerGrid;
	private GridView mPlayer2PlayerGrid;
	private GridView mPlayer1OpponentGrid;
	private GridView mPlayer2OpponentGrid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootLayout = new LinearLayout(getActivity());
		rootLayout.setOrientation(LinearLayout.HORIZONTAL);

		mPlayer1PlayerGrid = new GridView(getActivity());
		mPlayer1OpponentGrid = new GridView(getActivity());
		mPlayer2PlayerGrid = new GridView(getActivity());
		mPlayer2OpponentGrid = new GridView(getActivity());

		player1 = new Player();
		player2 = new Player();

		player1.setOnPlayerGridChangedListener(this);
		player2.setOnPlayerGridChangedListener(this);

		String[][] shipCellsPlayer1 = player1.getShipCells();
		String[][] shipCellsPlayer2 = player2.getShipCells();

		initializePlayerShipCells(mPlayer1PlayerGrid, shipCellsPlayer1);
		initializeOpenWaterCells(mPlayer1OpponentGrid);
		initializePlayerShipCells(mPlayer2PlayerGrid, shipCellsPlayer2);
		initializeOpenWaterCells(mPlayer2OpponentGrid);

		rootLayout.addView(mPlayer1PlayerGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		rootLayout.addView(mPlayer1OpponentGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

		return rootLayout;
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

	private void initializeOpenWaterCells(GridView view) {
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				CellView c = new CellView(getActivity(), x, y);
				c.setBackgroundColor(GridView.CELL_COLOR_WATER);

				c.setOnCellTouchedListener(new CellView.OnCellTouchedListener() {
					@Override
					public void onCellTouched(int x, int y) {
						if (!player2.opponentShotMissile(x, y))
							Toast.makeText(getActivity(), "Invalid shot", Toast.LENGTH_SHORT).show();
						//
					}
				});

				view.addView(c);
			}
		}
	}

	@Override
	public void onPlayerGridChanged(int x, int y, boolean isHit) {
		player1.playerShotMissile(x, y, isHit);
		mPlayer1OpponentGrid.setCellColor(x, y, isHit ? GridView.CELL_COLOR_HIT : GridView.CELL_COLOR_MISS);
	}
}
