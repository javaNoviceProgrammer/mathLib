package tests;

import static mathLib.numbers.Complex.j;

import mathLib.matrix.sys.ComplexLinearSystem;
import mathLib.numbers.Complex;
import mathLib.util.Timer;

public class TestComplexLinearSystem {
	public static void main(String[] args) {
		Complex[][] A = new Complex[][] {{1-j, 2*j, 3.0,1}, {0.5+j/2, j+1, 2,1}, {1, 1, 1,1}, {2,2,2,3}} ;
		Complex[] y = new Complex[] {2.0, 3*j, j-2, -1} ;
		Complex[] solution = new Complex[4] ;
		Timer timer = new Timer() ;
		timer.start();
		new ComplexLinearSystem(A, y, solution) ;
		timer.end();
		System.out.println(timer);
		for(int i=0; i<solution.length; i++) {
			System.out.println(solution[i]);
		}
	}
}
