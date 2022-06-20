import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tavolo implements Serializable{
	private static final long serialVersionUID = 439L;
	private int[][] matrice;
	private List<Coordinate> prob;

	public Tavolo(int[][] stato) {
		this.prob = new ArrayList<>();
		this.matrice = new int[stato.length][stato.length];
		for(int i=0; i<stato.length; i++) 
			for(int j=0; j<stato.length; j++) {
				this.matrice[i][j] = stato[i][j];
				if(stato[i][j] == 0) 
					for(int k=0; k<3; k++) 
						prob.add(new Coordinate(i, j));
			}
	}
	
	public void posiziona(int x, int y, int a) {
		matrice[x][y] = a;
	}
	
	public int[][] getMatrice(){
		return matrice;
	}
	
	public List<Coordinate> getProb(){
		return prob;
	}
	
	public int getStato(int x, int y) {
		if(x < matrice.length && y<matrice.length)
			return matrice[x][y];
		return -1;
	}
	
	public int getVincitore() {
		for(int i=0; i< matrice.length; i++){
			if(matrice[i][0] != 0 && matrice[i][0] == matrice[i][1] && matrice[i][0] == matrice[i][2])
				return matrice[i][0];
			if(matrice[0][i] != 0 && matrice[0][i] == matrice[1][i] && matrice[0][i] == matrice[2][i])
				return matrice[0][i];
		}
		if(matrice[0][0] != 0 && matrice[0][0] == matrice[1][1] && matrice[0][0] == matrice[2][2]){
			return matrice[0][0];
		}

		if(matrice[0][2] != 0 && matrice[0][2] == matrice[1][1] && matrice[0][2] == matrice[2][0])
			return matrice[0][2];
		for(int i=0; i<matrice.length; i++)
			for (int j=0; j< matrice.length; j++) {
				if(matrice[i][j] == 0)
					return -1;
			}
		return 0;
	}
	
	public void setProb(List<Coordinate> prob) {
		this.prob = prob;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Tavolo))
			return false;
		Tavolo t = (Tavolo)o;
		if(t == this) return true;
		for(int i=0; i<t.matrice.length; i++) 
			for(int j=0; j<t.matrice[i].length; j++) 
				if(this.matrice[i][j] != t.matrice[i][j])
					return false;
		return true;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<matrice.length; i++){
			for(int j=0; j< matrice.length; j++){
				sb.append(matrice[i][j]+" ");
			}
			sb.append("\n");
		}
		sb.append("  "+prob);
		return sb.toString();
	}

	public static Coordinate getCoordinateMossa(Tavolo pre, Tavolo corr) {
		for(int i=0; i<pre.matrice.length; i++) {
			for(int j=0; j<pre.matrice[i].length; j++) {
				if(corr.matrice[i][j] == 2 && pre.matrice[i][j] == 0)
					return new Coordinate(i, j);
			}
		}
		return null;
	}
}
