package mathLib.ode;

import static mathLib.util.MathUtils.*;

import mathLib.matrix.Matrix;

public class DiffOperator {
	
	int dim ;
	double dx ;
	double[][] Dx, Dxx ;
	
	public DiffOperator(int dim, double dx) {
		this.dim = dim ;
		this.dx = dx ;
		this.Dx = new double[dim][dim] ;
		buildDx() ;
		this.Dxx = new double[dim][dim] ;
		buildDxx();
	}
	
	private void buildDx() {
		double coeff = 1.0/(2.0*dx) ;
		for(int i=0; i<dim; i++)
			for(int j=0; j<dim; j++) {
				Dx[i][j] = coeff*(deltaKronecker(i, j-1) - deltaKronecker(i, j+1)) ;
			}
	}
	
	private void buildDxx() {
		double coeff = 1.0/(dx*dx) ;
		for(int i=0; i<dim; i++)
			for(int j=0; j<dim; j++) {
				Dxx[i][j] = coeff*(deltaKronecker(i, j-1) - 2*deltaKronecker(i, j) + deltaKronecker(i, j+1)) ;
			}
	}
	
	public double[][] getDx() {
		return Dx ;
	}
	
	public double[][] getDxx() {
		return Dxx ;
	}
	
	public Matrix getDxMatrix() {
		return new Matrix(Dx) ;
	}
	
	public Matrix getDxxMatrix() {
		return new Matrix(Dxx) ;
	}
	
	
	// for test
	public static void main(String[] args) {
		DiffOperator D = new DiffOperator(10, 1.0) ;
		System.out.println(D.getDxMatrix()*D.getDxMatrix()*4.0);
	}

}
