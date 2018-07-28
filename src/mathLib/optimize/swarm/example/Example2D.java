package mathLib.optimize.swarm.example;

import mathLib.optimize.swarm.FitnessFunction;
import mathLib.optimize.swarm.Swarm;
import mathLib.optimize.swarm.particle.Particle;
import mathLib.optimize.swarm.particle.SimpleParticle2D;

public class Example2D {

	public static void main(String[] args) {

		// step 1: define the function
		FitnessFunction func = new FitnessFunction() {

			@Override
			public double evaluate(double... position) {
				// Let's implement f(x) = (x-1.2)^2 + y^2
				double x = position[0] ;
				double y = position[1] ;
				return Math.pow(x-1.2, 2) + Math.pow(y, 2);
			}
		};

		func.setMaximize(false); // set the optimization criterion (e.g. max or min)

		// step 2: create a particle with the correct optimization dimensions
		Particle myParticle = new SimpleParticle2D();

		// step 3: create a swarm and evolve it for some number of times
		Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES, myParticle, func) ;

		/* setting the search interval */
		swarm.setMinPosition(new double[] {-5, -10});
		swarm.setMaxPosition(new double[]{10, 10});

		for(int i=0; i<10000; i++){
			swarm.evolve();
		}
//		System.out.println(swarm.toStringStats());

		// step 4: get optimization values
		double xOptimum = swarm.getBestPosition()[0] ;
		double yOptimum = swarm.getBestPosition()[1] ;
		double funcMin = swarm.getBestFitness() ;
		System.out.println("min x = " + xOptimum);
		System.out.println("min y = " + yOptimum);
		System.out.println("min f(x,y) = " + funcMin);
	}

}
