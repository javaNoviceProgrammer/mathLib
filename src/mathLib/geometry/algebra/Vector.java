package mathLib.geometry.algebra;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

import mathLib.matrix.Matrix;

public class Vector {

	private double[] data;
	private int dim;

	public Vector(double[] data) {
		this.data = data;
		dim = data.length;
	}
	
	public Vector(int dim) {
		this.dim = dim;
		this.data = new double[dim];
	}

	public int getDim() {
		return dim;
	}
	
	public int size() {
		return getDim() ;
	}
	
	public static Vector constant(int dim, double a) {
		double[] d = new double[dim] ;
		for(int i=0; i<dim; i++)
			d[i] = a ;
		return new Vector(d) ;
	}

	public double getNorm2() {
		double norm = 0;
		for (int i = 0; i < dim; i++) {
			norm += data[i] * data[i];
		}
		return Math.sqrt(norm);
	}

	public double getNorm1() {
		double norm = 0;
		for (int i = 0; i < dim; i++) {
			norm += Math.abs(data[i]);
		}
		return norm;
	}

	public double getNormInf() {
		double norm = Double.MIN_VALUE;
		for (int i = 0; i < dim; i++) {
			if (norm <= Math.abs(data[i]))
				norm = Math.abs(data[i]);
		}
		return norm;
	}

	public double getNormP(double p) {
		double norm = 0;
		for (int i = 0; i < dim; i++) {
			norm += pow(pow(abs(data[i]), p), 1.0 / p);
		}
		return norm;
	}
	
	public Matrix asMatrix() {
		return asRowMatrix().transpose() ;
	}
	
	public Matrix asRowMatrix() {
		return data ;
	}
	
	public double[] asArray() {
		return data ;
	}

//	@Override
//	public String toString() {
//		StringBuilder s = new StringBuilder();
//		s.append("[");
//		for (int i = 0; i < dim - 1; i++) {
//			s.append(data[i]);
//			s.append(", ");
//		}
//		s.append(data[dim - 1]);
//		s.append("]");
//
//		return s;
//	}
	
	@Override
	public String toString() {
		return this.asMatrix().toString() ;
	}
	
	public Vector plus(Vector v) {
		double[] temp = new double[dim];
		for (int i = 0; i < dim; i++) {
			temp[i] = this.data[i] + v.data[i];
		}
		return new Vector(temp);
	}

	public Vector plus(double v) {
		double[] temp = new double[dim];
		for (int i = 0; i < dim; i++) {
			temp[i] = this.data[i] + v;
		}
		return new Vector(temp);
	}
	
	public Vector minus(Vector v) {
		return v.times(-1).plus(this) ;
	}

	public Vector minus(double v) {
		return this.plus(-v) ;
	}
	
	public Vector timesElement(Vector v) {
		double[] temp = new double[dim];
		for (int i = 0; i < dim; i++) {
			temp[i] = this.data[i] * v.data[i];
		}
		return new Vector(temp);
	}
	
	public Vector times(double a) {
		double[] temp = new double[dim];
		for (int i = 0; i < dim; i++) {
			temp[i] = this.data[i] * a;
		}
		return new Vector(temp);
	}

	public Vector divides(double a) {
		double[] temp = new double[dim];
		for (int i = 0; i < dim; i++) {
			temp[i] = this.data[i] / a;
		}
		return new Vector(temp);
	}
	
	public Vector divideElement(Vector v) {
		double[] temp = new double[dim];
		for (int i = 0; i < dim; i++) {
			temp[i] = this.data[i] / v.data[i];
		}
		return new Vector(temp);
	}
	
	public Matrix transpose() {
		return this.asRowMatrix() ;
	}
	
    // ************ operator overloading **********************

    public static Vector valueOf(double[] v) {
    	return new Vector(v) ;
    }
    
    public static Vector valueOf(int[] v) {
    	int M = v.length ;
    	double[] data = new double[M] ;
    	for(int i=0; i<M; i++)
    			data[i] = (double) v[i] ;
    	return new Vector(data) ;
    }
    
    public static Vector valueOf(float[] v) {
    	int M = v.length ;
    	double[] data = new double[M] ;
    	for(int i=0; i<M; i++)
    			data[i] = (double) v[i] ;
    	return new Vector(data) ;
    }

