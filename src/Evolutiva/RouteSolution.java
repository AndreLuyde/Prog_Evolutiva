package Evolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RouteSolution {

	private ArrayList<Pontos> solucao = new ArrayList<Pontos>();
//	private ArrayList<Pontos> solucao;
	private int fitness;
	Random r = new Random();

	public RouteSolution(ArrayList<Pontos> pontos) {
		Collections.shuffle(pontos);
		this.setSolucao(pontos);
	}
	
	public RouteSolution(RouteSolution solucao) {
		setSolucao(solucao.getSolucao());
		setFitness(solucao.getFitness());
	}

	// método estático que embaralha os elementos de um vetor de inteiros
	public static void embaralhar(Pontos[] v) {
		Random random = new Random();

		for (int i = 0; i < (v.length - 1); i++) {
			// sorteia um índice
			int j = random.nextInt(v.length);

			// troca o conteúdo dos índices i e j do vetor
			Pontos temp = v[i];
			v[i] = v[j];
			v[j] = temp;
		}
	}

	// mostra a solucao
	public void printRoutSolution(Pontos[] solution) {
		// for (Pontos client : solution) {
		for (int i = 0; i < solution.length; i++) {
			System.out.println("Cliente: " + solution[i] + "Horario de coleta: " + solution[i].getTime());
		}
	}

	// Retorna o tempo de duraÃ§Ã£o da rota
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
		int pontoCorte1 = r.nextInt((individuo1.getSolucao().size() / 2) - 2) + 1;
		int pontoCorte2 = r.nextInt((individuo1.getSolucao().size() / 2) - 2) + individuo1.getSolucao().size() / 2;

		ArrayList<ArrayList<Pontos>> filhos = new ArrayList<ArrayList<Pontos>>();

		// pega os genes dos pais
		ArrayList<Pontos> genePai1 = individuo1.getSolucao();
		ArrayList<Pontos> genePai2 = individuo2.getSolucao();
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
		int pontoCorte = r.nextInt((individuo1.getSolucao().size() / 2) - 2) + individuo1.getSolucao().size() / 2;

		ArrayList<ArrayList<Pontos>> filhos = new ArrayList<ArrayList<Pontos>>();

		// pega os genes dos pais
		ArrayList<Pontos> genePai1 = individuo1.getSolucao();
		ArrayList<Pontos> genePai2 = individuo2.getSolucao();
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
		int pontoAleatorio = r.nextInt((solution.getSolucao().size()) - 2);
		temp = solution.getSolucao().get(pontoAleatorio);
		solution.getSolucao().set(pontoAleatorio, solution.getSolucao().get(solution.getSolucao().size()-1));
		solution.getSolucao().set(solution.getSolucao().size()-1, temp);
		return solution;
	}

	public RouteSolution mutationRouteByChange(RouteSolution solution) {
		Pontos temp = new Pontos();
		int pontoAleatorio1 = r.nextInt((solution.getSolucao().size()) - 1);
		int pontoAleatorio2 = r.nextInt((solution.getSolucao().size()) - 1);
		temp = solution.getSolucao().get(pontoAleatorio1);
		solution.getSolucao().set(pontoAleatorio1, solution.getSolucao().get(pontoAleatorio2));
		solution.getSolucao().set(pontoAleatorio2, temp);
		return solution;
	}

	public RouteSolution mutationRouteByShuffle(RouteSolution solution) {
		int pontoAleatorio1 = r.nextInt((solution.getSolucao().size()) - 1);
		int pontoAleatorio2 = r.nextInt((solution.getSolucao().size()) - 1);
		ArrayList<Pontos> listaPontos = new ArrayList<Pontos>();
		if (pontoAleatorio2 < pontoAleatorio1) {
			int aux = pontoAleatorio2;
			pontoAleatorio1 = pontoAleatorio2;
			pontoAleatorio2 = aux;
		}
		for (int i = pontoAleatorio1; i < pontoAleatorio2; i++) {
			listaPontos.add(solution.getSolucao().get(i));
		}
		Collections.shuffle(listaPontos);
		int k = 0;
		for (int i = pontoAleatorio1; i < pontoAleatorio2; i++) {
			solution.getSolucao().set(i, listaPontos.get(k));
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

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public ArrayList<Pontos> getSolucao() {
		return solucao;
	}

	public void setSolucao(ArrayList<Pontos> solucao) {
		this.solucao = solucao;
	}

//	public Pontos[] getSolucao() {
//		return solucao;
//	}
//
//	public void setSolucao(Pontos[] solucao) {
//		this.solucao = solucao;
//	}

}
