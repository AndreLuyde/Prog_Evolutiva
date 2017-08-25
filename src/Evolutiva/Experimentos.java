package Evolutiva;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Experimentos {
	Random r = new Random();
	private static BufferedReader readArq;
	
	public Experimentos() {

	}

	public void run(ArrayList<Integer> problemas, ArrayList<Integer> populacoes, ArrayList<Long> tempos) {
		for (int i = 0; i < 5 * problemas.size(); i++) {
			int aleatorio = r.nextInt(problemas.size());
			int aleatorio2 = r.nextInt(populacoes.size());
			int aleatorio3 = r.nextInt(populacoes.size());
			funcaoTeste(problemas.get(aleatorio), populacoes.get(aleatorio2), tempos.get(aleatorio3));
		}
	}

	public void funcaoTeste(int prob, int populacao, long tempo) {
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
	
	public RouteProblem leituraArquivo(String file, RouteProblem problema) throws IOException{
		readArq = new BufferedReader(new FileReader(file));
		ArrayList<Pontos> pontos = new ArrayList<Pontos>();
		int indexPontos = 0;
		int indexDistancias = 0;
		
		String linha = readArq.readLine();
		while (linha != null) {
			if (!linha.startsWith("/") && !linha.isEmpty() && !linha.contains("n") && !linha.contains("D") && !linha.contains("[") && !linha.contains("]")) {
				linha = linha.replaceAll(" ", "");
				String[] aux = linha.split(",");
				Pontos p1 = new Pontos();
				p1.setPontoX(Integer.parseInt(aux[0]));
				p1.setPontoX(Integer.parseInt(aux[1]));
				p1.setRotulo(indexPontos);
				pontos.add(p1);
				indexPontos++;
			}else if(linha.contains("[") && linha.contains("]")){
				int indexFim = linha.lastIndexOf("]");
				int indexIni = linha.lastIndexOf("[");
				String aux2 = linha.substring(indexIni+1, indexFim-1);
				aux2 = aux2.replaceAll(" ", "");
				String[] aux3 = aux2.split(",");
				ArrayList<Double> distancias = new ArrayList<Double>();
				for(int i=0;i<aux3.length;i++){
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
