package utils;

public class Position {
	private int row;
	private int col;
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int row() {
		return row;
	}
	
	public int column() {
		return col;
	}
	
	public String toString() {
		return "";	//Placeholder
	}
	
	public Position left() {
		return new Position(row, col - 1);
	}
	
	public Position right() {
		return new Position(row, col + 1);
	}
	
	public boolean isHorizantallyAligned(Position p) {
		return row == p.row();
	}
	
	public boolean isVerticallyAligned(Position p) {
		return col == p.column();
	}
}
