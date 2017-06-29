package Evolutiva;

import java.util.Random;

public class Experimentos {
	Random r = new Random();
	
	public Experimentos() {

	}

	public void run(RouteProblem[] problemas) {
		for (int i = 0; i < 5 * problemas.length; i++) {
			int aleatorio = r.nextInt((problemas.length - 2) + 1);
			funcaoTeste(problemas[aleatorio]);
		}
	}

	public void funcaoTeste(RouteProblem problema) {

	}
}
