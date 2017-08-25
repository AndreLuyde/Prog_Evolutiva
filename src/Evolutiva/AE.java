package Evolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AE {

	private RouteProblem problema;
	private ArrayList<RouteSolution> populacao = new ArrayList<RouteSolution>();
	private ArrayList<Individuo> popIndividuos = new ArrayList<Individuo>();
	private RouteSolution bestSolution;
	private double crossProbability;
	private double mutationProbability;
	private int geracaoAtual;
	private int tamanhoPopulacao;
	// este bool definirá se a seleção para reprodução envolverá os pais
	private Boolean competicaoPaisFilhos;
	// este double definirá a proporção entre pais e filhos
	private Double proporcaoPaisFilhos;
	private Boolean autoAdaptacao;
	Random r = new Random();

	public AE(RouteProblem problem, int populationSize, Boolean autoAdaptacao, Boolean competicaoPaisFilhos,
			double proporcaoPaisFilhos) {
		setMutationProbability(1 / problem.getNumClients());
		setCrossProbability(0.9);
		setProblema(problem);
		setTamanhoPopulacao(populationSize);
		setAutoAdaptacao(autoAdaptacao);
		setCompeticaoPaisFilhos(competicaoPaisFilhos);
		setProporcaoPaisFilhos(proporcaoPaisFilhos);

		// inicializa a populacao
		for (int i = 0; i < populationSize; i++) {
			ArrayList<Pontos> pontos = new ArrayList<Pontos>(problem.getPontos());
			Collections.shuffle(pontos);
			RouteSolution solucaoTemp = new RouteSolution(pontos);
			getPopulacao().add(solucaoTemp);
		}
	}

	public void ilhas(ArrayList<ArrayList<RouteSolution>> ilhas, long timeFimExecucao, long tempoInicial) {
		ArrayList<RouteSolution> melhoresSolutions = new ArrayList<RouteSolution>(); 
		RouteSolution best;
		int tempo = 0;
		double timeDouble = 0;
		do {
			for (int i = 0; i < ilhas.size(); i++) {
				cicloEvolucionario(ilhas.get(i), timeFimExecucao, tempoInicial, false);
				best = getMelhorSolucao(ilhas.get(i));
				if(melhoresSolutions.get(i).equals(null)) {
					melhoresSolutions.set(i, best);
				}else {
					if(fitness(melhoresSolutions.get(i)) > fitness(best)) {
						melhoresSolutions.set(i, best);
					}
				}
			}
			tempo++;
			
			if(tempo == 5) {
				for (int i = 0; i < ilhas.size(); i++) {
					ilhas.get(i).add(melhoresSolutions.get(i));
				}
				tempo = 0;
			}
			
			long actualTime = System.currentTimeMillis();
			long time = (actualTime - tempoInicial);
			timeDouble = Double.parseDouble((String.valueOf(time))) / 1000;
		} while (timeFimExecucao > timeDouble);
	}

	public void run(long timeFimExecucao, long tempoInicial) {
		cicloEvolucionario(getPopulacao(), timeFimExecucao, tempoInicial, getAutoAdaptacao());
	}

	public RouteSolution getMelhorSolucao(ArrayList<RouteSolution> populacao) {
		RouteSolution melhorSolucao = null;
		for (RouteSolution routeSolution : populacao) {
			fitness(routeSolution);
			if (melhorSolucao != null) {
				if (routeSolution.getFitness() < melhorSolucao.getFitness()) {
					melhorSolucao = routeSolution;
				}
			} else {
				melhorSolucao = routeSolution;
			}
		}
		setBestSolution(melhorSolucao);
		return melhorSolucao;
	}

	private void cicloEvolucionario(ArrayList<RouteSolution> populacao, long timeFimExecucao, long tempoInicial,
			boolean autoAdaptacao) {
		ArrayList<ArrayList<Pontos>> filhos = new ArrayList<ArrayList<Pontos>>();
		ArrayList<RouteSolution> populacaoFilhos = new ArrayList<RouteSolution>();
		ArrayList<Individuo> popIndividuos = new ArrayList<Individuo>();
		ArrayList<Individuo> filhosIndividuos = new ArrayList<Individuo>();
		Double timeDouble = 0d;
		RouteSolution pai1 = null;
		RouteSolution pai2 = null;
		Individuo paiInd1 = null;
		Individuo paiInd2 = null;
		RouteSolution filho = null;
		int indice1 = -1;
		int indice2 = -1;
		int indice3 = -1;
		int indice4 = -1;

		// coloca em cada solução o fitness dela
		for (RouteSolution routeSolution : populacao) {
			fitness(routeSolution);
		}

		if (autoAdaptacao) {
			Individuo individuo;
			for (RouteSolution routeSolution : populacao) {
				individuo = new Individuo(routeSolution, 0.8);
				popIndividuos.add(individuo);
			}

			do {
				do {
					// seleção de pontos para fazer seleção para cruzamento
					indice1 = (int) (0 + Math.random() * popIndividuos.size());
					indice2 = (int) (0 + Math.random() * popIndividuos.size());
					indice3 = (int) (0 + Math.random() * popIndividuos.size());
					indice4 = (int) (0 + Math.random() * popIndividuos.size());

					// seleção de pais para cruzamento
					pai1 = torneio(popIndividuos.get(indice1).getSolution(), popIndividuos.get(indice2).getSolution());
					paiInd1 = new Individuo(pai1, 0.8);
					pai2 = torneio(popIndividuos.get(indice3).getSolution(), popIndividuos.get(indice4).getSolution());
					paiInd2 = new Individuo(pai2, 0.8);

					// verifica probabilidade de cruzamento
					if (((paiInd1.getSigma() + paiInd2.getSigma()) / 2) < getCrossProbability()) {
						filhosIndividuos.addAll(paiInd1.crossing(paiInd1, paiInd2));
					}

					// verifica possibilidade de mutação
					if (((paiInd1.getSigma() + paiInd2.getSigma()) / 2) < getMutationProbability()) {
						paiInd1.mutacao(paiInd1);
						filhosIndividuos.add(paiInd1);
					}

					// faz busca local em N indivíduos
					buscaLocal(paiInd1);

				} while (filhosIndividuos.size() < getTamanhoPopulacao() / 2);

				// selecao para proxima geração //estratégia de seleção
				if (getProporcaoPaisFilhos() > 0.5 || getCompeticaoPaisFilhos()) {
					popIndividuos = selecaoIndividuos(popIndividuos, filhosIndividuos, getTamanhoPopulacao(),
							getProporcaoPaisFilhos(), getCompeticaoPaisFilhos());
				} else {
					popIndividuos = roletaIndividuos(popIndividuos, filhosIndividuos, getTamanhoPopulacao(),
							getProporcaoPaisFilhos(), getCompeticaoPaisFilhos());
				}
				setPopIndividuos(popIndividuos);
				setGeracaoAtual(getGeracaoAtual() + 1);

				// guarda melhor solução da geração
				RouteSolution melhorSolucaoGeracao = new RouteSolution(getMelhorSolucaoIndividuos(popIndividuos));
				if (getBestSolution() == null) {
					setBestSolution(melhorSolucaoGeracao);
					// mostra melhor solução da geração
					System.out.println("Solução Da " + getGeracaoAtual() + " geração");
					for (Pontos ponto : melhorSolucaoGeracao.getSolucao()) {
						System.out.print("| " + ponto.getRotulo() + " ");
					}
					System.out.println("|");
					System.out.println("Fitness: " + melhorSolucaoGeracao.getFitness());

				} else if (melhorSolucaoGeracao.getFitness() < getBestSolution().getFitness()) {
					setBestSolution(melhorSolucaoGeracao);
					// mostra melhor solucao da geração
					System.out.println("Solução Da " + getGeracaoAtual() + " geração");
					for (Pontos ponto : melhorSolucaoGeracao.getSolucao()) {
						System.out.print("| " + ponto.getRotulo() + " ");
					}
					System.out.println("|");
					System.out.println("Fitness: " + melhorSolucaoGeracao.getFitness());
				}

				// atualiza criterio de parada
				long actualTime = System.currentTimeMillis();
				long time = (actualTime - tempoInicial);
				timeDouble = Double.parseDouble((String.valueOf(time))) / 1000;

			} while (timeFimExecucao > timeDouble);

			System.out.println("Melhor Solução encontrada");
			for (Pontos ponto : getBestSolution().getSolucao()) {
				System.out.print("| " + ponto.getRotulo() + " ");
			}
			System.out.println("|");
			System.out.println("Fitness: " + getBestSolution().getFitness());

		} else {
			do {
				do {
					// seleção de pontos para fazer seleção para cruzamento
					indice1 = (int) (0 + Math.random() * populacao.size());
					indice2 = (int) (0 + Math.random() * populacao.size());
					indice3 = (int) (0 + Math.random() * populacao.size());
					indice4 = (int) (0 + Math.random() * populacao.size());

					// seleção de pais para cruzamento
					pai1 = torneio(populacao.get(indice1), populacao.get(indice2));
					pai2 = torneio(populacao.get(indice3), populacao.get(indice4));

					// verifica probabilidade de cruzamento
					if (r.nextGaussian() < getCrossProbability()) {
						filhos = crossingRoute2Cut(pai1, pai2);
						for (ArrayList<Pontos> arrayList : filhos) {
							filho = new RouteSolution(arrayList);
							populacaoFilhos.add(filho);
						}
					}

					// verifica possibilidade de mutação
					if (r.nextGaussian() < getMutationProbability()) {
						populacaoFilhos.add(mutationRouteByChange(pai1));
					}

					// faz busca local em N indivíduos
					Individuo teste = new Individuo(pai1, 0.5);
					buscaLocal(teste);
				} while (populacaoFilhos.size() < getTamanhoPopulacao() / 2);

				// selecao para proxima geração //estratégia de seleção
				if (getProporcaoPaisFilhos() > 0.5 || getCompeticaoPaisFilhos()) {
					populacao = selecao(populacao, populacaoFilhos, getTamanhoPopulacao(), getProporcaoPaisFilhos(),
							getCompeticaoPaisFilhos());
				} else {
					populacao = roleta(populacao, populacaoFilhos, getTamanhoPopulacao(), getProporcaoPaisFilhos(),
							getCompeticaoPaisFilhos());
				}
				setPopulacao(populacao);
				setGeracaoAtual(getGeracaoAtual() + 1);

				// guarda melhor solução da geração
				RouteSolution melhorSolucaoGeracao = new RouteSolution(getMelhorSolucao(populacao));
				if (getBestSolution() == null) {
					setBestSolution(melhorSolucaoGeracao);
					// mostra melhor solução da geração
					System.out.println("Solução Da " + getGeracaoAtual() + " geração");
					for (Pontos ponto : melhorSolucaoGeracao.getSolucao()) {
						System.out.print("| " + ponto.getRotulo() + " ");
					}
					System.out.println("|");
					System.out.println("Fitness: " + melhorSolucaoGeracao.getFitness());

				} else if (melhorSolucaoGeracao.getFitness() < getBestSolution().getFitness()) {
					setBestSolution(melhorSolucaoGeracao);
					// mostra melhor solucao da geração
					System.out.println("Solução Da " + getGeracaoAtual() + " geração");
					for (Pontos ponto : melhorSolucaoGeracao.getSolucao()) {
						System.out.print("| " + ponto.getRotulo() + " ");
					}
					System.out.println("|");
					System.out.println("Fitness: " + melhorSolucaoGeracao.getFitness());
				}

				// atualiza criterio de parada
				long actualTime = System.currentTimeMillis();
				long time = (actualTime - tempoInicial);
				timeDouble = Double.parseDouble((String.valueOf(time))) / 1000;

			} while (timeFimExecucao > timeDouble);

			System.out.println("Melhor Solução encontrada");
			for (Pontos ponto : getBestSolution().getSolucao()) {
				System.out.print("| " + ponto.getRotulo() + " ");
			}
			System.out.println("|");
			System.out.println("Fitness: " + getBestSolution().getFitness());
		}

	}

	private RouteSolution getMelhorSolucaoIndividuos(ArrayList<Individuo> popIndividuos) {
		RouteSolution melhorSolucao = null;
		for (Individuo individuo : popIndividuos) {
			fitness(individuo.getSolution());
			if (melhorSolucao != null) {
				if (individuo.getSolution().getFitness() < melhorSolucao.getFitness()) {
					melhorSolucao = individuo.getSolution();
				}
			} else {
				melhorSolucao = individuo.getSolution();
			}
		}
		setBestSolution(melhorSolucao);
		return melhorSolucao;
	}

	private ArrayList<Individuo> selecaoIndividuos(ArrayList<Individuo> popIndividuos,
			ArrayList<Individuo> popIndividuosFilhos, int tamanhoPopulacao, Double proporcaoPaisFilhos,
			Boolean competicaoPaisFilhos) {

		Individuo aleatorio1 = null;
		Individuo aleatorio2 = null;
		ArrayList<Individuo> populacaoFinal = new ArrayList<Individuo>();

		if (competicaoPaisFilhos) {
			for (Individuo individuoAdd : popIndividuosFilhos) {
				popIndividuos.add(individuoAdd);
			}

			for (int i = 0; i < tamanhoPopulacao * proporcaoPaisFilhos; i++) {
				int n = (int) (0 + Math.random() * popIndividuos.size());
				aleatorio1 = popIndividuos.get(n);

				n = (int) (0 + Math.random() * popIndividuos.size());
				aleatorio2 = popIndividuos.get(n);

				if (fitness(aleatorio1.getSolution()) < fitness(aleatorio2.getSolution())) {
					populacaoFinal.add(aleatorio1);
				} else {
					populacaoFinal.add(aleatorio2);
				}
			}
		} else {
			// pais
			for (int i = 0; i < tamanhoPopulacao * proporcaoPaisFilhos; i++) {
				int n = (int) (0 + Math.random() * popIndividuos.size());
				aleatorio1 = popIndividuos.get(n);

				n = (int) (0 + Math.random() * popIndividuos.size());
				aleatorio2 = popIndividuos.get(n);

				if (fitness(aleatorio1.getSolution()) < fitness(aleatorio2.getSolution())) {
					populacaoFinal.add(aleatorio1);
				} else {
					populacaoFinal.add(aleatorio2);
				}
			}

			// filhos
			while (populacaoFinal.size() <= tamanhoPopulacao) {
				int n = (int) (0 + Math.random() * popIndividuosFilhos.size());
				aleatorio1 = popIndividuosFilhos.get(n);

				n = (int) (0 + Math.random() * popIndividuosFilhos.size());
				aleatorio2 = popIndividuosFilhos.get(n);

				if (fitness(aleatorio1.getSolution()) < fitness(aleatorio2.getSolution())) {
					populacaoFinal.add(aleatorio1);
				} else {
					populacaoFinal.add(aleatorio2);
				}
			}
		}

		return populacaoFinal;

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
		solution.getSolucao().set(pontoAleatorio, solution.getSolucao().get(solution.getSolucao().size() - 1));
		solution.getSolucao().set(solution.getSolucao().size() - 1, temp);

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

	// seleção
	public ArrayList<RouteSolution> selecao(ArrayList<RouteSolution> populacao, ArrayList<RouteSolution> novasSulucoes,
			int tamanhoPopulacao, double proporcaoPaiFilhos, boolean competicaoPaisFilhos) {
		RouteSolution aleatorio1 = null;
		RouteSolution aleatorio2 = null;
		ArrayList<RouteSolution> populacaoFinal = new ArrayList<RouteSolution>();

		if (competicaoPaisFilhos) {
			for (RouteSolution routeSolution : novasSulucoes) {
				populacao.add(routeSolution);
			}

			for (int i = 0; i < tamanhoPopulacao * proporcaoPaiFilhos; i++) {
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
		} else {
			// pais
			for (int i = 0; i < tamanhoPopulacao * proporcaoPaiFilhos; i++) {
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

			// filhos
			while (populacaoFinal.size() <= tamanhoPopulacao) {
				int n = (int) (0 + Math.random() * novasSulucoes.size());
				aleatorio1 = novasSulucoes.get(n);

				n = (int) (0 + Math.random() * novasSulucoes.size());
				aleatorio2 = novasSulucoes.get(n);

				if (fitness(aleatorio1) < fitness(aleatorio2)) {
					populacaoFinal.add(aleatorio1);
				} else {
					populacaoFinal.add(aleatorio2);
				}
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

	// avaliação da solução
	private int fitness(RouteSolution solucao) {
		int distanciaTotal = 0;
		for (int i = 0; i < solucao.getSolucao().size(); i++) {
			if (i == solucao.getSolucao().size() - 1) {
				distanciaTotal += solucao.getSolucao().get(i).getDistancias().get(solucao.getSolucao().get(0).getRotulo() - 1);
			} else {
				distanciaTotal += solucao.getSolucao().get(i).getDistancias().get(solucao.getSolucao().get(i + 1).getRotulo() - 1);
			}
		}
		solucao.setFitness(distanciaTotal);
		return distanciaTotal;
	}

	// busca local para cada individuo
	private void buscaLocal(Individuo individuo) {
		RouteSolution melhorSolucao = new RouteSolution(individuo.getSolution());

		int iteracoes = 0;
		while (iteracoes < 100) {
			RouteSolution novaSolucao = new RouteSolution(geraNovoVizinho(individuo.getSolution()));
			fitness(novaSolucao);
			if (melhorSolucao.getFitness() > novaSolucao.getFitness()) {
				melhorSolucao = new RouteSolution(novaSolucao);
			}
			iteracoes++;
		}
		individuo.setSolution(melhorSolucao);
	}

	private RouteSolution geraNovoVizinho(RouteSolution solution) {
		int pontoAleatorio1 = r.nextInt((solution.getSolucao().size()) - 1);
		int pontoAleatorio2 = r.nextInt((solution.getSolucao().size()) - 1);
		ArrayList<Pontos> pontosModificados = new ArrayList<Pontos>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		if (pontoAleatorio2 < pontoAleatorio1) {
			int aux = pontoAleatorio2;
			pontoAleatorio2 = pontoAleatorio1;
			pontoAleatorio1 = aux;
		} else if (pontoAleatorio2 == pontoAleatorio1) {
			return solution;
		}
		int menorDistancia = -1;
		pontosModificados.add(solution.getSolucao().get(pontoAleatorio1));
		indices.add(pontoAleatorio1);
		for (int i = pontoAleatorio1 + 1; i < pontoAleatorio2; i++) {
			menorDistancia = getVizinhoMaisProximo(solution.getSolucao().get(i).getDistancias(), indices,
					pontoAleatorio1, pontoAleatorio2);
			if (menorDistancia != -1) {
				pontosModificados.add(solution.getSolucao().get(menorDistancia));
				indices.add(menorDistancia);
				menorDistancia = -1;
			}
		}
		int j = 0;
		if (pontoAleatorio2 - pontoAleatorio1 == pontosModificados.size()) {
			for (int i = pontoAleatorio1; i < pontoAleatorio2; i++) {
				solution.getSolucao().set(i, pontosModificados.get(j));
				j++;
			}
			return solution;
		} else {
			return solution;
		}
	}

	private int getVizinhoMaisProximo(ArrayList<Double> distancias, ArrayList<Integer> indices, int rand1, int rand2) {
		ArrayList<Double> dist = new ArrayList<Double>(distancias);
		for (int i = 0; i < rand1; i++) {
			dist.set(i, -1d);
		}
		for (int i = rand2; i < dist.size(); i++) {
			dist.set(i, -1d);
		}

		double menor = -1;
		int indiceMenor = -1;
		for (int j = 0; j < indices.size(); j++) {
			dist.set(indices.get(j), -1d);
		}
		for (int i = 0; i < dist.size(); i++) {
			if (menor < 0 && dist.get(i) > 0) {
				menor = dist.get(i);
				indiceMenor = i;
			} else if (dist.get(i) < menor && dist.get(i) > 0) {
				menor = dist.get(i);
				indiceMenor = i;
			}
		}
		return indiceMenor;
	}

	public ArrayList<RouteSolution> roleta(ArrayList<RouteSolution> populacao, ArrayList<RouteSolution> novasSulucoes,
			int tamanhoPopulacao, double proporcaoPaisFilhos, boolean competicaoPaisFilhos) {

		ArrayList<RouteSolution> populacaoFinal = new ArrayList<RouteSolution>();
		ArrayList<RouteSolution> populacaoTotal = new ArrayList<RouteSolution>(populacao);
		int somaFitness = 0;
		int somaFitnessPais = 0;
		int somaFitnessFilhos = 0;
		int somaFitness2 = 0;
		int aleatorio = -1;

		// calcula o fitness dos filhos
		for (int i = 0; i < novasSulucoes.size(); i++) {
			fitness(novasSulucoes.get(i));
		}

		for (int i = 0; i < populacao.size(); i++) {
			somaFitnessPais += populacao.get(i).getFitness();
		}
		for (int i = 0; i < novasSulucoes.size(); i++) {
			somaFitnessFilhos += novasSulucoes.get(i).getFitness();
		}
		somaFitness = somaFitnessPais + somaFitnessFilhos;
		if (competicaoPaisFilhos) {
			for (int i = 0; i < novasSulucoes.size(); i++) {
				populacaoTotal.add(novasSulucoes.get(i));
			}

			for (int i = 0; i < tamanhoPopulacao; i++) {
				aleatorio = r.nextInt(somaFitness + 1);
				for (int j = 0; j < populacaoTotal.size(); j++) {
					somaFitness2 += (int) populacaoTotal.get(j).getFitness();
					if (somaFitness2 >= aleatorio) {
						populacaoFinal.add(populacaoTotal.get(j));
						somaFitness2 = 0;
						break;
					}
				}
			}
		} else {
			// pais
			for (int i = 0; i < tamanhoPopulacao * proporcaoPaisFilhos; i++) {
				aleatorio = r.nextInt(somaFitnessPais + 1);
				for (int j = 0; j < populacaoFinal.size(); j++) {
					somaFitness2 += (int) populacao.get(j).getFitness();
					if (somaFitness2 >= aleatorio) {
						populacaoFinal.add(populacao.get(j));
						somaFitness2 = 0;
						break;
					}
				}
			}
			// filhos
			while (populacaoFinal.size() <= tamanhoPopulacao) {
				aleatorio = r.nextInt(somaFitnessFilhos + 1);
				for (int j = 0; j < populacaoFinal.size(); j++) {
					somaFitness2 += (int) novasSulucoes.get(j).getFitness();
					if (somaFitness2 >= aleatorio) {
						populacaoFinal.add(novasSulucoes.get(j));
						somaFitness2 = 0;
						break;
					}
				}
			}
		}
		return populacaoFinal;
	}

	private ArrayList<Individuo> roletaIndividuos(ArrayList<Individuo> popIndividuos,
			ArrayList<Individuo> popIndividuosFilhos, int tamanhoPopulacao, Double proporcaoPaisFilhos,
			Boolean competicaoPaisFilhos) {
		ArrayList<Individuo> populacaoFinal = new ArrayList<Individuo>();
		ArrayList<Individuo> populacaoTotal = new ArrayList<Individuo>(popIndividuos);
		int somaFitness = 0;
		int somaFitnessPais = 0;
		int somaFitnessFilhos = 0;
		int somaFitness2 = 0;
		int aleatorio = -1;
		// calcula o fitness dos filhos
		for (int i = 0; i < popIndividuosFilhos.size(); i++) {
			fitness(popIndividuosFilhos.get(i).getSolution());
		}

		for (int i = 0; i < popIndividuos.size(); i++) {
			somaFitnessPais += popIndividuos.get(i).getSolution().getFitness();
		}
		for (int i = 0; i < popIndividuosFilhos.size(); i++) {
			somaFitnessFilhos += popIndividuosFilhos.get(i).getSolution().getFitness();
		}
		somaFitness = somaFitnessPais + somaFitnessFilhos;
		if (competicaoPaisFilhos) {
			for (int i = 0; i < popIndividuosFilhos.size(); i++) {
				populacaoTotal.add(popIndividuosFilhos.get(i));
			}

			for (int i = 0; i < tamanhoPopulacao; i++) {
				aleatorio = r.nextInt(somaFitness + 1);
				for (int j = 0; j < populacaoTotal.size(); j++) {
					somaFitness2 += (int) populacaoTotal.get(j).getSolution().getFitness();
					if (somaFitness2 >= aleatorio) {
						populacaoFinal.add(populacaoTotal.get(j));
						somaFitness2 = 0;
						break;
					}
				}
			}
		} else {
			// pais
			for (int i = 0; i < tamanhoPopulacao * proporcaoPaisFilhos; i++) {
				aleatorio = r.nextInt(somaFitnessPais + 1);
				for (int j = 0; j < populacaoFinal.size(); j++) {
					somaFitness2 += (int) popIndividuos.get(j).getSolution().getFitness();
					if (somaFitness2 >= aleatorio) {
						populacaoFinal.add(popIndividuos.get(j));
						somaFitness2 = 0;
						break;
					}
				}
			}
			// filhos
			while (populacaoFinal.size() <= tamanhoPopulacao) {
				aleatorio = r.nextInt(somaFitnessFilhos + 1);
				for (int j = 0; j < populacaoFinal.size(); j++) {
					somaFitness2 += (int) popIndividuosFilhos.get(j).getSolution().getFitness();
					if (somaFitness2 >= aleatorio) {
						populacaoFinal.add(popIndividuosFilhos.get(j));
						somaFitness2 = 0;
						break;
					}
				}
			}
		}
		return populacaoFinal;
	}

	// -----Get and Set
	public RouteProblem getProblema() {
		return problema;
	}

	public void setProblema(RouteProblem problema) {
		this.problema = problema;
	}

	public ArrayList<RouteSolution> getPopulacao() {
		return populacao;
	}

	public void setPopulacao(ArrayList<RouteSolution> populacao) {
		this.populacao = populacao;
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

	public int getGeracaoAtual() {
		return geracaoAtual;
	}

	public void setGeracaoAtual(int geracaoAtual) {
		this.geracaoAtual = geracaoAtual;
	}

	public Boolean getCompeticaoPaisFilhos() {
		return competicaoPaisFilhos;
	}

	public void setCompeticaoPaisFilhos(Boolean competicaoPaisFilhos) {
		this.competicaoPaisFilhos = competicaoPaisFilhos;
	}

	public Double getProporcaoPaisFilhos() {
		return proporcaoPaisFilhos;
	}

	public void setProporcaoPaisFilhos(Double proporcaoPaisFilhos) {
		this.proporcaoPaisFilhos = proporcaoPaisFilhos;
	}

	public int getTamanhoPopulacao() {
		return tamanhoPopulacao;
	}

	public void setTamanhoPopulacao(int tamanhoPopulacao) {
		this.tamanhoPopulacao = tamanhoPopulacao;
	}

	public Boolean getAutoAdaptacao() {
		return autoAdaptacao;
	}

	public void setAutoAdaptacao(Boolean autoAdaptacao) {
		this.autoAdaptacao = autoAdaptacao;
	}

	public ArrayList<Individuo> getPopIndividuos() {
		return popIndividuos;
	}

	public void setPopIndividuos(ArrayList<Individuo> popIndividuos) {
		this.popIndividuos = popIndividuos;
	}
}
