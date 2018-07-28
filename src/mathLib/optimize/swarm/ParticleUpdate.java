package mathLib.optimize.swarm;

/**
 * Particle update strategy
 *
 * Every Swarm.evolve() itereation the following methods are called
 * 		- begin(Swarm) : Once at the begining of each iteration
 * 		- update(Swarm,Particle) : Once for each particle
 * 		- end(Swarm) : Once at the end of each iteration
 *
 *
 */
public abstract class ParticleUpdate {

	/**
	 * Constructor
	 * @param particle : Sample of particles that will be updated later
	 */
	public ParticleUpdate(Particle particle) {
	}

	/**
	 * This method is called at the begining of each iteration
	 * Initialize random vectors use for local and global updates (rlocal[] and rother[])
	 */
	public void begin(Swarm swarm) {
	}

	/** This method is called at the end of each iteration */
	public void end(Swarm swarm) {
	}

	/** Update particle's velocity and position */
	public abstract void update(Swarm swarm, Particle particle);
}
