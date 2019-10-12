package mathLib.optimize.pso;

import java.util.ArrayList;
import java.util.List;

public class PSO {

	// Algorithm parameters
	double w = 0.5 ;
	double c1 = 0.8 ;
	double c2 = 0.9 ;

	int numParticles ;
	// swarm's objective --> optimization goal
	boolean minimize = true ;
	FitnessFunction fitnessFunc ;
	List<Particle> particles ;

	// swarm's parameters --> swarm's best position & value
	VectorND guessBestPosition ;
	double guessBestValue = Double.MAX_VALUE ;

	public PSO(int numParticles, FitnessFunction fitnessFunc, Interval... intervals) {
		this.numParticles = numParticles ;
		this.fitnessFunc = fitnessFunc ;
		this.particles = new ArrayList<Particle>() ;
		for(int i=0; i<numParticles; i++)
			particles.add(new Particle(intervals)) ;
		double[] x = new double[intervals.length] ;
		for(int i=0; i<intervals.length; i++)
			x[i] = intervals[i].getRandom() ;
		this.guessBestPosition = new VectorND(x) ;
	}

	// Interval class --> package level access: static interval method
	public static Interval interval(double start, double end) {
		return new Interval(start, end) ;
	}

	public void setMinimize(boolean value) {
		this.minimize = value ;
	}

	// returns the value of the fitness function at the particle's position
	private double fitness(Particle particle) {
		if(fitnessFunc == null)
			throw new IllegalArgumentException("Fitness function is not set") ;
		if(minimize)
			return fitnessFunc.value(particle.position.x) ;
		else
			// reverse the sign of the fitness function to maximize it --> min(-f) == max(f)
			return -fitnessFunc.value(particle.position.x) ;
	}

	public void printParticles() {
		for(Particle particle : particles)
			System.out.println(particle) ;
	}

	private void moveParticles() {
		for(Particle particle : particles) {
			// update the velocity
			double r1 = Math.random() ; // [0,1)
			double r2 = Math.random() ; // [0,1)
			VectorND newVelocity = (w*particle.velocity) + c1*r1*(particle.bestPosition-particle.position) +
									c2*r2*(guessBestPosition-particle.bestPosition) ;
			particle.velocity = newVelocity ;
			// update the position
			particle.move() ;
		}
	}

	private void updateParticleBestParams() {
		// history of particle based on swarm's objective
		for(Particle particle : particles){
			double fitnessValue = fitness(particle) ;
			if(particle.bestValue > fitnessValue) { // default --> minimizing
				particle.bestValue = fitnessValue ;
				particle.bestPosition = particle.position ;
			}
		}
	}

	// Guess --> swarm's parameters
	private void updateGuessBestParams() {
		for(Particle particle : particles){
			double fitnessValue = fitness(particle) ;
			if(guessBestValue > fitnessValue) { // default --> minimizing
				guessBestValue = fitnessValue ;
				guessBestPosition = particle.position ;
			}
		}
	}

	public void solve(int numIterations) {
		int iterations = 0 ;
		while(iterations < numIterations) {
			// particle's behavior
			updateParticleBestParams() ;
			// swarm's behavior
			updateGuessBestParams() ;
			// move particles
			moveParticles() ;
			// increase iterations
			iterations++ ;
		}
	}

	public double bestValue() {
		if(minimize)
			return guessBestValue ;
		else
			return -guessBestValue ;
	}

	public VectorND bestPosition() {
		return guessBestPosition ;
	}

	// test
	public static void main(String[] args) {
		FitnessFunction func = t -> Math.sin(t[0]) ; // 1d function: sin(x)
		PSO pso = new PSO(10, func, interval(0, Math.PI)) ;
		pso.setMinimize(false); // max --> x = pi/2
		pso.solve(100) ;
		System.out.println(pso.bestValue());
		System.out.println(pso.bestPosition());
		System.out.println(Math.PI/2.0);
	}

}
