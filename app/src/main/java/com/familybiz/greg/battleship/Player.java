package com.familybiz.greg.battleship;

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
	}

	public Player(String[][] shipCells, String[][] opponentCells) {
		initializeCells();

		setCells(shipCells, this.playerCells);
		setCells(opponentCells, this.opponentCells);

		// Set the number of hits
		for (int y = 0; y < GRID_HEIGHT; y++)
			for (int x = 0; x < GRID_WIDTH; x++)
				if(this.opponentCells[y][x].cellType.equals(HIT) || this.opponentCells[y][x].cellType.equals(MISS))
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
		for (int y = 0; y < GRID_HEIGHT; y++)
			for (int x = 0; x < GRID_WIDTH; x++)
				destCells[y][x].cellType = srcCells[y][x];
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
		boolean invalidShot = playerCells[y][x].cellType.equals(HIT) || playerCells[y][x].cellType.equals(MISS);
		if (invalidShot)
			return false;
		if (playerCells[y][x].cellType.equals(SHIP)) {
			playerCells[y][x].cellType = HIT;
			numberOfHits++;
		}
		else
			playerCells[y][x].cellType = MISS;

		return true;
	}

	/**
	 * Changes the opponent's grid according to the missile fired.
	 */
	public void playerShotMissile(int x, int y, boolean hit) {
		opponentCells[y][x].cellType = hit ? HIT : MISS;
	}

	/**
	 * Represents one cell on the battleship grid.  Contains the type of cell (cell contains part of
	 * a ship or cell is water) as well as whether the cell has been shot at.
	 */
	public class Cell {
		public String cellType = WATER;     // Either water or a ship
	}
}
