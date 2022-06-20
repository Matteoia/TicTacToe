import java.io.Serializable;

public class Coordinate implements Serializable{
	private static final long serialVersionUID = 113L;
	private int x;
	private int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "<"+x+","+y+">";
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Coordinate))
			throw new IllegalArgumentException();
		Coordinate c = (Coordinate)o;
		if(this.x == c.x && this.y == c.y)
			return true;
		return false;
	}

}
