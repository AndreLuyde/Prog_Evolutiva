package Evolutiva;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

public class Principal {
	private static BufferedReader readArq;

	public static void main(String[] args) throws IOException {
		// Teste GA
		Pontos p1 = new Pontos();
//		p1.setDistancias(distancias);	
		ArrayList<Pontos> pontos = new ArrayList<Pontos>();
		RouteProblem problema = new RouteProblem(100, pontos);
		RouteSolution solucao = new RouteSolution(problema, pontos, 0);

		// leitura arquivo com rotas
		JFileChooser fileopen = new JFileChooser();
		int ret = fileopen.showDialog(null, "Open file");
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fileopen.getSelectedFile();
			readArq = new BufferedReader(new FileReader(file));
			String linha = readArq.readLine();
		}
	}
}
