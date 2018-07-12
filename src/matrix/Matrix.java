package matrix;


/******************************************************************************
 *  Compilation:  javac Matrix.java
 *  Execution:    java Matrix
 *
 *  A bare-bones immutable data type for M-by-N matrices.
 *
 ******************************************************************************/
// Class for real matrix --> No complex elements
final public class Matrix {
    private final int M;             // number of rows
    private final int N;             // number of columns
    private final double[][] data;   // M-by-N array

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
 
    
    // create matrix based on 1d array --> if data is complex
    public Matrix(double[] data) {
        M = 1; // default is row matrix
        N = data.length;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[j];
    }

    // copy constructor
    @SuppressWarnings("unused")
	private Matrix(Matrix A) { this(A.data); }

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

    // swap rows i and j
    public void swap(int i, int j) {
        double[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
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

    // does A = B exactly?
    public boolean eq(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (A.data[i][j] != B.data[i][j]) return false;
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
 
    
    // print matrix to standard output
    public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                System.out.printf("" + data[i][j] +  "           ");
            System.out.println();
        }
        System.out.println();
    }
    
    // get the i,j element of the matrix (i=0,1,...  j=0,1,...)
    public double getElement(int i, int j){
    	return this.data[i][j] ;
    }
    
    // returning all data elements
    public double[][] getAllElements(){
    	return this.data ;
    }

}
