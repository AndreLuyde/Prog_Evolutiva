package Evolutiva;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Principal {
	private static BufferedReader readArq;

	public static void main(String[] args) throws IOException {
		// Teste GA
//		Pontos p1 = new Pontos();
//		p1.setDistancias(distancias);	
		ArrayList<Pontos> pontos = new ArrayList<Pontos>();
//		RouteProblem problema = new RouteProblem(100, pontos);
//		RouteSolution solucao = new RouteSolution(problema, pontos, 0);

		// leitura arquivo com rotas
		int tamanho = 0;
		int indexPontos = 0;
		int indexDistancias = 0;
		Double edge = 0.0;
		long timeBegin = 0;
		String seconds = "";
		int k = 0;

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
//					if(linha.startsWith("n")){
//						linha = linha.replaceAll(" ", "");
//						String[] aux = linha.split("=");
//						pontos = new Pontos[Integer.parseInt(aux[1])];
//					}
					if (!linha.startsWith("/") && !linha.isEmpty() && !linha.contains("n") && !linha.contains("D") && !linha.contains("[") && !linha.contains("]")) {
						linha = linha.replaceAll(" ", "");
						String[] aux = linha.split(",");
						Pontos p1 = new Pontos();
						p1.setPontoX(Integer.parseInt(aux[0]));
						p1.setPontoX(Integer.parseInt(aux[1]));
						p1.setRotulo(indexPontos);
//						pontos[k] = p1;
						pontos.add(p1);
//						k++;
						indexPontos++;
//						p1 = new Pontos();
					}else if(linha.startsWith("[")){
						int indexFim = linha.lastIndexOf("]");
						String aux2 = linha.substring(1, indexFim-1);
						aux2 = aux2.replaceAll(" ", "");
						String[] aux3 = aux2.split(",");
						ArrayList<Integer> distancias = new ArrayList<Integer>();
						for(int i=0;i<aux3.length;i++){
							distancias.add(Integer.parseInt(aux3[i]));
						}
						pontos.get(indexDistancias).setDistancias(distancias);
//						pontos[indexDistancias].setDistancias(distancias);
						indexDistancias++;
					}
					linha = readArq.readLine();
				}

			}
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}
		
		//execução GA
		long tempoInicial = System.currentTimeMillis();
		RouteProblem problema = new RouteProblem(pontos.size(), pontos);
		GA ga = new GA(problema, 100);
		long tempoFinal = Long.parseLong(seconds);
		ga.run(tempoFinal, tempoInicial);
		
		long timeEnd = System.currentTimeMillis();
		long time = (timeEnd - timeBegin);
		System.out.println("Finish");
		System.out.println("Tempo: " + Double.parseDouble((String.valueOf(time))) / 1000 + " segundos");
	}
}
