package Evolutiva;
import java.util.ArrayList;

public class Client {
	private int time;
	private int position;
	private int distance;
	ArrayList<Client> neighborhood = new ArrayList<Client>();
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public ArrayList<Client> getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(ArrayList<Client> neighborhood) {
		this.neighborhood = neighborhood;
	}
}
