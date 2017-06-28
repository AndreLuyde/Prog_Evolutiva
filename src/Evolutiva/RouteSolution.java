package Evolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RouteSolution {

	private ArrayList<Pontos> solution = new ArrayList<Pontos>();
	private int fitness;
	Random r = new Random();

	public RouteSolution(ArrayList<Pontos> pontos) {
		Collections.shuffle(pontos);
		this.setSolution(pontos);
	}

	// mostra a solucao
	public void printRoutSolution(ArrayList<Pontos> solution) {
		// for (Pontos client : solution) {
		for (int i = 0; i < solution.size(); i++) {
			System.out.println("Cliente: " + solution.get(i) + "Horário de coleta: " + solution.get(i).getTime());
		}
	}

	// Retorna o tempo de duração da rota
	public int evoluate(RouteProblem problem, ArrayList<Pontos> pontos, int actualTime) {
		int durationRout = 0;
		ArrayList<Pontos> rota = new ArrayList<Pontos>();
		for (Pontos client : pontos) {
			rota.add(getNextPointByTime(actualTime, pontos));
			durationRout += client.getTime() - actualTime;
			actualTime = client.getTime();
			client.setTime(25);
		}
		problem.setSolution(this);
		return durationRout;
	}

	// Cruzamento de rota
	public ArrayList<ArrayList<Pontos>> crossingRoute2Cut(RouteSolution individuo1, RouteSolution individuo2) {
		// sorteia pontos de corte
		int pontoCorte1 = r.nextInt((individuo1.getSolution().size() / 2) - 2) + 1;
		int pontoCorte2 = r.nextInt((individuo1.getSolution().size() / 2) - 2) + individuo1.getSolution().size() / 2;

		ArrayList<ArrayList<Pontos>> filhos = new ArrayList<ArrayList<Pontos>>();

		// pega os genes dos pais
		ArrayList<Pontos> genePai1 = individuo1.getSolution();
		ArrayList<Pontos> genePai2 = individuo2.getSolution();
		ArrayList<Pontos> geneFilho1 = new ArrayList<Pontos>();
		ArrayList<Pontos> geneFilho2 = new ArrayList<Pontos>();

		// realiza o corte,
		for (int i = 0; i < genePai1.size(); i++) {
			if (i > pontoCorte1 && i <= pontoCorte2) {
				geneFilho1.add(genePai2.get(i));
				geneFilho2.add(genePai1.get(i));
			}
		}
		for (int i = 0; i < genePai1.size(); i++) {
			if (!geneFilho1.contains(genePai2.get(i))) {
				geneFilho1.add(genePai2.get(i));
			}
			if (!geneFilho2.contains(genePai1.get(i))) {
				geneFilho2.add(genePai1.get(i));
			}
		}
		filhos.add(geneFilho1);
		filhos.add(geneFilho2);
		return filhos;
	}

	public ArrayList<ArrayList<Pontos>> crossingRoute1Cut(RouteSolution individuo1, RouteSolution individuo2) {
		// sorteia o ponto de corte
		int pontoCorte = r.nextInt((individuo1.getSolution().size() / 2) - 2) + individuo1.getSolution().size() / 2;

		ArrayList<ArrayList<Pontos>> filhos = new ArrayList<ArrayList<Pontos>>();

		// pega os genes dos pais
		ArrayList<Pontos> genePai1 = individuo1.getSolution();
		ArrayList<Pontos> genePai2 = individuo2.getSolution();
		ArrayList<Pontos> geneFilho1 = new ArrayList<Pontos>();
		ArrayList<Pontos> geneFilho2 = new ArrayList<Pontos>();

		// realiza o corte,
		for (int i = 0; i < genePai1.size(); i++) {
			if (i > pontoCorte) {
				geneFilho1.add(genePai2.get(i));
				geneFilho2.add(genePai1.get(i));
			}
		}
		for (int i = 0; i < genePai1.size(); i++) {
			if (!geneFilho1.contains(genePai2.get(i))) {
				geneFilho1.add(genePai2.get(i));
			}
			if (!geneFilho2.contains(genePai1.get(i))) {
				geneFilho2.add(genePai1.get(i));
			}
		}
		filhos.add(geneFilho1);
		filhos.add(geneFilho2);
		return filhos;
	}

	// Mutacao da rota
	public RouteSolution mutationRoute1(RouteSolution solution) {
		Pontos temp = new Pontos();
		int pontoAleatorio = r.nextInt((solution.getSolution().size()) - 2);
		temp = solution.getSolution().get(pontoAleatorio);
		solution.getSolution().set(pontoAleatorio, solution.getSolution().get(solution.getSolution().size() - 1));
		solution.getSolution().set(solution.getSolution().size() - 1, temp);
		return solution;
	}

	public RouteSolution mutationRouteByChange(RouteSolution solution) {
		Pontos temp = new Pontos();
		int pontoAleatorio1 = r.nextInt((solution.getSolution().size()) - 1);
		int pontoAleatorio2 = r.nextInt((solution.getSolution().size()) - 1);
		temp = solution.getSolution().get(pontoAleatorio1);
		solution.getSolution().set(pontoAleatorio1, solution.getSolution().get(pontoAleatorio2));
		solution.getSolution().set(pontoAleatorio2, temp);
		return solution;
	}

	public RouteSolution mutationRouteByShuffle(RouteSolution solution) {
		int pontoAleatorio1 = r.nextInt((solution.getSolution().size()) - 1);
		int pontoAleatorio2 = r.nextInt((solution.getSolution().size()) - 1);
		ArrayList<Pontos> listaPontos = new ArrayList<Pontos>();
		if (pontoAleatorio2 < pontoAleatorio1) {
			int aux = pontoAleatorio2;
			pontoAleatorio1 = pontoAleatorio2;
			pontoAleatorio2 = aux;
		}
		for (int i = pontoAleatorio1; i < pontoAleatorio2; i++) {
			listaPontos.add(solution.getSolution().get(i));
		}
		Collections.shuffle(listaPontos);
		int k = 0;
		for (int i = pontoAleatorio1; i < pontoAleatorio2; i++) {
			solution.getSolution().set(i, listaPontos.get(k));
			k++;
		}
		return solution;
	}

	// retorna o cliente mais proximo pelo tempo de coleta
	public Pontos getNextPointByTime(int time, ArrayList<Pontos> pontos) {
		Pontos clientReturn = new Pontos();
		int smallTime = 25;
		for (Pontos cli : pontos) {
			if (cli.getTime() > time && cli.getTime() < smallTime) {
				clientReturn = cli;
				smallTime = cli.getTime();
			}
		}
		return clientReturn;
	}

	public ArrayList<Pontos> getSolution() {
		return solution;
	}

	public void setSolution(ArrayList<Pontos> solution) {
		this.solution = solution;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

}
