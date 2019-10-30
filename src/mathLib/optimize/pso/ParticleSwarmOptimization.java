package mathLib.optimize.pso;

import java.util.ArrayList;
import java.util.List;

import mathLib.plot.MatlabChart;

public class ParticleSwarmOptimization {

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
	VectorND swarmBestPosition ;
	double swarmBestValue = Double.MAX_VALUE ;

	boolean visualize ;
	MatlabChart fig ;
	
	// bounded optimization
	boolean isBounded = false ;
	ParticleDomain psoDomain ;

	public ParticleSwarmOptimization(int numParticles, FitnessFunction fitnessFunc, Interval... intervals) {
		this.numParticles = numParticles ;
		this.fitnessFunc = fitnessFunc ;
		this.particles = new ArrayList<Particle>() ;
		for(int i=0; i<numParticles; i++)
			particles.add(new Particle(intervals)) ;
		double[] x = new double[intervals.length] ;
		for(int i=0; i<intervals.length; i++)
			x[i] = intervals[i].getRandom() ;
		this.swarmBestPosition = new VectorND(x) ;
		psoDomain = new ParticleDomain(intervals) ;
	}

	// Interval class --> package level access: static interval method
	public static Interval interval(double start, double end) {
		return new Interval(start, end) ;
	}
	
	public void boundedOptimization(boolean isBounded) {
		this.isBounded = isBounded ;
	}

	public void visualize(boolean visualize) {
		this.visualize = visualize ;
		if(visualize) {
			fig = new MatlabChart() ;
			fig.plot(new double[0], new double[0]);
			fig.renderPlot();
			fig.font(13);
			fig.xlabel("Iteration step");
			fig.ylabel("Swarm's best value");
			fig.run(true);
		}
	}

	public List<Particle> getParticles() {
		return particles ;
	}

	public int getNumberOfPartciles() {
		return numParticles ;
	}

	public FitnessFunction getFitnessFunction() {
		return fitnessFunc ;
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
			VectorND oldVelocity = particle.velocity ;
			VectorND newVelocity = (w*particle.velocity) + c1*r1*(particle.bestPosition-particle.position) +
									c2*r2*(swarmBestPosition-particle.bestPosition) ;
			particle.velocity = newVelocity ;
			// update the position
			particle.move() ;
			// check for bounded optimization
			if(isBounded && !psoDomain.isParticleInside(particle)) {
				particle.moveBack() ;
				particle.velocity = oldVelocity ;
			}	
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

	// swarm's parameters
	private void updateSwarmBestParams() {
		for(Particle particle : particles){
			double fitnessValue = fitness(particle) ;
			if(swarmBestValue > fitnessValue) { // default --> minimizing
				swarmBestValue = fitnessValue ;
				swarmBestPosition = particle.position ;
			}
		}
	}

	public void solve(int numIterations) {
		if(visualize)
			solveAndPlot(numIterations);
		else {
			int iterations = 0 ;
			while(iterations < numIterations) {
				// particle's behavior
				updateParticleBestParams() ;
				// swarm's behavior
				updateSwarmBestParams() ;
				// move particles
				moveParticles() ;
				// increase iterations
				iterations++ ;
			}
		}
	}

	public void solveAndPlot(int numIterations) {
		visualize(true);
		int iterations = 0 ;
		while(iterations < numIterations) {
			// particle's behavior
			updateParticleBestParams() ;
			// swarm's behavior
			updateSwarmBestParams() ;
			// move particles
			moveParticles() ;
			// increase iterations
			iterations++ ;
			// plot the fitness values
			fig.append(0, iterations, swarmBestValue);
		}
	}

	public void next() {
		// particle's behavior
		updateParticleBestParams() ;
		// swarm's behavior
		updateSwarmBestParams() ;
		// move particles
		moveParticles() ;
	}

	public double bestValue() {
		if(minimize)
			return swarmBestValue ;
		else
			return -swarmBestValue ;
	}

	public VectorND bestPosition() {
		return swarmBestPosition ;
	}

	// **************** testing *****************
	public static void main(String[] args) {
		test2() ;
	}
	
//	private static void test1() {
//		Timer timer = new Timer();
//		timer.start();
//		FitnessFunction func = t -> Math.sin(t[0])*Math.cos(t[1])*Math.sin(t[2]*t[2]) ; // 2d function: sin(x)
//		ParticleSwarmOptimization pso = new ParticleSwarmOptimization(20, func,
//																	  interval(0, 2*Math.PI),
//																	  interval(0, 2*Math.PI),
//																	  interval(0, 2*Math.PI)) ;
//		pso.setMinimize(true);
//		pso.solve(50);
//		timer.stop();
//		timer.show();
//		System.out.println(pso.bestValue());
//		System.out.println(pso.bestPosition());
//	}
	
	private static void test2() {
		FitnessFunction func = t -> t[0]*t[0] + t[0] - 1.0 ;
		ParticleSwarmOptimization pso = new ParticleSwarmOptimization(20, func, interval(-2.0, 1.0)) ;
		pso.setMinimize(true);
		pso.visualize(true);
		pso.boundedOptimization(true);
		pso.solve(100);
		System.out.println(pso.bestValue());
		System.out.println(pso.bestPosition());
	}

}
