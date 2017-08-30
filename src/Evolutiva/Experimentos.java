package Evolutiva;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Experimentos {
	Random r = new Random();
	private static BufferedReader readArq;

	public Experimentos() {

	}

	public void run(ArrayList<Integer> problemas, ArrayList<Integer> populacoes, ArrayList<Long> tempos, int tipo,
			ArrayList<Double> proporcaoPaisFilhos) {
		if (tipo == 1) {
			for (int i = 0; i < 5 * problemas.size(); i++) {
				int aleatorio = r.nextInt(problemas.size());
				int aleatorio2 = r.nextInt(populacoes.size());
				int aleatorio3 = r.nextInt(tempos.size());
				funcaoTesteGA(problemas.get(aleatorio), populacoes.get(aleatorio2), tempos.get(aleatorio3));
			}
		} else if (tipo == 2) {
			for (int i = 0; i < 5 * problemas.size(); i++) {
				boolean autoAdaptacao;
				boolean competicaoPaisFilhos;
				int aleatorio = r.nextInt(problemas.size());
				int aleatorio2 = r.nextInt(populacoes.size());
				int aleatorio3 = r.nextInt(tempos.size());
				int aleatorio4 = r.nextInt(proporcaoPaisFilhos.size());
				int aleatorio5 = r.nextInt(2);
				int aleatorio6 = r.nextInt(2);
				if (aleatorio5 == 1) {
					autoAdaptacao = true;
				} else {
					autoAdaptacao = false;
				}
				if (aleatorio6 == 1) {
					competicaoPaisFilhos = true;
				} else {
					competicaoPaisFilhos = false;
				}
				funcaoTesteAE(problemas.get(aleatorio), populacoes.get(aleatorio2), tempos.get(aleatorio3),
						autoAdaptacao, competicaoPaisFilhos, proporcaoPaisFilhos.get(aleatorio4));
			}
		}
	}

	public void runIlhas(ArrayList<Integer> problemas, ArrayList<Integer> populacoes, ArrayList<Long> tempos,
			ArrayList<Double> proporcaoPaisFilhos) {
		for (int i = 0; i < 5 * problemas.size(); i++) {
			boolean autoAdaptacao;
			boolean competicaoPaisFilhos;
			int aleatorio = r.nextInt(problemas.size());
			int aleatorio2 = r.nextInt(populacoes.size());
			int aleatorio3 = r.nextInt(tempos.size());
			int aleatorio4 = r.nextInt(proporcaoPaisFilhos.size());
			int aleatorio5 = r.nextInt(2);
			int aleatorio6 = r.nextInt(2);
			if (aleatorio5 == 1) {
				autoAdaptacao = true;
			} else {
				autoAdaptacao = false;
			}
			if (aleatorio6 == 1) {
				competicaoPaisFilhos = true;
			} else {
				competicaoPaisFilhos = false;
			}
			funcaoTesteIlhas(problemas.get(aleatorio), populacoes.get(aleatorio2), tempos.get(aleatorio3),
					autoAdaptacao, competicaoPaisFilhos, proporcaoPaisFilhos.get(aleatorio4));
		}
	}

	public void funcaoTesteGA(int prob, int populacao, long tempo) {
		String arquivo = "";
		if (prob == 30) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP30.txt";
		} else if (prob == 35) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP35.txt";
		} else if (prob == 40) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP40.txt";
		} else if (prob == 50) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP50.txt";
		} else if (prob == 80) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP80.txt";
		} else if (prob == 100) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP100.txt";
		}

		RouteProblem problema = null;
		try {
			problema = leituraArquivo(arquivo, problema);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long tempoInicial = System.currentTimeMillis();
		GA ga = new GA(problema, populacao);
		ga.run(tempo, tempoInicial);
	}

	public void funcaoTesteAE(int prob, int populacao, long tempo, boolean autoAdaptacao, boolean competicaoPaisFilhos,
			double proporcaoPaisFilhos) {
		String arquivo = "";
		if (prob == 30) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP30.txt";
		} else if (prob == 35) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP35.txt";
		} else if (prob == 40) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP40.txt";
		} else if (prob == 50) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP50.txt";
		} else if (prob == 80) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP80.txt";
		} else if (prob == 100) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP100.txt";
		}

		RouteProblem problema = null;
		try {
			problema = leituraArquivo(arquivo, problema);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long tempoInicial = System.currentTimeMillis();
		// execução sem busca local
		 AE ae = new AE(problema, populacao, autoAdaptacao, competicaoPaisFilhos,
		 proporcaoPaisFilhos, false);

		// execução com busca local
		ae = new AE(problema, populacao, autoAdaptacao, competicaoPaisFilhos, proporcaoPaisFilhos, true);
		ae.run(tempo, tempoInicial);
	}

	public void funcaoTesteIlhas(int prob, int populacao, long tempo, boolean autoAdaptacao,
			boolean competicaoPaisFilhos, double proporcaoPaisFilhos) {
		String arquivo = "";
		if (prob == 30) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP30.txt";
		} else if (prob == 35) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP35.txt";
		} else if (prob == 40) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP40.txt";
		} else if (prob == 50) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP50.txt";
		} else if (prob == 80) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP80.txt";
		} else if (prob == 100) {
			arquivo = "/home/andre/git/Prog_Evolutiva/src/Evolutiva/TSP100.txt";
		}

		RouteProblem problema = null;
		try {
			problema = leituraArquivo(arquivo, problema);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long tempoInicial = System.currentTimeMillis();

		// execução com busca local
		AE ae = new AE(problema, populacao, autoAdaptacao, competicaoPaisFilhos, proporcaoPaisFilhos, true);
		ArrayList<ArrayList<Individuo>> ilhas = new ArrayList<ArrayList<Individuo>>();
		ilhas = geraIlhas(problema, populacao);
		ae.ilhas(ilhas, tempo, tempoInicial);
	}

	private ArrayList<ArrayList<Individuo>> geraIlhas(RouteProblem problem, int populationSize) {
		ArrayList<Individuo> popInd = new ArrayList<Individuo>();
		ArrayList<ArrayList<Individuo>> ilhas = new ArrayList<ArrayList<Individuo>>();
		RouteSolution solucaoTemp;
		Individuo individuo;
		for(int j=0; j<5; j++) {
			popInd = new ArrayList<Individuo>();
			for (int i = 0; i < populationSize; i++) {
				ArrayList<Pontos> pontos = new ArrayList<Pontos>(problem.getPontos());
				Collections.shuffle(pontos);
				solucaoTemp = new RouteSolution(pontos);
				individuo = new Individuo(solucaoTemp, 0.5);
				popInd.add(individuo);
			}
			ilhas.add(popInd);
		}
		return ilhas;
	}

	public RouteProblem leituraArquivo(String file, RouteProblem problema) throws IOException {
		readArq = new BufferedReader(new FileReader(file));
		ArrayList<Pontos> pontos = new ArrayList<Pontos>();
		int indexPontos = 0;
		int indexDistancias = 0;

		String linha = readArq.readLine();
		while (linha != null) {
			if (!linha.startsWith("/") && !linha.isEmpty() && !linha.contains("n") && !linha.contains("D")
					&& !linha.contains("[") && !linha.contains("]")) {
				linha = linha.replaceAll(" ", "");
				String[] aux = linha.split(",");
				Pontos p1 = new Pontos();
				p1.setPontoX(Integer.parseInt(aux[0]));
				p1.setPontoX(Integer.parseInt(aux[1]));
				p1.setRotulo(indexPontos);
				pontos.add(p1);
				indexPontos++;
			} else if (linha.contains("[") && linha.contains("]")) {
				int indexFim = linha.lastIndexOf("]");
				int indexIni = linha.lastIndexOf("[");
				String aux2 = linha.substring(indexIni + 1, indexFim - 1);
				aux2 = aux2.replaceAll(" ", "");
				String[] aux3 = aux2.split(",");
				ArrayList<Double> distancias = new ArrayList<Double>();
				for (int i = 0; i < aux3.length; i++) {
					distancias.add(Double.parseDouble(aux3[i]));
				}
				pontos.get(indexDistancias).setDistancias(distancias);
				indexDistancias++;
			}
			linha = readArq.readLine();
		}
		problema = new RouteProblem(pontos.size(), pontos);
		return problema;
	}
}
