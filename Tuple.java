package Sparsevector.solved;

public class Tuple<T> {
	public T thing;
	public int row;
	public Tuple(int row, T thing) {
		this.row = row;
		this.thing = thing;
	}

	public int compare(Tuple<T> other) {
		if (this.row < other.row)
			return -1;
		else if (this.row == other.row)
			return 0;
		else {
			return 1;
		}
	}
	
	public String toString() {
		return "(" + row + ", " + thing + ")";
	}
}
