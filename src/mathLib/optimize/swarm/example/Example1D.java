package mathLib.optimize.swarm.example;

import mathLib.optimize.swarm.FitnessFunction;
import mathLib.optimize.swarm.Swarm;
import mathLib.optimize.swarm.particle.Particle;
import mathLib.optimize.swarm.particle.SimpleParticle1D;

public class Example1D {

	public static void main(String[] args) {

		// step 1: define the function
		FitnessFunction func = new FitnessFunction() {

			@Override
			public double evaluate(double... position) {
				// Let's implement f(x) = x^2-3*x+1
				double x = position[0] ;
				return Math.pow(x, 2)-3*x+1;
			}
		};

		func.setMaximize(false); // set the optimization criterion (e.g. max or min)

		// step 2: create a particle with the correct optimization dimensions
		Particle myParticle = new SimpleParticle1D();

		// step 3: create a swarm and evolve it for some number of times
		Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES, myParticle, func) ;

		/* setting the search interval */
		swarm.setMinPosition(-10.0);
		swarm.setMaxPosition(10.0);

		for(int i=0; i<500; i++){
			swarm.evolve();
		}
//		System.out.println(swarm.toStringStats());

		// step 4: get optimization values
		double xOptimum = swarm.getBestPosition()[0] ;
		double funcMin = swarm.getBestFitness() ;
		System.out.println("min x = " + xOptimum);
		System.out.println("min f(x) = " + funcMin);
	}

}
