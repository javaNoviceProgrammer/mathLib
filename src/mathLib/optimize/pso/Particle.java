package mathLib.optimize.pso;

import java.util.List;

// package level access
// not accessible to the user
// internally PSO uses this particle class

class Particle {

	// N-dimensional particle
	int dim ;
	VectorND position ;
	VectorND velocity ;
	VectorND bestPosition ;
	double bestValue ; // for the fitness function --> tracking the particle motion in
						// in accordance to the swarm's objective

	public Particle(Interval... intervals) {
		this.dim = intervals.length ;
		double[] initialPosition = new double[dim] ;
		for(int i=0; i<dim; i++)
			initialPosition[i] = intervals[i].getRandom() ;
		this.position = new VectorND(initialPosition) ;
		this.velocity = new VectorND(new double[dim]) ; // velocity = [0, 0, 0, ..., 0]
		this.bestPosition = position ;
		this.bestValue = Double.MAX_VALUE ;
	}

	public Particle(List<Interval> intervals) {
		this.dim = intervals.size() ;
		double[] initialPosition = new double[dim] ;
		for(int i=0; i<dim; i++)
			initialPosition[i] = intervals.get(i).getRandom() ;
		this.position = new VectorND(initialPosition) ;
		this.velocity = new VectorND(new double[dim]) ;
		this.bestPosition = position ;
		this.bestValue = Double.MAX_VALUE ;
	}

	// one useful method
	public void move() {
		position = position + velocity ;
	}

	@Override
	public String toString() {
		return "Particle at " + position.toString() ;
	}

	public static void main(String[] args) {
		Particle p1 = new Particle(new Interval(2,4), new Interval(-2,2)) ;
		System.out.println(p1);

	}


}
