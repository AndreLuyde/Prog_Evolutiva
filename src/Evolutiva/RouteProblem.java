package Evolutiva;
import java.util.ArrayList;

//Problema Dial-a-Ride buscando melhor rota respeitanto horario marcado de cada ponto e diminuindo tempo de espera 
public class RouteProblem {

	private int numClients;
	
	private ArrayList<Pontos> pontos = new ArrayList<Pontos>();
	
	private RouteSolution solution;

	//	construtor recebe a quantidade de clientes da rota
	public RouteProblem(int numClients, ArrayList<Pontos> pontos){
		setNumClients(numClients);
		setPontos(pontos);
	}
	//Mostra os clientes e o horario que se deve buscar cada um
	public void printRoutProbelem(RouteProblem problem){
		for (int i=0;i<problem.getPontos().size();i++){	
			System.out.println("Cliente: "+problem.getPontos().get(i) + "Horário de coleta: "+problem.getPontos().get(i).getTime());
		}
	}

	public int getNumClients() {
		return numClients;
	}
	public void setNumClients(int numClients) {
		this.numClients = numClients;
	}
		
	public ArrayList<Pontos> getPontos() {
		return pontos;
	}
	public void setPontos(ArrayList<Pontos> pontos) {
		this.pontos = pontos;
	}
	public RouteSolution getSolution() {
		return solution;
	}
	public void setSolution(RouteSolution solution) {
		this.solution = solution;
	}
	
}