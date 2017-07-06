package Evolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GA {

	private double crossProbability;
	private double mutationProbability;
	private RouteProblem problem;
	private int gen = 0;
	private int geracao = 0;
	private RouteSolution bestSolution;
	private ArrayList<RouteSolution> populacao = new ArrayList<RouteSolution>();
	Random r = new Random();

	public GA(RouteProblem problem, int populationSize) {
		this.setBestSolution(problem.getSolution());
		this.setProblem(problem);
		this.setMutationProbability(1 / problem.getNumClients());
		this.setCrossProbability(0.9);

		// inicializa a populacao
		for (int i = 0; i < populationSize; i++) {
			ArrayList<Pontos> pontos = new ArrayList<Pontos>(problem.getPontos());
			Collections.shuffle(pontos);
			RouteSolution solucaoTemp = new RouteSolution(pontos);
			getPopulacao().add(solucaoTemp);
		}
	}
	
	public void run(long timeFimExecucao, long tempoInicial) {
		evolutionaryCicle(getPopulacao(), timeFimExecucao, tempoInicial);
	}

	public void evolutionaryCicle(ArrayList<RouteSolution> populacao, long timeFimExecucao, long tempoInicial) {
		ArrayList<ArrayList<Pontos>> filhos = new ArrayList<ArrayList<Pontos>>();
		Double timeDouble = 0d;
		RouteSolution pai1 = null;
		RouteSolution pai2 = null;
		RouteSolution filho = null;
		// RouteSolution melhorSolucaoGeracao = null;
		int indice1 = -1;
		int indice2 = -1;
		int indice3 = -1;
		int indice4 = -1;

		// coloca em cada solu��o o fitness dela
		for (RouteSolution routeSolution : populacao) {
			fitness(routeSolution);
		}

		do {
			// sele��o de pontos para fazer sele��o para cruzamento
			indice1 = (int) (0 + Math.random() * populacao.size());
			indice2 = (int) (0 + Math.random() * populacao.size());
			indice3 = (int) (0 + Math.random() * populacao.size());
			indice4 = (int) (0 + Math.random() * populacao.size());

			// sele��o de pais para cruzamento
			pai1 = torneio(populacao.get(indice1), populacao.get(indice2));
			pai2 = torneio(populacao.get(indice3), populacao.get(indice4));

			// verifica probabilidade de cruzamento
			if (r.nextGaussian() < getCrossProbability()) {
				filhos = crossingRoute2Cut(pai1, pai2);
				for (ArrayList<Pontos> arrayList : filhos) {
					filho = new RouteSolution(arrayList);
					populacao.add(filho);
				}
			}

			// verifica possibilidade de muta��o
			if (r.nextGaussian() < getMutationProbability()) {
				populacao.add(mutationRouteByChange(pai1));
			}

			// selecao para proxima gera��o
			populacao = selecao(populacao, 100);
			setGeracao(getGeracao() + 1);

			// guarda melhor solu��o da gera��o
			// RouteSolution melhorSolucaoGeracao = null;
			RouteSolution melhorSolucaoGeracao = new RouteSolution(getMelhorSolucao(populacao));
			if (getBestSolution() == null) {
				setBestSolution(melhorSolucaoGeracao);
				// mostra melhor solu��o da gera��o
				System.out.println("Solu��o Da " + getGeracao() + " gera��o");
				for (Pontos ponto : melhorSolucaoGeracao.getSolucao()) {
					System.out.print("| " + ponto.getRotulo() + " ");
				}
				System.out.println("|");
				System.out.println("Fitness: " + melhorSolucaoGeracao.getFitness());

			} else if (melhorSolucaoGeracao.getFitness() < getBestSolution().getFitness()) {
				setBestSolution(melhorSolucaoGeracao);
				// mostra melhor solu��o da gera��o
				System.out.println("Solu��o Da " + getGeracao() + " gera��o");
				for (Pontos ponto : melhorSolucaoGeracao.getSolucao()) {
					System.out.print("| " + ponto.getRotulo() + " ");
				}
				System.out.println("|");
				System.out.println("Fitness: " + melhorSolucaoGeracao.getFitness());
			}

			// atualiza crit�rio de parada
			long actualTime = System.currentTimeMillis();
			long time = (actualTime - tempoInicial);
			timeDouble = Double.parseDouble((String.valueOf(time))) / 1000;

		} while (timeFimExecucao > timeDouble);

		System.out.println("Melhor Solu��o encontrada");
		for (Pontos ponto : getBestSolution().getSolucao()) {
			System.out.print("| " + ponto.getRotulo() + " ");
		}
		System.out.println("|");
		System.out.println("Fitness: " + getBestSolution().getFitness());
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

	// Mutacao
	public RouteSolution mutationRouteByChange(RouteSolution solution) {
		Pontos temp = new Pontos();
		int pontoAleatorio1 = r.nextInt((solution.getSolucao().size()) - 1);
		int pontoAleatorio2 = r.nextInt((solution.getSolucao().size()) - 1);

		temp = solution.getSolucao().get(pontoAleatorio1);
		solution.getSolucao().set(pontoAleatorio1, solution.getSolucao().get(pontoAleatorio2));
		solution.getSolucao().set(pontoAleatorio2, temp);

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

	public RouteSolution torneio(RouteSolution solucao1, RouteSolution solucao2) {
		if (solucao1.getFitness() == 0) {
			fitness(solucao1);
		}
		if (solucao2.getFitness() == 0) {
			fitness(solucao2);
		}
		if (solucao1.getFitness() > solucao2.getFitness()) {
			return solucao2;
		} else {
			return solucao1;
		}
	}

	private int fitness(RouteSolution solucao) {
		int distanciaTotal = 0;
		for (int i = 0; i < solucao.getSolucao().size(); i++) {
			if (i == solucao.getSolucao().size() - 1) {
				distanciaTotal += solucao.getSolucao().get(i).getDistancias()
						.get(solucao.getSolucao().get(0).getRotulo());
			} else {
				distanciaTotal += solucao.getSolucao().get(i).getDistancias()
						.get(solucao.getSolucao().get(i + 1).getRotulo());
			}
		}
		solucao.setFitness(distanciaTotal);
		return distanciaTotal;
	}

	private RouteSolution getMelhorSolucao(ArrayList<RouteSolution> populacao) {
		RouteSolution melhorSolucao = populacao.get(0);
		// for(int i=0; i< populacao.size(); i++){
		// if(populacao.get(i).getFitness() < melhorSolucao.getFitness()){
		// melhorSolucao.setFitness(fitness);
		// melhorSolucao = populacao.get(i);
		// }
		// }
		for (RouteSolution routeSolution : populacao) {
			if (routeSolution.getFitness() < melhorSolucao.getFitness()) {
				melhorSolucao.setFitness(routeSolution.getFitness());
				melhorSolucao.setSolucao(routeSolution.getSolucao());
				// melhorSolucao = routeSolution;
			}
		}
		return melhorSolucao;
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

	public int getGeracao() {
		return geracao;
	}

	public void setGeracao(int geracao) {
		this.geracao = geracao;
	}

}
