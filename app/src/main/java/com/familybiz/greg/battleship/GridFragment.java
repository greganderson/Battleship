package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Greg Anderson
 */
public class GridFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		GridView gridView = new GridView(getActivity());
		gridView.setBackgroundColor(Color.BLACK);

		for (int i = 0; i < 100; i++)
			gridView.addView(new CellView(getActivity()));

		return gridView;
	}
}
