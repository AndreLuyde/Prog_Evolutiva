package Evolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GA {

	private double crossProbability;
	private double mutationProbability;
	private RouteProblem problem;
	private int gen = 0;
	private RouteSolution bestSolution;
	private ArrayList<RouteSolution> populacao = new ArrayList<RouteSolution>();
	Random r = new Random();

	public GA(RouteProblem problem, int populationSize) {
		this.setBestSolution(problem.getSolution());
		this.setProblem(problem);
		this.setMutationProbability(1 / problem.getSolution().getSolution().size());
		this.setCrossProbability(0.9);
		for (int i = 0; i < populationSize; i++) {
			Collections.shuffle(problem.getPontos());
			this.getPopulacao().get(i).setSolution(problem.getPontos());
		}
	}

	public void run() {
		while (stopCriteria(this.bestSolution)) {
//			evolutionaryCicle(12);
		}
	}

	public boolean stopCriteria(RouteSolution solution) {
		if (solution.getTimeSolution() < 10) {
			return true;
		} else {
			return false;
		}
	}

	public void evolutionaryCicle(ArrayList<RouteSolution> populacao) {
		//coloca em cada solução o fitness dela
		for (RouteSolution routeSolution : populacao) {
			fitness(routeSolution);
		}
		
		int indice1 = (int) (0 + Math.random() * populacao.size());
		int indice2 = (int) (0 + Math.random() * populacao.size());
		crossingRoute2Cut(populacao.get(indice1), populacao.get(indice2));
		selecao(populacao, 20);
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

	// Mutação
	public RouteSolution mutationRouteByChange(RouteSolution solution) {
		Pontos temp = new Pontos();
		int pontoAleatorio1 = r.nextInt((solution.getSolution().size()) - 1);
		int pontoAleatorio2 = r.nextInt((solution.getSolution().size()) - 1);
		temp = solution.getSolution().get(pontoAleatorio1);
		solution.getSolution().set(pontoAleatorio1, solution.getSolution().get(pontoAleatorio2));
		solution.getSolution().set(pontoAleatorio2, temp);
		return solution;
	}

	public ArrayList<RouteSolution> selecao(ArrayList<RouteSolution> populacao, int tamanhoPopulacao) {
		ArrayList<RouteSolution> novasSulucoes = new ArrayList<RouteSolution>();
		RouteSolution aleatorio1 = null;
		RouteSolution aleatorio2 = null;
		for (RouteSolution routeSolution : novasSulucoes) {
			populacao.add(routeSolution);
		}
		ArrayList<RouteSolution> populacaoFinal = new ArrayList<RouteSolution>();
		for (int i = 0; i < tamanhoPopulacao; i++) {
			int n = (int) (0 + Math.random() * populacao.size());
			aleatorio1 = populacao.get(n);
			n = (int) (0 + Math.random() * populacao.size());
			aleatorio2 = populacao.get(n);
			if (fitness(aleatorio1) < fitness(aleatorio2)) {
				populacaoFinal.add(aleatorio1);
			} else {
				populacaoFinal.add(aleatorio2);
			}
		}
		return populacaoFinal;
	}

	public RouteSolution torneio(RouteSolution solucao1, RouteSolution solucao2){
		if(solucao1.getTimeSolution() == 0){
			fitness(solucao1);
		}
		if(solucao2.getTimeSolution() == 0){
			fitness(solucao2);
		}
		if(solucao1.getTimeSolution() > solucao2.getTimeSolution()){
			return solucao2;
		}else{
			return solucao1;
		}
	}

	private int fitness(RouteSolution solucao) {
		int distanciaTotal = 0;
		for (int i = 0; i < solucao.getSolution().size(); i++) {
			distanciaTotal += solucao.getSolution().get(i).getDistancias().get(i);
		}
		solucao.setTimeSolution(distanciaTotal);
		return distanciaTotal;
	}

	public RouteSolution getBestSolution() {
		return bestSolution;
	}

	public void setBestSolution(RouteSolution bestSolution) {
		this.bestSolution = bestSolution;
	}

	public double getCrossProbability() {
		return crossProbability;
	}

	public void setCrossProbability(double crossProbability) {
		this.crossProbability = crossProbability;
	}

	public double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	public RouteProblem getProblem() {
		return problem;
	}

	public void setProblem(RouteProblem problem) {
		this.problem = problem;
	}

	public int getGen() {
		return gen;
	}

	public void setGen(int gen) {
		this.gen = gen;
	}

	public ArrayList<RouteSolution> getPopulacao() {
		return populacao;
	}

	public void setPopulacao(ArrayList<RouteSolution> populacao) {
		this.populacao = populacao;
	}

}
