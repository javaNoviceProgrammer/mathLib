package mathLib.fitting.poly;

import mathLib.optimize.swarm.FitnessFunction;
import mathLib.optimize.swarm.Swarm;
import mathLib.optimize.swarm.particle.Particle;
import mathLib.optimize.swarm.particle.simple.SimpleParticle10D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle1D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle2D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle3D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle4D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle5D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle6D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle7D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle8D;
import mathLib.optimize.swarm.particle.simple.SimpleParticle9D;
import mathLib.plot.MatlabChart;
import mathLib.polynom.Polynomial;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

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
		// 2. calculate error rmse

		Particle particle = getParticle() ;

		if (particle == null)
			throw new IllegalArgumentException("degree of polynomial not supported!") ;

		Swarm swarm = new Swarm(100, particle, func) ;
		double[] maxPosition = new double[degree+1] ;
		double[] minPosition = new double[degree+1] ;
		for(int i=0; i<maxPosition.length; i++) {
			maxPosition[i] = 100 ;
			minPosition[i] = -100 ;
		}
		swarm.setMaxPosition(maxPosition);
		swarm.setMinPosition(minPosition);
		for(int i=0; i<10000; i++)
			swarm.evolve();

		polyfit = new Polynomial(swarm.getBestPosition()) ;

	}


	@SuppressWarnings("unchecked")
	private <T extends Particle> T getParticle() {
		switch (degree) {
		case 0:
			return (T) new SimpleParticle1D() ;
		case 1:
			return (T) new SimpleParticle2D() ;
		case 2:
			return (T) new SimpleParticle3D() ;
		case 3:
			return (T) new SimpleParticle4D() ;
		case 4:
			return (T) new SimpleParticle5D() ;
		case 5:
			return (T) new SimpleParticle6D() ;
		case 6:
			return (T) new SimpleParticle7D() ;
		case 7:
			return (T) new SimpleParticle8D() ;
		case 8:
			return (T) new SimpleParticle9D() ;
		case 9:
			return (T) new SimpleParticle10D() ;
		default:
			return null ;
		}
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
		double[] x = MathUtils.linspace(0.0, 1*Math.PI, 100) ;
		double[] y = MathUtils.Arrays.Functions.sin(x) ;
		
		Timer timer = new Timer() ;
		timer.start();
		PolynomialFittingPSO pFit = new PolynomialFittingPSO(2) ;
		pFit.setData(x, y);
		pFit.fit();
		timer.stop();
		System.out.println(timer);

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y, "b");

		double[] z = new double[x.length] ;
		for(int i=0; i<z.length; i++)
			z[i] = pFit.interpolate(x[i]) ;

		fig.plot(x, z, "r");
		fig.renderPlot();
		fig.markerON();
		fig.run(true);

		System.out.println(pFit.getPolynomial());
	}

}
