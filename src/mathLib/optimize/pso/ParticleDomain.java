package mathLib.optimize.pso;

import java.util.Arrays;
import java.util.List;

public class ParticleDomain {
	
	private List<Interval> intervals ;
	
	public ParticleDomain(Interval... intervals) {
		this.intervals = Arrays.asList(intervals) ;
	}
	
	public boolean isParticleInside(Particle particle) {
		// check the position
		VectorND position = particle.getPosition() ;
		for(int i=0; i<position.dim; i++)
			if(!intervals.get(i).isInsideInterval(position.at(i)))
				return false ;
		return true ;
	}
	
	
	// for test
	public static void main(String[] args) {
		Interval interval1 = new Interval(-1.0, 1.0) ;
		Interval interval2 = new Interval(0.0, 2.0) ;
		Particle particle = new Particle(interval1, new Interval(0,3)) ;
		ParticleDomain domain = new ParticleDomain(interval1, interval2) ;
		System.out.println(domain.isParticleInside(particle));
	}

}
