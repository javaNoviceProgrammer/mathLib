package mathLib.optimize.swarm;

/**
 * Particle update: Each particle selects an rlocal and rother
 * independently from other particles' values
 *
 *
 */
public class ParticleUpdateRandomByParticle extends ParticleUpdate {

	/**
	 * Constructor
	 * @param particle : Sample of particles that will be updated later
	 */
	public ParticleUpdateRandomByParticle(Particle particle) {
		super(particle);
	}

	/** Update particle's velocity and position */
	@Override
	public void update(Swarm swarm, Particle particle) {
		double position[] = particle.getPosition();
		double velocity[] = particle.getVelocity();
		double globalBestPosition[] = swarm.getBestPosition();
		double particleBestPosition[] = particle.getBestPosition();
		double neighBestPosition[] = swarm.getNeighborhoodBestPosition(particle);

		double rlocal = Math.random();
		double rneighborhood = Math.random();
		double rglobal = Math.random();

		// Update velocity and position
		for (int i = 0; i < position.length; i++) {
			// Update position
			position[i] = position[i] + velocity[i];

			// Update velocity
			velocity[i] = swarm.getInertia() * velocity[i] // Inertia
					+ rlocal * swarm.getParticleIncrement() * (particleBestPosition[i] - position[i]) // Local best
					+ rneighborhood * swarm.getNeighborhoodIncrement() * (neighBestPosition[i] - position[i]) // Neighborhood best
					+ rglobal * swarm.getGlobalIncrement() * (globalBestPosition[i] - position[i]); // Global best
		}
	}
}
