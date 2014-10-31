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

		GridView player1PlayerGrid = new GridView(getActivity());
		GridView player1OpponentGrid = new GridView(getActivity());
		GridView player2PlayerGrid = new GridView(getActivity());
		GridView player2OpponentGrid = new GridView(getActivity());

		Player player1 = new Player();
		Player player2 = new Player();

		String[][] shipCellsPlayer1 = player1.getShipCells();
		String[][] shipCellsPlayer2 = player2.getShipCells();
		String[][] blankCells = player1.getOpponentCells();

		initializePlayerShipCells(player1PlayerGrid, shipCellsPlayer1);
		initializePlayerShipCells(player1OpponentGrid, blankCells);
		initializePlayerShipCells(player2PlayerGrid, shipCellsPlayer2);
		initializePlayerShipCells(player2OpponentGrid, blankCells);

		rootLayout.addView(player1PlayerGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		rootLayout.addView(player1OpponentGrid, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

		return rootLayout;
	}

	private void initializePlayerShipCells(GridView view, String[][] cells) {
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
