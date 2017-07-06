package Evolutiva;

import java.util.Random;

public class Individuo {
	private RouteSolution solution;
	private Double sigma;
	private Double tau;
	private Double[] sigma2 = null;
	private Double[] sigma3;
	private Double[] alpha;
	Random r = new Random();
	
	public Individuo(RouteSolution solucao, Double sigma) {
		setSigma(sigma);
		Double tau = (double) (1/((solucao.getSolucao().size())^(1/2))); 
		setTau(tau);
		setSolution(solucao);
	}

	public void mutacao(Individuo individuo) {
		//mutacao correlacionada do sigma
		individuo.setSigma(individuo.getSigma() * Math.exp(individuo.getTau() * r.nextGaussian()));

		//mutacao nao-correlacionada
		for (int i = 0; i < sigma2.length; i++) {
			sigma2[i] = (Double) (individuo.getSigma() * Math.exp(individuo.getTau() * r.nextGaussian() + individuo.getTau() * r.nextGaussian()));
		}
		// mutacao correlacionada
		for (int i = 0; i < sigma3.length; i++) {
			sigma3[i] = (Double) (individuo.getSigma() * Math.exp(individuo.getTau() * r.nextGaussian() + individuo.getTau() * r.nextGaussian()));
		}
		
		if(individuo.getSigma() < (1/(3*individuo.getSigma()))){
			individuo.getSolution().mutationRoute1(individuo.getSolution());
		}else if(individuo.getSigma() < (2/(3*individuo.getSigma()))){
			individuo.getSolution().mutationRouteByChange(individuo.getSolution());
		}else{
			individuo.getSolution().mutationRouteByShuffle(individuo.getSolution());
		}
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

	public Double[] getSigma2() {
		return sigma2;
	}

	public void setSigma2(Double[] sigma2) {
		this.sigma2 = sigma2;
	}

	public Double[] getSigma3() {
		return sigma3;
	}

	public void setSigma3(Double[] sigma3) {
		this.sigma3 = sigma3;
	}

	public Double[] getAlpha() {
		return alpha;
	}

	public void setAlpha(Double[] alpha) {
		this.alpha = alpha;
	}

	public Double getTau() {
		return tau;
	}

	public void setTau(Double tau) {
		this.tau = tau;
	}
}
