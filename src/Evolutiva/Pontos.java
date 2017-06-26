package Evolutiva;
import java.util.ArrayList;

public class Pontos {
	private int time;
	private int rotulo;
	private int pontoX;
	private int pontoY;
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
	public int getRotulo() {
		return rotulo;
	}
	public void setRotulo(int rotulo) {
		this.rotulo = rotulo;
	}
	public int getPontoX() {
		return pontoX;
	}
	public void setPontoX(int pontoX) {
		this.pontoX = pontoX;
	}
	public int getPontoY() {
		return pontoY;
	}
	public void setPontoY(int pontoY) {
		this.pontoY = pontoY;
	}
}
