package mathLib.pde;

import static mathLib.util.MathUtils.deltaKronecker;

import Jama.EigenvalueDecomposition;
import mathLib.matrix.Matrix;
import mathLib.util.Timer;

/**
 * this class is to represent dxx + dyy
 *
 */
public class DiffOperator2D {

	int xDim, yDim ;
	double dx, dy ;
	double[][] DxxPlusDyy, Dx, Dy, Dxx, Dyy ;

	public DiffOperator2D(int nx, int ny, double dx, double dy) {
		this.xDim = nx ;
		this.yDim = ny ;
		this.dx = dx ;
		this.dy = dy ;
		buildDxxPlusDyy() ;
		buildDxx();
		buildDx();
		buildDyy();
		buildDy();
	}

	private void buildDxxPlusDyy() {
		int M = xDim*yDim ;
		DxxPlusDyy = new double[M][M] ;
		for(int i=0; i<M; i++) {
			for(int j=0; j<M; j++) {
				DxxPlusDyy[i][j] = 1.0/(dx*dx) * (deltaKronecker(i, j-yDim)-2*deltaKronecker(i, j)+deltaKronecker(i, j+yDim))
						+ 1.0/(dy*dy) * (deltaKronecker(i, j+1)-2*deltaKronecker(i, j)+deltaKronecker(i, j-1)) ;
			}
		}
	}

	private void buildDxx() {
		int M = xDim*yDim ;
		Dxx = new double[M][M] ;
		for(int i=0; i<M; i++) {
			for(int j=0; j<M; j++) {
				Dxx[i][j] = 1.0/(dx*dx) * (deltaKronecker(i, j-yDim)-2*deltaKronecker(i, j)+deltaKronecker(i, j+yDim));
			}
		}
	}

	private void buildDx() {
		int M = xDim*yDim ;
		Dx = new double[M][M] ;
		for(int i=0; i<M; i++) {
			for(int j=0; j<M; j++) {
				Dx[i][j] = 1.0/(2.0*dx) * (deltaKronecker(i, j-yDim) - deltaKronecker(i, j+yDim));
			}
		}
	}

	private void buildDyy() {
		int M = xDim*yDim ;
		Dyy = new double[M][M] ;
		for(int i=0; i<M; i++) {
			for(int j=0; j<M; j++) {
				Dyy[i][j] = 1.0/(dy*dy) * (deltaKronecker(i, j+1)-2*deltaKronecker(i, j)+deltaKronecker(i, j-1));
			}
		}
	}

	private void buildDy() {
		int M = xDim*yDim ;
		Dy = new double[M][M] ;
		for(int i=0; i<M; i++) {
			for(int j=0; j<M; j++) {
				Dy[i][j] = 1.0/(2.0*dy) * (deltaKronecker(i, j+1) - deltaKronecker(i, j-1));
			}
		}
	}

	public double[][] getDxxPlusDyy() {
		return DxxPlusDyy ;
	}

	public Matrix getDxxPlusDyyMatrix() {
		return new Matrix(DxxPlusDyy) ;
	}

	public double[][] getDx() {
		return Dx ;
	}

	public Matrix getDxMatrix() {
		return Dx ;
	}

	public double[][] getDxx() {
		return Dxx ;
	}

	public Matrix getDxxMatrix() {
		return Dxx ;
	}

	public double[][] getDy() {
		return Dy ;
	}

	public Matrix getDyMatrix() {
		return Dy ;
	}

	public double[][] getDyy() {
		return Dyy ;
	}

	public Matrix getDyyMatrix() {
		return Dyy ;
	}

	// for test
	public static void main(String[] args) {
		DiffOperator2D diff = new DiffOperator2D(400/10, 220/10, 1.0, 1.0) ;
//		System.out.println(diff.getDxxPlusDyyMatrix());
		Timer timer = new Timer() ;
		timer.start();
		EigenvalueDecomposition eigen = new EigenvalueDecomposition(Matrix.toJamaMatrix(diff.getDxxPlusDyyMatrix())) ;
		timer.end();
		System.out.println(timer);
//		System.out.println(new Matrix(eigen.getD().getArray()) );
	}

}
