package com.familybiz.greg.battleship.network.requestObjects;

/**
 * Created by Greg Anderson
 */
public class BoardData {

	public CellData[][] cells;

	public void addCell(int x, int y) {
		cells[y][x] = new CellData();
	}

	public class CellData {
		public int xPos;
		public int yPos;
		public String status;
	}
}
