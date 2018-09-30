package mathLib.plot.util;

public class MeshGrid {

	int xDim, yDim ;
	double[] x, y ;

	public MeshGrid(double[] x, double[] y){
		this.x = x ;
		this.y = y ;
		this.xDim = x.length ;
		this.yDim = y.length ;
	}

	public double getX(int m, int n) {
		return x[m] ;
	}

	public double getY(int m, int n) {
		return y[n] ;
	}
	
	public double[] getXY(int m, int n) {
		return new double[] {getX(m, n), getY(m, n)} ;
	}

	public int getXDim() {
		return xDim ;
	}

	public int getYDim() {
		return yDim ;
	}

	public int getRowDim() {
		return getXDim() ;
	}

	public int getColumnDim() {
		return getYDim() ;
	}

}
