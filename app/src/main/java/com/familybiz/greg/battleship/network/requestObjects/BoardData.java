package com.familybiz.greg.battleship.network.requestObjects;

/**
 * Created by Greg Anderson
 */
public class BoardData {

	public CellData[][] cells;

	public BoardData() {
		cells = new CellData[10][10];
	}

	public void addCell(int x, int y) {
		cells[y][x] = new CellData();
		cells[y][x].xPos = x;
		cells[y][x].yPos = y;
		cells[y][x].status = "";
	}

	public class CellData {
		public int xPos;
		public int yPos;
		public String status;
	}
}
