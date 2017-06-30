package Evolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AE {

	private RouteProblem problema;
	private RouteSolution[] populacao;
	private RouteSolution melhorSolucao;
	private Double probabilidadeCruzamento;
	private Double probabilidadeMutacao;
	private int geracaoAtual;
	private int tamanhoPopulacao;
	// este bool definirá se a seleção para reprodução envolverá os pais
	private Boolean competicaoPaisFilhos;
	// este double definirá a proporção entre pais e filhos
	private Boolean proporcaoPaisFilhos;
	Random r = new Random();

	public AE(RouteProblem problema, int tamanhoPopulacao) {
		setProblema(problema);
		setTamanhoPopulacao(tamanhoPopulacao);
	}

	public void run() {
	}

	public RouteSolution getMelhorSolucao(RouteSolution[] populacao) {
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
		setMelhorSolucao(melhorSolucao);
		return melhorSolucao;
	}

	private void cicloEvolucionario() {

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
			int tamanhoPopulacao) {
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

	// avaliação da solução
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

	// -----Get and Set
	public RouteProblem getProblema() {
		return problema;
	}

	public void setProblema(RouteProblem problema) {
		this.problema = problema;
	}

	public RouteSolution getMelhorSolucao() {
		return melhorSolucao;
	}

	public RouteSolution[] getPopulacao() {
		return populacao;
	}

	public void setPopulacao(RouteSolution[] populacao) {
		this.populacao = populacao;
	}

	public void setMelhorSolucao(RouteSolution melhorSolucao) {
		this.melhorSolucao = melhorSolucao;
	}

	public Double getProbabilidadeCruzamento() {
		return probabilidadeCruzamento;
	}

	// seta probabilidade de cruzamento
	public void setProbabilidadeCruzamento(Double probabilidadeCruzamento) {
		this.probabilidadeCruzamento = probabilidadeCruzamento;
	}

	public Double getProbabilidadeMutacao() {
		return probabilidadeMutacao;
	}

	// seta a probabilidade de mutação
	public void setProbabilidadeMutacao(Double probabilidadeMutacao) {
		this.probabilidadeMutacao = probabilidadeMutacao;
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

	public Boolean getProporcaoPaisFilhos() {
		return proporcaoPaisFilhos;
	}

	public void setProporcaoPaisFilhos(Boolean proporcaoPaisFilhos) {
		this.proporcaoPaisFilhos = proporcaoPaisFilhos;
	}

	public int getTamanhoPopulacao() {
		return tamanhoPopulacao;
	}

	public void setTamanhoPopulacao(int tamanhoPopulacao) {
		this.tamanhoPopulacao = tamanhoPopulacao;
	}
}