    public static Vector valueOf(long[] v) {
    	int M = v.length ;
    	double[] data = new double[M] ;
    	for(int i=0; i<M; i++)
    			data[i] = (double) v[i] ;
    	return new Vector(data) ;
    }

 	/**
 	 * Operator overload support: a+b
 	 */
 	public Vector add(Vector v) {
 		return this.plus(v) ;
 	}

 	public Vector addRev(Vector v) {
 		return v.plus(this) ;
 	}

 	public Vector add(int v) {
 		return this.plus(v) ;
 	}

 	public Vector addRev(int v) {
 		return this.plus(v) ;
 	}

 	public Vector add(long v) {
 		return this.plus(v) ;
 	}

 	public Vector addRev(long v) {
 		return this.plus(v) ;
 	}

 	public Vector add(float v) {
 		return this.plus(v) ;
 	}

 	public Vector addRev(float v) {
 		return this.plus(v) ;
 	}

 	public Vector add(double v) {
 		return this.plus(v) ;
 	}

 	public Vector addRev(double v) {
 		return this.plus(v) ;
 	}

 	/**
 	 * Operator overload support: a-b
 	 */
 	public Vector subtract(Vector v) {
 		return this.minus(v) ;
 	}

 	public Vector subtractRev(Vector v) {
 		return this.times(-1).plus(v) ;
 	}

 	public Vector subtract(int v) {
 		return this.minus(v) ;
 	}

 	public Vector subtractRev(int v) {
 		return this.times(-1).plus(v) ;
 	}

 	public Vector subtract(long v) {
 		return this.minus(v) ;
 	}

 	public Vector subtractRev(long v) {
 		return this.times(-1).plus(v) ;
 	}

 	public Vector subtract(float v) {
 		return this.minus(v) ;
 	}

 	public Vector subtractRev(float v) {
 		return this.times(-1).plus(v) ;
 	}

 	public Vector subtract(double v) {
 		return this.minus(v) ;
 	}

 	public Vector subtractRev(double v) {
 		return this.times(-1).plus(v) ;
 	}

 	/**
 	 * Operator overload support: a*b
 	 */
 	public Vector multiply(Vector v) {
 		return this.timesElement(v);
 	}

 	public Vector multiplyRev(Vector v) {
 		return v.timesElement(this);
 	}

 	public Vector multiply(int v) {
 		return this.times(v);
 	}

 	public Vector multiplyRev(int v) {
 		return this.times(v);
 	}

 	public Vector multiply(long v) {
 		return this.times(v);
 	}

 	public Vector multiplyRev(long v) {
 		return this.times(v);
 	}

 	public Vector multiply(float v) {
 		return this.times(v);
 	}

 	public Vector multiplyRev(float v) {
 		return this.times(v);
 	}

 	public Vector multiply(double v) {
 		return this.times(v);
 	}

 	public Vector multiplyRev(double v) {
 		return this.times(v);
 	}

 	/**
 	 * Operator overload support: a/b
 	 */
 	public Vector divide(Vector v) {
 		return this.divideElement(v) ;
 	}

 	public Vector divideRev(Vector v) {
 		return v.divideElement(this) ;
 	}

 	public Vector divide(int v) {
 		return this.times(1/v) ;
 	}

 	public Vector divideRev(int v) {
 		return constant(dim, v).divideElement(this) ;
 	}

 	public Vector divide(long v) {
 		return this.times(1/v) ;
 	}

 	public Vector divideRev(long v) {
 		return constant(dim, v).divideElement(this) ;
 	}

 	public Vector divide(float v) {
 		return this.times(1/v) ;
 	}

 	public Vector divideRev(float v) {
 		return constant(dim, v).divideElement(this) ;
 	}

 	public Vector divide(double v) {
 		return this.times(1/v) ;
 	}

 	public Vector divideRev(double v) {
 		return constant(dim, v).divideElement(this) ;
 	}

 	/**
 	 * Operator overload support: -a
 	 */
 	public Vector negate() {
 		return this.times(-1) ;
 	}

	// for test
	public static void main(String[] args) {
		Vector vec = new double[] {1,-2.1};
		System.out.println(vec);
		Matrix mat = new double[][] {{2.2, 0}, {-1.0, 10}} ;
		System.out.println(mat);
		double reighly = (vec.transpose()*mat*vec/(vec.transpose()*vec)).getElement(0, 0) ;
		System.out.println(reighly);
		System.out.println(vec.getNorm1());
	}

}
