package mathLib.optimize.swarm;

import java.util.Collection;
import java.util.HashMap;

/**
 * A neighborhood of particles
 *
 *
 */
public abstract class Neighborhood {

	// All neighborhoods are stored here, so that we do not need to calculate them each time
	HashMap<Particle, Collection<Particle>> neighborhoods;
	// The best particle in the neighborhood is stored here
	HashMap<Particle, Particle> bestInNeighborhood;

	public Neighborhood() {
		neighborhoods = new HashMap<Particle, Collection<Particle>>();
		bestInNeighborhood = new HashMap<Particle, Particle>();
	}

	/**
	 * Calculate all neighbors of particle 'p'
	 *
	 * Note: The p's neighbors DO NOT include 'p'
	 *
	 * @param p : a particle
	 * @return A collection with all neighbors
	 */
	public abstract Collection<Particle> calcNeighbours(Particle p);

	/**
	 * Get the best particle in the neighborhood
	 * @param p
	 * @return The best particle in the neighborhood of 'p'
	 */
	public Particle getBestParticle(Particle p) {
		return bestInNeighborhood.get(p);
	}

	/**
	 * Get the best position ever found by all the particles in the neighborhood of 'p'
	 * @param p
	 * @return The best position in the neighborhood of 'p'
	 */
	public double[] getBestPosition(Particle p) {
		Particle bestp = getBestParticle(p);
		if (bestp == null) return null;
		return bestp.getBestPosition();
	}

	/**
	 * Get all neighbors of particle 'p'
	 * @param p : a particle
	 * @return A collection with all neighbors
	 */
	public Collection<Particle> getNeighbours(Particle p) {
		Collection<Particle> neighs = neighborhoods.get(p);
		if (neighs == null) neighs = calcNeighbours(p);
		return neighs;
	}

	/**
	 * Initialize neighborhood
	 * @param swarm
	 * @return
	 */
	public void init(Swarm swarm) {
		// Create neighborhoods for each particle
		for (Particle p : swarm) {
			Collection<Particle> neigh = getNeighbours(p);
			neighborhoods.put(p, neigh);
		}
	}

	/**
	 * Update neighborhood: This is called after each iteration
	 * @param swarm
	 * @return
	 */
	public void update(Swarm swarm, Particle p) {
		// Find best fitness in this neighborhood
		Particle pbest = getBestParticle(p);
		if ((pbest == null) || swarm.getFitnessFunction().isBetterThan(pbest.getBestFitness(), p.getBestFitness())) {
			// Particle 'p' is the new 'best in neighborhood' => we need to update all neighbors
			Collection<Particle> neigh = getNeighbours(p);
			for (Particle pp : neigh) {
				bestInNeighborhood.put(pp, p);
			}
		}
	}
}
