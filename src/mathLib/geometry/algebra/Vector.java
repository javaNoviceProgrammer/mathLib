package mathLib.geometry.algebra;

import static java.lang.Math.*;

import mathLib.utils.MathUtils;

public class Vector {
	
	private double[] data ;
	private int dim ;
	
	public Vector(double[] data) {
		this.data = data ;
		dim = data.length ;
	}
	
	public int getDim() {
		return dim ;
	}
	
	public Vector(int dim) {
		this.dim = dim ;
		this.data = new double[dim] ;
	}
	
	public double getNorm2() {
		double norm = 0 ;
		for(int i=0; i<dim; i++) {
			norm += data[i]*data[i] ;
		}
		return Math.sqrt(norm) ;
	}
	
	public double getNorm1() {
		double norm = 0 ;
		for(int i=0; i<dim; i++) {
			norm += Math.abs(data[i]) ;
		}
		return norm ;
	}
	
	public double getNormInf() {
		double norm = Double.MIN_VALUE ;
		for(int i=0; i<dim; i++) {
			if(norm >= Math.abs(data[i])) 
				norm = Math.abs(data[i]) ;
		}
		return norm ;
	}
	
	public double getNormP(double p) {
		double norm = 0 ;
		for(int i=0; i<dim; i++) {
			norm += pow(pow(abs(data[i]), p), 1.0/p) ;
		}
		return norm ;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder() ;
		s.append("[") ;
		for(int i=0; i<dim-1; i++) {
			s.append(data[i]) ;
			s.append(", ") ;
		}
		s.append(data[dim-1]) ;
		s.append("]") ;
		
		return s;
	}
	
	
	// for test
	public static void main(String[] args) {
		double[] a = MathUtils.linspace(0, 10, 100) ;
		Vector vec = new Vector(a) ;
		System.out.println(vec);
		
	}
	

}
