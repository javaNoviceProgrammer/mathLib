package mathLib.util;

public class PhysicalConstants {
	
	// private constructor to prevent instantiation
	
	private PhysicalConstants() {
		
	}

	// MKS system (SI units)

	public static double AvogadroNumber(){
		return 6.02214e23 ;
	}

	public static double BohrRadius(){
		return 0.52917e-10;
	}

	public static double BoltzmannConstant(){
		return 1.38066e-23;
	}

	public static double ElementaryCharge(){
		return 1.60218e-19;
	}

	public static double ElectronRestMass(){
		return 0.91094e-30;
	}

	public static double ElectronVolt(){
		return 1.60218e-19;
	}

	public static double GasConstant(){
		return 1.98719;
	}

	public static double PermittivityVacuum(){
		return 8.85418e-12;
	}

	public static double PermeabilityVacuum(){
		return (4*Math.PI*1e-7);
	}

	public static double PlanckConstant(){
		return 6.62607e-34;
	}

	public static double ReducedPlanckConstant(){
		return 1.05457e-34;
	}

	public static double ProtonRestMass(){
		return 1.67262e-27;
	}

	public static double SpeedOfLightVacuum(){
		return 2.99792e8;
	}

	public static double StandardAtmoshpere(){
		return 1.01325e5;
	}

	public static double ThermalVoltageAt300K(){
		return 0.025852;
	}

	public static double WavelengthOf1eVQuantum(){
		return 1.23984e-6;
	}

}
