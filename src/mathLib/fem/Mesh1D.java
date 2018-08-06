package mathLib.fem;

import mathLib.util.Units;

public class Mesh1D {
	
	double xMin, xMax ;
	Units unit ;
	
	public Mesh1D(double xMin, double xMax, Units unit) {
		this.xMin = xMin ;
		this.xMax = xMax ;
		this.unit = unit ;
	}

}
