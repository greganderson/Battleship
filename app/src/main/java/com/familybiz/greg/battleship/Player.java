package com.familybiz.greg.battleship;

/**
 * Created by Greg Anderson
 */
public class Player {

	public interface OnGridChangedListener {
		public void onGridChanged(int i, int j);
	}
	OnGridChangedListener mOnGridChangedListener = null;
	public void setOnGridChangedListener(OnGridChangedListener onGridChangedListener) {
		mOnGridChangedListener = onGridChangedListener;
	}
	public OnGridChangedListener getOnGridChangedListener() {
		return mOnGridChangedListener;
	}

	private final String SHIP = "ship";
	private final String WATER = "water";

	private Cell[][] cells;

	public Player() {
		int GRID_HEIGHT = 10;
		int GRID_WIDTH = 10;
		cells = new Cell[GRID_WIDTH][GRID_HEIGHT];

		for (int i = 0; i < GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_WIDTH; j++) {
				Cell c = new Cell();
				c.cellType = WATER;
				cells[i][j] = c;
			}
		}
	}

	public boolean missileShot(int i, int j) {
		if (cells[i][j].isShot)
			return false;
		cells[i][j].isShot = true;
		if (mOnGridChangedListener != null)
			mOnGridChangedListener.onGridChanged(i, j);
		return true;
	}


	/**
	 * Represents one cell on the battleship grid.  Contains the type of cell (cell contains part of
	 * a ship or cell is water) as well as whether the cell has been shot at.
	 */
	private class Cell {
		public String cellType = "none";
		public boolean isShot = false;
	}
}
