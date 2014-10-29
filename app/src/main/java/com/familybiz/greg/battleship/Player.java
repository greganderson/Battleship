package com.familybiz.greg.battleship;

import java.util.Random;

/**
 * Created by Greg Anderson
 */
public class Player {

	public interface OnGridChangedListener {
		public void onGridChanged(int x, int y, boolean isHit);
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
	private final int GRID_HEIGHT = 10;
	private final int GRID_WIDTH = 10;

	private Cell[][] cells;

	public boolean isShip(int i, int j) {
		return cells[i][j].cellType.equals(SHIP);
	}

	public Player() {
		cells = new Cell[GRID_WIDTH][GRID_HEIGHT];

		for (int x = 0; x < GRID_HEIGHT; x++) {
			for (int y = 0; y < GRID_WIDTH; y++) {
				Cell c = new Cell();
				c.cellType = WATER;
				cells[x][y] = c;
			}
		}

		initializeShips();
	}

	private void initializeShips() {
		Random rand = new Random();

		// Set up ship lengths
		int[] shipSizes = {2, 3, 3, 4, 5};

		for (int shipSize : shipSizes) {
			boolean shipComplete = false;
			while (!shipComplete) {
				shipComplete = true;

				// Pick random cell location
				int randomX = rand.nextInt(GRID_WIDTH);
				int randomY = rand.nextInt(GRID_HEIGHT);

				// Pick which direction to try putting the ship
				boolean tryRight = rand.nextBoolean();

				// Attempt to place the ship
				for (int i = 0; i < shipSize; i++) {
					Cell currentCell;

					// Catch index out of bounds exception to try a different location
					try {
						// Pick the cell in the specified direction
						if (tryRight)
							currentCell = cells[randomX][randomY + i];
						else
							currentCell = cells[randomX + i][randomY];
					}
					catch (ArrayIndexOutOfBoundsException e) {
						shipComplete = false;

						// Reset the cells that got set during this attempt
						resetShipCells(randomX, randomY, i, tryRight);

						// Start over
						break;
					}

					// Check if the current cell already contains part of a ship
					if (currentCell.cellType.equals(SHIP)) {
						shipComplete = false;

						// Reset the cells that got set during this attempt
						resetShipCells(randomX, randomY, i, tryRight);

						// Start over
						break;
					}

					// Everything good so far, set cell to be part of ship
					currentCell.cellType = SHIP;
				}
			}
		}
	}

	private void resetShipCells(int x, int y, int i, boolean tryRight) {
		Cell currentCell;
		// Reset the cells that got set during this attempt
		i--;
		for (; i >= 0; i--) {
			if (tryRight)
				currentCell = cells[x][y + i];
			else
				currentCell = cells[x + i][y];
			currentCell.cellType = WATER;
		}
	}

	/**
	 * Returns true if the cell has NOT already been shot at, false otherwise.  Triggers the grid
	 * changed listener.
	 */
	public boolean missileShot(int x, int y) {
		if (cells[x][y].isShot)
			return false;
		cells[x][y].isShot = true;
		if (mOnGridChangedListener != null)
			mOnGridChangedListener.onGridChanged(x, y, cells[x][y].cellType.equals(SHIP));
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
