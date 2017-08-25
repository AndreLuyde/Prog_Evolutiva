package Evolutiva;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Principal {
	private static BufferedReader readArq;

	public static void main(String[] args) throws IOException {
		Random r = new Random();
		
//		---------------------Experimentos GA------------------------
		Experimentos exp = new Experimentos();
		ArrayList<Integer> prob = new ArrayList<Integer>();
		prob.add(30);
		prob.add(35);
		prob.add(40);
		prob.add(50);
		prob.add(80);
		prob.add(100);
		ArrayList<Integer>  populacoes = new ArrayList<Integer>();
		populacoes.add(20);
		populacoes.add(40);
		populacoes.add(50);
		populacoes.add(70);
		populacoes.add(80);
		populacoes.add(100);
		ArrayList<Long> tempos = new ArrayList<Long>();
		tempos.add(30l);
		tempos.add(60l);
		tempos.add(90l);
		tempos.add(120l);
		tempos.add(150l);
		tempos.add(180l);
		exp.run(prob, populacoes, tempos);
		
//		-----------------------------------------------------------------------
		ArrayList<Pontos> pontos = new ArrayList<Pontos>();

		// leitura arquivo com rotas
		int indexPontos = 0;
		long timeBegin = 0;
		String seconds = "";

		// ------------------------Leitura do arquivo
		try {
			JFileChooser fileopen = new JFileChooser();
			int ret = fileopen.showDialog(null, "Abrir arquivo");
			seconds = JOptionPane.showInputDialog(null, "Quantidade em Segundos", JOptionPane.INFORMATION_MESSAGE);

			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fileopen.getSelectedFile();

				readArq = new BufferedReader(new FileReader(file));

				String linha = readArq.readLine();
				while (linha != null) {
					if (!linha.startsWith("!") && !linha.contains("X") && !linha.isEmpty()) {
						linha = linha.replaceAll("   ", " ");
						linha = linha.replaceAll("  ", " ");
						String[] aux = linha.split(" ");
						Pontos p1 = new Pontos();
						p1.setRotulo(Integer.parseInt(aux[0]));
						p1.setPontoX(Integer.parseInt(aux[1]));
						p1.setPontoY(Integer.parseInt(aux[2]));
						p1.setIniTW(Integer.parseInt(aux[4]));
						p1.setEndTW(Integer.parseInt(aux[5]));
						pontos.add(p1);
						indexPontos++;
					}
					linha = readArq.readLine();
				}

			}
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}
		// remover ultimo que é a garagem
		if (pontos.get(pontos.size() - 1).getRotulo() == 999) {
			pontos.remove(pontos.size() - 1);
		}

		// calcula as distâncias
		calcularDistancias(pontos);

		RouteProblem problema = new RouteProblem(pontos.size(), pontos);

		// execução GA
		long tempoInicial = System.currentTimeMillis();
		long tempoFinal = Long.parseLong(seconds);
//		GA ga = new GA(problema, 100);
//		ga.run(tempoFinal, tempoInicial);

		//execução AE
		AE ae = new AE(problema, 50, true, true, 0.2);
		ae.run(tempoFinal, tempoInicial);
		
		long timeEnd = System.currentTimeMillis();
		long time = (timeEnd - timeBegin);
		System.out.println("Finish");
		System.out.println("Tempo: " + Double.parseDouble((String.valueOf(time))) / 1000 + " segundos");
	}
	
	//calcula distancia em todos os pontos
	static void calcularDistancias(ArrayList<Pontos> pontos) {
		for (int i = 0; i < pontos.size(); i++) {
			for (int j = 0; j < pontos.size(); j++) {
				if (i != j) {
					pontos.get(i).getDistancias().add(distanciaEntrePontos(pontos.get(i), pontos.get(j)));
				} else {
					pontos.get(i).getDistancias().add(0d);
				}
			}
		}
	}

	// Calcula distancia entre dois pontos
	static double distanciaEntrePontos(Pontos p1, Pontos p2) {
		double distance = Math.sqrt(Math.pow((p2.getPontoX() - p2.getPontoX()), 2) + Math.pow((p2.getPontoY() - p1.getPontoY()), 2));
		return distance;
	}

}
