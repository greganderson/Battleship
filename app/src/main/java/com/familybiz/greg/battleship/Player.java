package com.familybiz.greg.battleship;

/**
 * Created by Greg Anderson
 */
public class Player {

	public interface OnGridChangedListener {
		public void onGridChanged(int x, int y);
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

		for (int x = 0; x < GRID_HEIGHT; x++) {
			for (int y = 0; y < GRID_WIDTH; y++) {
				Cell c = new Cell();
				c.cellType = WATER;
				cells[x][y] = c;
			}
		}
	}

	public boolean missileShot(int x, int y) {
		if (cells[x][y].isShot)
			return false;
		cells[x][y].isShot = true;
		if (mOnGridChangedListener != null)
			mOnGridChangedListener.onGridChanged(x, y);
		return true;
	}

	public boolean isCellAShip(int x, int y) {
		return cells[x][y].cellType.equals(SHIP);
	}

	public boolean hasCellBeenShot(int x, int y) {
		return cells[x][y].isShot;
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
