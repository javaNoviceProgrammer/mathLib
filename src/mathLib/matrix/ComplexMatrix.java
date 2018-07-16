package mathLib.matrix;

import mathLib.numbers.Complex;

/******************************************************************************
 *  Compilation:  javac Matrix.java
 *  Execution:    java Matrix
 *
 *  A bare-bones immutable data type for M-by-N matrices.
 *
 ******************************************************************************/
// Class modified for Complex matrix
final public class ComplexMatrix {
    private final int M;             // number of rows
    private final int N;             // number of columns
    private final Complex[][] data;   // M-by-N array

    // create M-by-N matrix of 0's
    public ComplexMatrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new Complex[M][N];
        for(int i=0; i<M; i++){
        	for(int j=0; j<N; j++){
        		data[i][j] = new Complex(0,0) ;
        	}
        }
    }

    // create matrix based on 2d array --> if data is complex
    public ComplexMatrix(Complex[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new Complex[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[i][j];
    }

    // create matrix based on 2d array --> if data is real
    public ComplexMatrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new Complex[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = new Complex(data[i][j],0) ;
    }

    // create matrix based on 1d array --> if data is complex
    public ComplexMatrix(Complex[] data) {
        M = 1; // default is row matrix
        N = data.length;
        this.data = new Complex[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[j];
    }

    // copy constructor
    @SuppressWarnings("unused")
	private ComplexMatrix(ComplexMatrix A) { this(A.data); }

    // create and return a random M-by-N matrix with values between 0 and 1
    public static ComplexMatrix random(int M, int N) {
        ComplexMatrix A = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[i][j] = new Complex(Math.random(),0) ;
        return A;
    }

    // create and return the N-by-N identity matrix
    public static ComplexMatrix identity(int N) {
        ComplexMatrix I = new ComplexMatrix(N, N);
        for(int i=0; i<N; i++){
        	for(int j=0; j<N; j++){
        		I.data[i][j] = new Complex(0,0) ;
        	}
        }
        for (int i = 0; i < N; i++){
            I.data[i][i] = new Complex(1,0);
        }
        return I;
    }

    // swap rows i and j
/*    public void swap(int i, int j) {
        Complex[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }*/

    // create and return the transpose of the invoking matrix
    public ComplexMatrix transpose() {
        ComplexMatrix A = new ComplexMatrix(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public ComplexMatrix plus(ComplexMatrix B) {
        ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].plus(B.data[i][j]) ;
        return C;
    }

    // return C = A - B
    public ComplexMatrix minus(ComplexMatrix B) {
        ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j].minus(B.data[i][j]) ;
        return C;
    }

    // does A = B exactly?
    public boolean eq(ComplexMatrix B) {
        ComplexMatrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (A.data[i][j] != B.data[i][j]) return false;
        return true;
    }

    // return C = A * B
    public ComplexMatrix times(ComplexMatrix B) {
        ComplexMatrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        ComplexMatrix C = new ComplexMatrix(A.M, B.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = new Complex(0,0) ;
        	}
        }
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] = C.data[i][j].plus(A.data[i][k].times(B.data[k][j]));
        return C;
    }

    // returns multiplication by a complex scalar
    public ComplexMatrix times(Complex a) {
        ComplexMatrix A = this;
        ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].times(a) ;
        	}
        }
        return C;
    }

    // returns multiplication by a double scalar
    public ComplexMatrix times(double a) {
        ComplexMatrix A = this;
        ComplexMatrix C = new ComplexMatrix(A.M, A.N);
        for(int i=0; i<C.M; i++){
        	for(int j=0; j<C.N; j++){
        		C.data[i][j] = A.data[i][j].times(a) ;
        	}
        }
        return C;
    }

    // returns conjugate of a matrix
//    public ComplexMatrix conjugate() {
//        ComplexMatrix A = this;
//        ComplexMatrix C = new ComplexMatrix(A.M, A.N);
//        for(int i=0; i<C.M; i++){
//        	for(int j=0; j<C.N; j++){
//        		C.data[i][j] = A.data[i][j].conjugate() ;
//        	}
//        }
//        return C;
//    }


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
    public Complex getElement(int i, int j){
    	return this.data[i][j] ;
    }

    // For testing ********************************************************
    public static void main(String[] args) {
        Complex[][] d = { { new Complex(1,0), new Complex(2,0) }, {new Complex(2,3), new Complex(-1, -2)}};
        ComplexMatrix A = new ComplexMatrix(d);
        A.show();

       // calculating the inverse matrix of the 2x2 matrix
        Complex one = new Complex(1,0) ;

		Complex[][] data = new Complex[2][2] ;
		Complex term1 = A.getElement(0, 0).times(A.getElement(1, 1)) ;
		Complex term2 = A.getElement(0, 1).times(A.getElement(1, 0)) ;
		Complex detM = term1.minus(term2) ;
		data[0][0] = one.divides(detM).times(A.getElement(1, 1)) ;
		data[0][1] = one.divides(detM).times(A.getElement(0, 1)).times(-1) ;
		data[1][0] = one.divides(detM).times(A.getElement(1, 0)).times(-1) ;
		data[1][1] = one.divides(detM).times(A.getElement(0, 0)) ;
		ComplexMatrix invA = new ComplexMatrix(data) ;
		invA.show() ;
		invA.times(A).show(); ;

/*        ComplexMatrix B = A.transpose();
        System.out.printf("" + B.getElement(1, 0) + "  ") ;
        System.out.println();
        System.out.println();
        B.show();
        System.out.println();

        ComplexMatrix C = ComplexMatrix.identity(5);
        C.show();
        System.out.println();

        A.plus(B).show();
        System.out.println();

        B.times(A).show();
        System.out.println();

        A.times(B).show();
        System.out.println();

        // shouldn't be equal since AB != BA in general
        System.out.println(A.times(B).eq(B.times(A)));
        System.out.println();

        ComplexMatrix b = ComplexMatrix.random(5, 1);
        b.show();
        System.out.println();*/

    }
    //********************************************************************************
}
