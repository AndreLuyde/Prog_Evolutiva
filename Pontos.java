package Evolutiva;
import java.util.ArrayList;

public class Pontos {
	private int time;
	private int rotulo;
	ArrayList<Pontos> neighborhood = new ArrayList<Pontos>();
	ArrayList<Integer> distancias = new ArrayList<Integer>();
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	public ArrayList<Integer> getDistancias() {
		return distancias;
	}
	public void setDistancias(ArrayList<Integer> distancias) {
		this.distancias = distancias;
	}
	public ArrayList<Pontos> getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(ArrayList<Pontos> neighborhood) {
		this.neighborhood = neighborhood;
	}
	public int getRotulo() {
		return rotulo;
	}
	public void setRotulo(int rotulo) {
		this.rotulo = rotulo;
	}
}
