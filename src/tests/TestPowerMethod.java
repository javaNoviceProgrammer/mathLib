package tests;


import mathLib.matrix.powerIter.EigenValueVector;
import mathLib.matrix.powerIter.PowerIterationMatrix;
import mathLib.matrix.powerIter.methods.InverseIteration;
import mathLib.matrix.powerIter.methods.PowerIteration;
import mathLib.matrix.powerIter.methods.RegularIteration;
import mathLib.matrix.powerIter.methods.ShiftedIteration;
import mathLib.numbers.Complex;
import mathLib.util.Timer;

import static mathLib.numbers.Complex.*;

public class TestPowerMethod {
	
	public static void main(String[] args) {
		double error = 1e-15 ;
		Timer timer = new Timer() ;
		timer.start(); 
		Complex[][] d = new Complex[][] {{1,2*j, 3}, {-2, j-1, 1}, {1,1,1}} ;
		PowerIterationMatrix mat = new PowerIterationMatrix(d) ;
		PowerIteration iteration = new InverseIteration() ;
		PowerIteration regular = new RegularIteration() ;
		EigenValueVector v = iteration.solve(mat, error) ;
		EigenValueVector v1 = regular.solve(mat, error) ;
		iteration.printCommandLine(v);
		System.out.println("\n");
		regular.printCommandLine(v1);
		
		System.out.println("\n\n--------------\n");
		ShiftedIteration shift = new ShiftedIteration() ;
		shift.setEstimatevalue(j);
		EigenValueVector v2 = shift.solve(mat, error) ;
		shift.printCommandLine(v2);
		
		System.out.println("\n");
		
		shift.setEstimatevalue(5);
		EigenValueVector v3 = shift.solve(mat, error) ;
		shift.printCommandLine(v3);
		
		System.out.println("\n");
		
		shift.setEstimatevalue(-20);
		EigenValueVector v4 = shift.solve(mat, error) ;
		shift.printCommandLine(v4);
		timer.end();
		System.out.println("\n");
		System.out.println(timer);
	}

}
