package utils;


public class Position {
	private int row;
	private int col;
	private final static int CONST=1000000007;
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
		return "(" + col +"," +row+")";
	}
	
	public Position left() {
		return new Position(row, col - 1);
	}
	
	public Position right() {
		return new Position(row, col + 1);
	}

	public Position[] adjacentPositions() {
		Position[] neighbors = new Position[8];
		int index = 0;
		for (int r = -1; r <= 1; r++) {
			for (int c = -1; c <= 1; c++) {
				if (r != 0 || c != 0) {
					neighbors[index++] = new Position(r + row, c + col);
				}
			}
		}
		return neighbors;
	}
	
	public boolean isHorizontallyAligned(Position p) {
		return row == p.row();
	}
	
	public boolean isVerticallyAligned(Position p) {
		return col == p.column();
	}
	@Override
	public int hashCode() {
		return CONST *row+col;
	}
	public boolean equals(Position other) {
		return other.row()==row() && other.column()==column();
	}
}
