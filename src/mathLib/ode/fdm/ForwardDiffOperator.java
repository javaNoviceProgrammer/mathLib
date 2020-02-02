package mathLib.ode.fdm;

import static mathLib.util.MathUtils.deltaKronecker;

import mathLib.matrix.Matrix;

public class ForwardDiffOperator {

	int dim ;
	double dx ;
	double[][] Dx ;

	// uniform grid size
	public ForwardDiffOperator(int dim, double dx) {
		this.dim = dim ;
		this.dx = dx ;
		this.Dx = new double[dim][dim] ;
		buildDx() ;
	}

	// forward difference method: y'_i = (y_i+1 - y_i)/dx
	private void buildDx() {
		double coeff = 1.0/dx ;
		for(int i=0; i<dim; i++)
			for(int j=0; j<dim; j++) {
				Dx[i][j] = coeff*(-1.0*deltaKronecker(i, j) + deltaKronecker(i+1, j)) ;
			}
	}

	public double[][] getDx() {
		return Dx ;
	}

	public Matrix getDxMatrix() {
		return new Matrix(Dx) ;
	}

	// for test
	public static void main(String[] args) {
		ForwardDiffOperator D = new ForwardDiffOperator(5, 1.0) ;
		System.out.println(D.getDxMatrix());
	}

}
