package mathLib.optimize.swarm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A neighborhood of particles
 *
 *
 */
public class Neighborhood1D extends Neighborhood {

	int size;
	boolean circular;
	ArrayList<Particle> array1d;

	/**
	 * Create a 1 dimensional neighborhood (all particles have 2 neighbors: 1 to the left, 1 to the right)
	 * @param size : How many particles to each side do we consider? (total neighborhood is 2*size)
	 * @param circular : If true, the first particle and the last particles are neighbors
	 */
	public Neighborhood1D(int size, boolean circular) {
		super();
		this.size = size;
		this.circular = circular;
		array1d = new ArrayList<Particle>();
	}

	@Override
	public Collection<Particle> calcNeighbours(Particle p) {
		ArrayList<Particle> neigh = new ArrayList<Particle>();
		int idx = findIndex(p); // Find this particle's index

		// Add all the particles in the neighborhood
		for (int i = idx - size; i <= (idx + size); i++) {
			Particle pp = getParticle(i);
			if ((pp != null) && (pp != p)) neigh.add(pp); // Do not add 'p'
		}

		return neigh;
	}

	/**
	 * Find a particle's number
	 * @param p
	 * @return
	 */
	int findIndex(Particle p) {
		for (int i = 0; i < array1d.size(); i++) {
			if (p == array1d.get(i)) return i;
		}
		throw new RuntimeException("Cannot find particle. This should never happen!\n" + p);
	}

	/**
	 * Get particle number 'idx'
	 * @param idx
	 * @return
	 */
	Particle getParticle(int idx) {
		int arraySize = array1d.size();
		if ((idx >= 0) && (idx < array1d.size())) return array1d.get(idx); // Within limits => OK
		if (!circular) return null; // Not circular? => Nothing to do

		if (idx >= arraySize) idx = idx % arraySize;
		else if (idx < 0) idx += arraySize; // This might not work if 'size' > 'arraySize'

		return array1d.get(idx);
	}

	@Override
	public void init(Swarm swarm) {
		// Add all particles to the array
		for (Particle p : swarm)
			array1d.add(p);

		super.init(swarm); // Call to Neighborhood.init() method
	}
}
