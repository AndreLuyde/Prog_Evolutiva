package Evolutiva;

import java.util.ArrayList;

public class Individuo {
	private RouteSolution solution;
	private Double sigma;
	private ArrayList<Double> sigma2;
	private ArrayList<Double> sigma3;
	private ArrayList<Double> alpha;

	public Individuo(RouteSolution solucao) {
		setSolution(solucao);
	}

	public void mutacao(Individuo individuo) {
		individuo.setSigma(individuo.getSigma() * Math.exp(individuo.getSigma()));// mutação
																					// nao-correlacionada
																					// do
																					// sigma
		// mutação nao-correlacionada
		for (Double s2 : individuo.getSigma2()) {

		}
		// mutação correlacionada
		for (Double s3 : individuo.getSigma3()) {

		}
		individuo.getSolution().mutationRoute1(individuo.getSolution());
	}

	public RouteSolution getSolution() {
		return solution;
	}

	public void setSolution(RouteSolution solution) {
		this.solution = solution;
	}

	public Double getSigma() {
		return sigma;
	}

	public void setSigma(Double sigma) {
		this.sigma = sigma;
	}

	public ArrayList<Double> getSigma2() {
		return sigma2;
	}

	public void setSigma2(ArrayList<Double> sigma2) {
		this.sigma2 = sigma2;
	}

	public ArrayList<Double> getSigma3() {
		return sigma3;
	}

	public void setSigma3(ArrayList<Double> sigma3) {
		this.sigma3 = sigma3;
	}

	public ArrayList<Double> getAlpha() {
		return alpha;
	}

	public void setAlpha(ArrayList<Double> alpha) {
		this.alpha = alpha;
	}
}
