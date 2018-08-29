package mathLib.fitting.poly;

import mathLib.optimize.swarm.FitnessFunction;
import mathLib.optimize.swarm.Swarm;
import mathLib.optimize.swarm.particle.Particle;
import mathLib.polynom.Polynomial;
import mathLib.util.MathUtils;
import plotter.chart.MatlabChart;

public class PolynomialFittingPSO {

	int degree ;
	Polynomial polyfit = null ;
	double[] valX, valY ;

	public PolynomialFittingPSO(int degree) {
		this.degree = degree ;
	}

	public void setData(double[] valX, double[] valY) {
		this.valX = valX ;
		this.valY = valY ;
	}

	public void fit() {

		FitnessFunction func = new FitnessFunction() {
			@Override
			public double evaluate(double... position) {
				// 1. define a polynomial of degree n
				polyfit = new Polynomial(position) ;
				double sum = 0 ;
				for(int i=0; i<valX.length; i++) {
					double a = polyfit.evaluate(valX[i]) ;
					sum += (a-valY[i])*(a-valY[i]) ;
				}
				sum /= valX.length ;
				double rmse = Math.sqrt(sum) ;
				return rmse;
			}
		};

		func.setMaximize(false);

		class simpleParticle extends Particle {
			public simpleParticle() {
				super(degree) ;
			}
		}
		// 2. calculate error rmse
		Particle particle = new simpleParticle() ;
		Swarm swarm = new Swarm(100, particle, func) ;

	}

	public double interpolate(double var) {
		return polyfit.evaluate(var) ;
	}

	public Polynomial getPolynomial() {
		return polyfit ;
	}

	@Override
	public String toString() {
		return polyfit ;
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0.0, 2.0, 100) ;
		double[] y = MathUtils.Arrays.Functions.sinc(x) ;
		PolynomialFittingPSO pFit = new PolynomialFittingPSO(6) ;
		pFit.setData(x, y);
		pFit.fit();

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y, "b");

		double[] z = new double[x.length] ;
		for(int i=0; i<z.length; i++)
			z[i] = pFit.interpolate(x[i]) ;

		fig.plot(x, z, "r");
		fig.RenderPlot();
		fig.markerON();
		fig.run(true);

		System.out.println(pFit.getPolynomial());
	}

}
