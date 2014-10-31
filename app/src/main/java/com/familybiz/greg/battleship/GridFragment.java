package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Greg Anderson
 */
public class GridFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		GridView gridView = new GridView(getActivity());
		gridView.setBackgroundColor(Color.BLACK);

		Player player = new Player();
		String[][] cells = player.getCells();

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
				gridView.addView(c);
			}
		}

		return gridView;
	}
}
