package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.familybiz.greg.battleship.network.requestObjects.BoardData;

/**
 * Created by Greg Anderson
 */
public class GridFragment extends Fragment {

	private Player mPlayer;

	private GridView mPlayerGrid;
	private GridView mOpponentGrid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

		mPlayer = new Player();

		rootLayout.addView(mPlayerGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		rootLayout.addView(mOpponentGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

		return rootLayout;
	}

	/**
	 * Sets the title of the app to the name of the player.
	 */
	public void setPlayerName(String playerName) {
		getActivity().setTitle(playerName);

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
						// TODO: Implement
						// TODO: Fire missile, then check if the shot was valid
					}
				});
			}
		}
	}

	public void updateCells(boolean isPlayersBoard, BoardData board) {
		updateCells(isPlayersBoard ? mPlayerGrid : mOpponentGrid, board);
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
}
