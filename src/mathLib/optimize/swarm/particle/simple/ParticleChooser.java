package mathLib.optimize.swarm.particle.simple;

import mathLib.optimize.swarm.particle.Particle;

public class ParticleChooser {
	
	@SuppressWarnings("unchecked")
	public static <T extends Particle> T getParticle(int dim) {
		switch (dim) {
		case 1:
			return (T) new SimpleParticle1D() ;
		case 2:
			return (T) new SimpleParticle2D() ;
		case 3:
			return (T) new SimpleParticle3D() ;
		case 4:
			return (T) new SimpleParticle4D() ;
		case 5:
			return (T) new SimpleParticle5D() ;
		case 6:
			return (T) new SimpleParticle6D() ;
		case 7:
			return (T) new SimpleParticle7D() ;
		case 8:
			return (T) new SimpleParticle8D() ;
		case 9:
			return (T) new SimpleParticle9D() ;
		case 10:
			return (T) new SimpleParticle10D() ;
		case 11:
			return (T) new SimpleParticle11D() ;
		case 12:
			return (T) new SimpleParticle12D() ;
		case 13:
			return (T) new SimpleParticle13D() ;
		default:
			throw new IllegalArgumentException("Particle not implemented yet");
		}
	}

}
