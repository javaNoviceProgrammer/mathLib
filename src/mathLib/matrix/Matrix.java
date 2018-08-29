package mathLib.matrix;

import flanagan.roots.RealRoot;
import flanagan.roots.RealRootFunction;
import mathLib.geometry.algebra.Vector;
import mathLib.matrix.powerIter.PowerIterationMatrix;

public class Matrix {

    int M;             // number of rows
    int N;             // number of columns
    double[][] data;   // M-by-N array

    // create M-by-N matrix of 0's
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new double[M][N];
        for(int i=0; i<M; i++){
        	for(int j=0; j<N; j++){
        		data[i][j] = 0 ;
        	}
        }
    }

    // create matrix based on 2d array --> if data is complex
    public Matrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[i][j];
    }

    // create matrix based on 1d array
    public Matrix(double[] data) {
        M = 1; // default is row matrix
        N = data.length;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[j];
    }

    public int[] getSize() {
    	return new int[] {M, N} ;
    }

    public int getRowDim() {
    	return M ;
    }

    public int getColumnDim() {
    	return N ;
    }

    public double[][] getData() {
    	return this.data ;
    }

    // create and return a random M-by-N matrix with values between 0 and 1
    public static Matrix random(int M, int N) {
        Matrix A = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[i][j] = Math.random() ;
        return A;
    }

    // create and return the N-by-N identity matrix
    public static Matrix identity(int N) {
        Matrix I = new Matrix(N, N);
        for(int i=0; i<N; i++){
        	for(int j=0; j<N; j++){
        		I.data[i][j] = 0 ;
        	}
        }
        for (int i = 0; i < N; i++){
            I.data[i][i] = 1 ;
        }
        return I;
    }

    // create and return the M-by-N constant matrix
    public static Matrix constant(int M, int N, double c) {
        Matrix C = new Matrix(M, N);
        for(int i=0; i<M; i++){
        	for(int j=0; j<N; j++){
        		C.data[i][j] = c ;
        	}
        }
        return C;
    }

    // create and return the M-by-N constant matrix
    public static Matrix diag(double[] var) {
    	int M = var.length ;
        Matrix C = new Matrix(M, M);
        for(int i=0; i<M; i++){
        	C.data[i][i] = var[i] ;
        }
        return C;
    }

    // create and return the transpose of the invoking matrix
    public Matrix transpose() {
        Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public Matrix plus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] + B.data[i][j] ;
        return C;
    }

    // return C = A + a
    public Matrix plus(double a) {
        Matrix A = this;
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] + a ;
        return C;
    }

    // return C = A - B
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] - B.data[i][j] ;
        return C;
    }

    // return C = A - a
    public Matrix minus(double a) {
        Matrix A = this;
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] - a ;
        return C;
    }

    // does A = B exactly?
    public boolean equals(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (A.data[i][j] != B.data[i][j]) return false;
        return true;
    }

    // does A = B within a threshold?
    public boolean equals(Matrix B, double tol) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (Math.abs(A.data[i][j]-B.data[i][j]) > tol) return false;
        return true;
    }

    // return C = A * B
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.M, B.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = 0 ;
        	}
        }
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] = C.data[i][j] + (A.data[i][k] * B.data[k][j]) ;
        return C;
    }

    // returns multiplication by a double scalar
    public Matrix times(double a) {
        Matrix A = this;
        Matrix C = new Matrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j] * a ;
        	}
        }
        return C;
    }

    public Matrix pow(int k) {
    	if(M != N)
    		throw new IllegalArgumentException("must be a square matrix!") ;
    	if(k == 0)
    		return identity(M) ;
    	else if (k==1)
    		return this ;
    	else
    		return this.times(pow(k-1)) ;
    }

    // print matrix to standard output
    public void show() {
        System.out.println(this.toString());
    }


    @Override
	public String toString() {
    	StringBuilder st = new StringBuilder() ;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++){
            	String sign = "" ;
            	String val = "" ;
            	if(data[i][j]>=0) {
            		sign = " " ;
            		val = String.format("%.4f", data[i][j]) ;
            	}
            	else {
            		sign = "-" ;
            		val = String.format("%.4f", Math.abs(data[i][j])) ;
            	}
            	st.append(sign) ;
            	st.append(val) ;
            	st.append("         ") ;
            }
            st.append("\n") ;
        }
        return st ;
	}

	// get the i,j element of the matrix (i=0,1,...  j=0,1,...)
    public double getElement(int i, int j){
    	return this.data[i][j] ;
    }

    // returning all data elements
    public double[][] getElements(){
    	return this.data ;
    }

    // conversion interfaces
    public static Jama.Matrix toJamaMatrix(Matrix A){
    	return new Jama.Matrix(A.data) ;
    }

    public Jama.Matrix getJamaMatrix(){
    	return new Jama.Matrix(this.data) ;
    }

    public double det() {
    	return toJamaMatrix(this).det() ;
    }

    public static PowerIterationMatrix toPowerIterationMatrix(Matrix A){
    	return new PowerIterationMatrix(A.data) ;
    }

    public PowerIterationMatrix getPowerIterationMatrix(){
    	return new PowerIterationMatrix(this.data) ;
    }

    // element-wise operations

    public Matrix timesElement(Matrix B){
        Matrix A = this;
        Matrix C = new Matrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j] * B.data[i][j] ;
        	}
        }
        return C;
    }

    public Matrix divideElement(Matrix B){
        Matrix A = this;
        Matrix C = new Matrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j] / B.data[i][j] ;
        	}
        }
        return C;
    }

    /**
     * sub-Blocks of the matrix
     */

    public Matrix getRow(int row) {
    	int rowSize = N ;
    	double[][] selectedRow = new double[1][rowSize] ;
    	for(int i=0; i<rowSize; i++) {
    		selectedRow[0][i] = data[row][i] ;
    	}
    	return new Matrix(selectedRow) ;
    }

    public Matrix getColumn(int column) {
    	int columnSize = M ;
    	double[][] selectedColumn = new double[columnSize][1] ;
    	for(int i=0; i<columnSize; i++) {
    		selectedColumn[i][0] = data[i][column] ;
    	}
    	return new Matrix(selectedColumn) ;
    }

    /**
     * inverse of the matrix
     *
     * @return Matrix
     */

    public Matrix inv() {
    	Jama.Matrix B = toJamaMatrix(this) ;
    	Jama.Matrix invB = B.inverse() ;
    	return new Matrix(invB.getArray()) ;
    }


    public double findEigenValue(double approx) {
    	Matrix A = this ;
    	RealRootFunction func = new RealRootFunction() {

			@Override
			public double function(double x) {
				Matrix temp = A - x * identity(M) ;
				return temp.det();
			}
		};

		RealRoot root = new RealRoot() ;
		root.setEstimate(approx);
		return root.bisect(func, 0.5*approx, 1.5*approx) ;

    }

    // ************ operator overloading **********************

    public static Matrix valueOf(double[][] v) {
    	return new Matrix(v) ;
    }

    public static Matrix valueOf(double[] v) {
    	return new Matrix(v) ;
    }

    public static Matrix valueOf(int[][] v) {
    	int M = v.length ;
    	int N = v[0].length ;
    	double[][] data = new double[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = v[i][j] ;
    	return new Matrix(data) ;
    }

    public static Matrix valueOf(int[] v) {
    	int M = v.length ;
    	double[] data = new double[M] ;
    	for(int i=0; i<M; i++)
    			data[i] = (double) v[i] ;
    	return new Matrix(data) ;
    }

    public static Matrix valueOf(float[][] v) {
    	int M = v.length ;
    	int N = v[0].length ;
    	double[][] data = new double[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = v[i][j] ;
    	return new Matrix(data) ;
    }

    public static Matrix valueOf(float[] v) {
    	int M = v.length ;
    	double[] data = new double[M] ;
    	for(int i=0; i<M; i++)
    			data[i] = (double) v[i] ;
    	return new Matrix(data) ;
    }

    public static Matrix valueOf(long[][] v) {
    	int M = v.length ;
    	int N = v[0].length ;
    	double[][] data = new double[M][N] ;
    	for(int i=0; i<M; i++)
    		for(int j=0; j<N; j++)
    			data[i][j] = v[i][j] ;
    	return new Matrix(data) ;
    }

    public static Matrix valueOf(long[] v) {
    	int M = v.length ;
    	double[] data = new double[M] ;
    	for(int i=0; i<M; i++)
    			data[i] = (double) v[i] ;
    	return new Matrix(data) ;
    }

 	/**
 	 * Operator overload support: a+b
 	 */
 	public Matrix add(Matrix v) {
 		return this.plus(v) ;
 	}

 	public Matrix addRev(Matrix v) {
 		return v.plus(this) ;
 	}

 	public Matrix add(int v) {
 		return this.plus(v) ;
 	}

 	public Matrix addRev(int v) {
 		return this.plus(v) ;
 	}

 	public Matrix add(long v) {
 		return this.plus(v) ;
 	}

 	public Matrix addRev(long v) {
 		return this.plus(v) ;
 	}

 	public Matrix add(float v) {
 		return this.plus(v) ;
 	}

 	public Matrix addRev(float v) {
 		return this.plus(v) ;
 	}

 	public Matrix add(double v) {
 		return this.plus(v) ;
 	}

 	public Matrix addRev(double v) {
 		return this.plus(v) ;
 	}

 	/**
 	 * Operator overload support: a-b
 	 */
 	public Matrix subtract(Matrix v) {
 		return this.minus(v) ;
 	}

 	public Matrix subtractRev(Matrix v) {
 		return this.times(-1).plus(v) ;
 	}

 	public Matrix subtract(int v) {
 		return this.minus(v) ;
 	}

 	public Matrix subtractRev(int v) {
 		return this.times(-1).plus(v) ;
 	}

 	public Matrix subtract(long v) {
 		return this.minus(v) ;
 	}

 	public Matrix subtractRev(long v) {
 		return this.times(-1).plus(v) ;
 	}

 	public Matrix subtract(float v) {
 		return this.minus(v) ;
 	}

 	public Matrix subtractRev(float v) {
 		return this.times(-1).plus(v) ;
 	}

 	public Matrix subtract(double v) {
 		return this.minus(v) ;
 	}

 	public Matrix subtractRev(double v) {
 		return this.times(-1).plus(v) ;
 	}

 	/**
 	 * Operator overload support: a*b
 	 */
 	public Matrix multiply(Matrix v) {
 		return this.times(v);
 	}

 	public Matrix multiply(Vector v) {
 		return this.times(v.asMatrix());
 	}

 	public Matrix multiplyRev(Matrix v) {
 		return v.times(this);
 	}

 	public Matrix multiply(int v) {
 		return this.times(v);
 	}

 	public Matrix multiplyRev(int v) {
 		return this.times(v);
 	}

 	public Matrix multiply(long v) {
 		return this.times(v);
 	}

 	public Matrix multiplyRev(long v) {
 		return this.times(v);
 	}

 	public Matrix multiply(float v) {
 		return this.times(v);
 	}

 	public Matrix multiplyRev(float v) {
 		return this.times(v);
 	}

 	public Matrix multiply(double v) {
 		return this.times(v);
 	}

 	public Matrix multiplyRev(double v) {
 		return this.times(v);
 	}

 	/**
 	 * Operator overload support: a/b
 	 */
 	public Matrix divide(Matrix v) {
 		return this.divideElement(v) ;
 	}

 	public Matrix divideRev(Matrix v) {
 		return v.divideElement(this) ;
 	}

 	public Matrix divide(int v) {
 		return this.times(1/v) ;
 	}

 	public Matrix divideRev(int v) {
 		return constant(M, N, v).divideElement(this) ;
 	}

 	public Matrix divide(long v) {
 		return this.times(1/v) ;
 	}

 	public Matrix divideRev(long v) {
 		return constant(M, N, v).divideElement(this) ;
 	}

 	public Matrix divide(float v) {
 		return this.times(1/v) ;
 	}

 	public Matrix divideRev(float v) {
 		return constant(M, N, v).divideElement(this) ;
 	}

 	public Matrix divide(double v) {
 		return this.times(1/v) ;
 	}

 	public Matrix divideRev(double v) {
 		return constant(M, N, v).divideElement(this) ;
 	}

 	/**
 	 * Operator overload support: -a
 	 */
 	public Matrix negate() {
 		return this.times(-1) ;
 	}

 	// for test
 	public static void main(String[] args) {
 		double[][] d = new double[][] {{1, 2}, {1, 1}} ;
		Matrix A = new Matrix(d) ;

		System.out.println(A);
		System.out.println(A.pow(5));
		System.out.println(A.pow(20));

//		double eigen = A.findEigenValue(1.2) ;
//		System.out.println(eigen);
//
//		EigenvalueDecomposition eg = A.getJamaMatrix().eig() ;
//		for(int i=0; i<eg.getRealEigenvalues().length; i++) {
//			System.out.println(eg.getRealEigenvalues()[i] + " + i + " + eg.getImagEigenvalues()[i]);
//		}

//		System.out.println(A.getRow(0));
//		System.out.println(A.getRow(1));
//		System.out.println(A.getColumn(0));
//		System.out.println(A.getColumn(1));

		// operator overloading
//		Matrix B = d ;
//		System.out.println(B);
//
//		Matrix C = new int[][]{{1, 2, 3, 4}, {3, 5, 7, -2}} ;
//		System.out.println(C);
//
//		System.out.println(A.inv());
//
//		System.out.println(A.inv() * A);
//		System.out.println(identity(2).equals(A.inv() * A));
//		System.out.println(identity(2).equals(A.inv() * A, 1e-10));
	}

}
