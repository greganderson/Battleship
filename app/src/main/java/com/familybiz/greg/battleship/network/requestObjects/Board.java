package com.familybiz.greg.battleship.network.requestObjects;

/**
 * Created by Greg Anderson
 */
public class Board {

	public Cell[][] cells;

	public void addCell(int x, int y) {
		cells[y][x] = new Cell();
	}

	public class Cell {
		public int xPos;
		public int yPos;
		public String status;
	}
}
