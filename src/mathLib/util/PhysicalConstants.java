package mathLib.util;

public class PhysicalConstants {
	
	// private constructor to prevent instantiation
	
	private PhysicalConstants() {
		
	}

	// MKS system (SI units)

	public static double getAvogadroNumber(){
		return 6.02214e23 ;
	}

	public static double getBohrRadius(){
		return 0.52917e-10;
	}

	public static double getBoltzmannConstant(){
		return 1.38066e-23;
	}

	public static double getElementaryCharge(){
		return 1.60218e-19;
	}

	public static double getElectronRestMass(){
		return 0.91094e-30;
	}

	public static double getElectronVolt(){
		return 1.60218e-19;
	}

	public static double getGasConstant(){
		return 1.98719;
	}

	public static double getPermittivityVacuum(){
		return 8.85418e-12;
	}

	public static double getPermeabilityVacuum(){
		return (4*Math.PI*1e-7);
	}

	public static double getPlanckConstant(){
		return 6.62607e-34;
	}

	public static double getReducedPlanckConstant(){
		return 1.05457e-34;
	}

	public static double getProtonRestMass(){
		return 1.67262e-27;
	}

	public static double getSpeedOfLightVacuum(){
		return 2.99792e8;
	}

	public static double getStandardAtmoshpere(){
		return 1.01325e5;
	}

	public static double getThermalVoltageAt300K(){
		return 0.025852;
	}

	public static double WavelengthOf1eVQuantum(){
		return 1.23984e-6;
	}

}
