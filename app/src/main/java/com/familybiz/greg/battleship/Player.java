package com.familybiz.greg.battleship;

import java.util.Random;

/**
 * Created by Greg Anderson
 */
public class Player {

	public static final String SHIP = "ship";
	public static final String WATER = "water";
	public static final String HIT = "hit";
	public static final String MISS = "miss";
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

	private Cell[][] playerCells;
	private Cell[][] opponentCells;

	private int numberOfShipCells = 2 + 3 + 3 + 4 + 5;
	private int numberOfHits = 0;

	public Player() {
		initializeCells();
		initializeShips();
	}

	public Player(String[][] shipCells, String[][] opponentCells) {
		initializeCells();

		setCells(shipCells, this.playerCells);
		setCells(opponentCells, this.opponentCells);

		// Set the number of hits
		for (int y = 0; y < GRID_HEIGHT; y++)
			for (int x = 0; x < GRID_WIDTH; x++)
				if (this.opponentCells[y][x].isShot)
					numberOfHits++;
	}

	private void initializeCells() {
		playerCells = new Cell[GRID_WIDTH][GRID_HEIGHT];
		opponentCells = new Cell[GRID_WIDTH][GRID_HEIGHT];

		// Initialize all cells to water
		Cell c;
		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < GRID_WIDTH; x++) {
				// Player's cells
				c = new Cell();
				playerCells[y][x] = c;

				// Opponent's cells
				c = new Cell();
				opponentCells[y][x] = c;
			}
		}
	}

	private void setCells(String[][] srcCells, Cell[][] destCells) {
		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < GRID_WIDTH; x++) {
				destCells[y][x].cellType = srcCells[y][x];
				destCells[y][x].isShot = srcCells[y][x].equals(HIT);
			}
		}
	}

	/**
	 * Returns a 2D array of strings containing the cell types.
	 */
	public String[][] getShipCells() {
		return getCells(true);
	}

	public String[][] getOpponentCells() {
		return getCells(false);
	}

	private String[][] getCells(boolean getShipCells) {
		String[][] cells = new String[GRID_WIDTH][GRID_HEIGHT];
		for (int y = 0; y < playerCells.length; y++)
			for (int x = 0; x < playerCells.length; x++)
				cells[y][x] = getShipCells ? playerCells[y][x].cellType : opponentCells[y][x].cellType;
		return cells;
	}

	/**
	 * Returns true if the cell has NOT already been shot at, false otherwise.  Triggers the grid
	 * changed listener.
	 */
	public boolean opponentShotMissile(int x, int y) {
		// Check if cell has already been shot at
		if (playerCells[y][x].isShot)
			return false;
		if (playerCells[y][x].cellType.equals(SHIP))
			numberOfHits++;

		playerCells[y][x].isShot = true;

		if (mOnPlayerGridChangedListener != null)
			mOnPlayerGridChangedListener.onPlayerGridChanged(x, y, playerCells[y][x].cellType.equals(SHIP));

		if (numberOfHits == numberOfShipCells)
			if (mOnAllShipsDestroyedListener != null)
				mOnAllShipsDestroyedListener.onAllShipsDestroyed();

		return true;
	}

	/**
	 * Changes the opponent's grid according to the missile fired.
	 */
	public void playerShotMissile(int x, int y, boolean hit) {
		opponentCells[y][x].isShot = true;
		opponentCells[y][x].cellType = hit ? HIT : MISS;
	}

	/**
	 * Randomly place the ships.
	 */
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
							currentCell = playerCells[randomY][randomX + i];
						else
							currentCell = playerCells[randomY + i][randomX];
					}
					catch (ArrayIndexOutOfBoundsException e) {
						shipComplete = false;

						// Reset the playerCells that got set during this attempt
						resetShipCells(randomX, randomY, i, tryRight);

						// Start over
						break;
					}

					// Check if the current cell already contains part of a ship
					if (currentCell.cellType.equals(SHIP)) {
						shipComplete = false;

						// Reset the playerCells that got set during this attempt
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


	/**
	 * Helper method for randomizing the ship locations.
	 */
	private void resetShipCells(int x, int y, int i, boolean tryRight) {
		Cell currentCell;
		// Reset the playerCells that got set during this attempt
		i--;
		for (; i >= 0; i--) {
			if (tryRight)
				currentCell = playerCells[y][x + i];
			else
				currentCell = playerCells[y + i][x];
			currentCell.cellType = WATER;
		}
	}


	/**
	 * Represents one cell on the battleship grid.  Contains the type of cell (cell contains part of
	 * a ship or cell is water) as well as whether the cell has been shot at.
	 */
	public class Cell {
		public String cellType = WATER;     // Either water or a ship
		public boolean isShot = false;      // True if the cell has already been shot at
	}


	/****************** LISTENERS ******************/


	// Player's grid
	public interface OnPlayerGridChangedListener {
		public void onPlayerGridChanged(int x, int y, boolean isHit);
	}
	OnPlayerGridChangedListener mOnPlayerGridChangedListener = null;
	public void setOnPlayerGridChangedListener(OnPlayerGridChangedListener onPlayerGridChangedListener) {
		mOnPlayerGridChangedListener = onPlayerGridChangedListener;
	}
	public OnPlayerGridChangedListener getPlayerOnGridChangedListener() {
		return mOnPlayerGridChangedListener;
	}

	// All ships have been destroyed
	public interface OnAllShipsDestroyedListener {
		public void onAllShipsDestroyed();
	}
	private OnAllShipsDestroyedListener mOnAllShipsDestroyedListener = null;
	public void setOnAllShipsDestroyedListener(OnAllShipsDestroyedListener onAllShipsDestroyedListener) {
		mOnAllShipsDestroyedListener = onAllShipsDestroyedListener;
	}
	public OnAllShipsDestroyedListener getOnAllShipsDestroyedListener() {
		return mOnAllShipsDestroyedListener;
	}
}
