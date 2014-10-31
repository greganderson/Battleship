package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Greg Anderson
 */
public class GridFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootLayout = new LinearLayout(getActivity());
		rootLayout.setOrientation(LinearLayout.HORIZONTAL);

		GridView gridViewPlayer1 = new GridView(getActivity());
		GridView gridViewPlayer2 = new GridView(getActivity());
		gridViewPlayer1.setBackgroundColor(Color.BLACK);
		gridViewPlayer2.setBackgroundColor(Color.BLACK);

		Player player1 = new Player();
		Player player2 = new Player();
		String[][] cellsPlayer1 = player1.getCells();
		String[][] cellsPlayer2 = player2.getCells();

		initializePlayerCells(gridViewPlayer1, cellsPlayer1);
		initializePlayerCells(gridViewPlayer2, cellsPlayer2);

		rootLayout.addView(gridViewPlayer1, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		rootLayout.addView(gridViewPlayer2, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

		return rootLayout;
	}

	private void initializePlayerCells(GridView view, String[][] cells) {
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				CellView c = new CellView(getActivity(), x, y);

				// Set color of cell to gray if a ship, blue otherwise
				c.setBackgroundColor(cells[y][x].equals(Player.SHIP) ? Color.DKGRAY : Color.BLUE);

				c.setOnCellTouchedListener(new CellView.OnCellTouchedListener() {
					@Override
					public void onCellTouched(int x, int y) {
						Toast.makeText(getActivity(), "(" + x + ", " + y + ")", Toast.LENGTH_SHORT).show();
					}
				});
				view.addView(c);
			}
		}
	}
}
