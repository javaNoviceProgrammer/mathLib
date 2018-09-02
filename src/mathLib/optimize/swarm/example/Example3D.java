package mathLib.optimize.swarm.example;

import mathLib.optimize.swarm.FitnessFunction;
import mathLib.optimize.swarm.Swarm;
import mathLib.optimize.swarm.particle.Particle;
import mathLib.optimize.swarm.particle.simple.ParticleChooser;
import mathLib.util.Timer;

public class Example3D {

	public static void main(String[] args) {
		Timer timer = new Timer() ;
		timer.start();
		// step 1: define the function
		FitnessFunction func = new FitnessFunction() {

			@Override
			public double evaluate(double... position) {
				// Let's implement f(x) = (x-1.2)^2 + y^2 - z^2
				double x = position[0] ;
				double y = position[1] ;
				double z = position[2] ;
				return Math.pow(x-1.2, 2) + Math.pow(y, 2) - Math.pow(z, 2);
			}
		};

		func.setMaximize(false); // set the optimization criterion (e.g. max or min)

		// step 2: create a particle with the correct optimization dimensions
//		Particle myParticle = new SimpleParticle3D();
		Particle myParticle = ParticleChooser.getParticle(3) ;

		// step 3: create a swarm and evolve it for some number of times
		Swarm swarm = new Swarm(1000, myParticle, func) ;

		/* setting the search interval */
		swarm.setMinPosition(new double[] {-5, -10, -20});
		swarm.setMaxPosition(new double[]{10, 10, 20});

		for(int i=0; i<10000; i++){
			swarm.evolve();
		}
//		System.out.println(swarm.toStringStats());

		// step 4: get optimization values
		double xOptimum = swarm.getBestPosition()[0] ;
		double yOptimum = swarm.getBestPosition()[1] ;
		double zOptimum = swarm.getBestPosition()[2] ;
		double funcMin = swarm.getBestFitness() ;
		timer.stop(); 
		System.out.println(timer);
		System.out.println("min x = " + xOptimum);
		System.out.println("min y = " + yOptimum);
		System.out.println("min z = " + zOptimum);
		System.out.println("min f(x,y,z) = " + funcMin);
	}

}
